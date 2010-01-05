package bazooka.client;

import bazooka.common.exception.ExistingRequestException;
import bazooka.common.exception.NonExistingShooterException;
import bazooka.common.model.*;
import bazooka.common.service.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.form.TextArea;

import java.util.*;

public class ContentPanel extends ResizeComposite {

  interface Binder extends UiBinder<Widget, ContentPanel> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField Panel scriptPanel;
  @UiField Button saveScriptButton;
  @UiField Button cancelButton;
  @UiField DivElement scriptTextAreaDiv;
  @UiField Panel messagePanel;

  @UiField ListBox requestList;
  @UiField Button deleteRequestButton;
  @UiField Button saveRequestButton;
  @UiField Button saveRequestAsButton;
  @UiField Button shootButton;
  @UiField Image loadingImage;
  @UiField SpanElement textAreaSpan;
  @UiField DivElement requestTextAreaDiv;
  @UiField DivElement responseTextAreaDiv;

  TextArea scriptTextArea;
  TextArea requestTextArea;
  TextArea responseTextArea;

  private final ShooterServiceAsync shooterService = GWT.create(ShooterService.class);
  private final RequestServiceAsync requestService = GWT.create(RequestService.class);

  private final Map<String, Request> requests = new HashMap<String, Request>();

  private ShooterPanel shooterPanel;
  private ConfigurationPanel configurationPanel;

  ContentPanel() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override protected void onLoad() {
    loadTextAreas();
    populateRequestList();
  }

  @UiHandler("saveScriptButton")
  void onSaveScriptButtonClicked(ClickEvent event) {
    saveScript(scriptTextArea.getText());
  }

  @UiHandler("cancelButton")
  void onCancelButtonClicked(ClickEvent event) {
    shooterPanel.onSelectedShooterClicked();
    scriptTextArea.setValue(shooterPanel.getSelectedShooter().getScript());
  }

  @UiHandler("saveRequestButton")
  void onSaveRequestButtonClicked(ClickEvent event) {
    if (isFirstRequestSelected())
      onSaveRequestAsButtonClicked(event);
    else
      updateExistingRequest(getCurrentPayload());
  }

  @UiHandler("saveRequestAsButton")
  void onSaveRequestAsButtonClicked(ClickEvent event) {
    String newRequestName = askNewRequestName();
    if (newRequestName != null)
      createNewRequest(newRequestName, requestTextArea.getText());
  }

  @UiHandler("deleteRequestButton")
  void onDeleteRequestButtonClicked(ClickEvent event) {
    if (!isFirstRequestSelected() && Window.confirm("Are you sure you want to delete '" + getSelectedRequestName() + "'?"))
      deleteExistingRequest(getSelectedRequest());
  }

  @UiHandler("shootButton")
  void onShootButtonClicked(ClickEvent event) {
    if (shooterPanel.hasSelectedShooter()) {
      loadingImage.setVisible(true);

      shoot(shooterPanel.getSelectedShooter(), getCurrentRequest(),
        configurationPanel.getCurrentConfiguration());
    }
    else
      Window.alert("Please select a shooter.");
  }

  @UiHandler("requestList")
  void onRequestListChanged(ChangeEvent event) {
    clearResponseTextArea();
    enableSaveRequestButton();
    
    if (isFirstRequestSelected())
      clearRequestTextArea();
    else
      requestTextArea.setValue(getSelectedPayload());
  }

  @UiHandler("requestList")
  void onRequestListKeyDown(KeyDownEvent event) {
    onRequestListChanged(null);
  }

  @UiHandler("requestList")
  void onRequestListKeyUp(KeyUpEvent event) {
    onRequestListChanged(null);
  }

  void saveScript(final String script) {
    final Shooter shooter = shooterPanel.getSelectedShooter();
    shooter.setScript(script);

    shooterService.updateShooter(shooter, new AsyncCallback<Shooter>() {
      public void onFailure(Throwable caught) {
        if (caught instanceof NonExistingShooterException)
          Window.alert("Cannot save script for a non-existing shooter.");
        else
          Window.alert("Error while saving script: " + caught.getMessage());
      }
      public void onSuccess(Shooter shooter) {
        shooterPanel.showMessagePanel();
        shooterPanel.enableEditButton();
      }
    });
  }

  void createNewRequest(final String name, final String payload) {
    final Request request = new Request(name, payload);

    requestService.saveRequest(request, new AsyncCallback<Request>() {
      public void onFailure(Throwable caught) {
        if (caught instanceof ExistingRequestException)
          Window.alert("This name is already taken.");
        else
          Window.alert("Error while creating request: " + caught.getMessage());
      }
      public void onSuccess(Request request) {
        addRequest(request);
        selectLastRequest();
      }
    });
  }

  void updateExistingRequest(final String payload) {
    final Request request = getSelectedRequest();
    request.setPayload(payload);

    requestService.updateRequest(request, new AsyncCallback<Request>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while updating request: " + caught.getMessage());
      }
      public void onSuccess(Request request) {
        requests.put(request.getName(), request);
        disableSaveRequestButton();
      }
    });
  }

  void deleteExistingRequest(final Request request) {
    requestService.deleteRequest(request, new AsyncCallback<Void>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while deleting request: " + caught.getMessage());
      }
      public void onSuccess(Void success) {
        removeSelectedRequest();
      }
    });
  }

  void shoot(final Shooter shooter, final Request request, final Configuration config) {
    shooterService.shoot(shooter, request, config, new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        responseTextArea.setValue(caught.getMessage());
        loadingImage.setVisible(false);
      }
      public void onSuccess(String result) {
        responseTextArea.setValue(result);
        loadingImage.setVisible(false);
      }
    });
  }

  void populateRequestList() {
    addRequest(new Request("")); // default

    requestService.listRequests(new AsyncCallback<List<Request>>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while retrieving requests: " + caught.getMessage());
      }
      public void onSuccess(List<Request> requests) {
        for (Request request : requests)
          addRequest(request);
      }
    });
  }

  void showScriptPanel() {
    scriptPanel.setVisible(true);
    scriptTextArea.autoSize();
    scriptTextArea.focus();
  }

  void showMessagePanel() {
    messagePanel.setVisible(true);
    requestTextArea.autoSize();
    requestTextArea.focus();
  }

  void hideScriptPanel() {
    scriptPanel.setVisible(false);
  }

  void hideMessagePanel() {
    messagePanel.setVisible(false);
  }
  
  void enableSaveRequestButton() {
    saveRequestButton.setEnabled(true);
  }
  
  void disableSaveRequestButton() {
    saveRequestButton.setEnabled(false);
  }

  void setShooterPanel(ShooterPanel shooterPanel) {
    this.shooterPanel = shooterPanel;
  }

  void setConfigurationPanel(ConfigurationPanel configurationPanel) {
    this.configurationPanel = configurationPanel;
  }

  void setScript(String script) {
    scriptTextArea.setValue(script);
  }

  private void loadTextAreas() {
    loadScriptTextArea();
    loadRequestTextArea();
    loadResponseTextArea();
  }

  private void loadScriptTextArea() {
    scriptTextArea = new TextArea();
    scriptTextArea.setGrow(true);
    scriptTextArea.setWidth("100%");
    scriptTextArea.setStyleName(textAreaSpan.getClassName());
    scriptTextAreaDiv.insertFirst(scriptTextArea.getElement());
  }

  private void loadRequestTextArea() {
    requestTextArea = new TextArea();
    requestTextArea.setGrow(true);
    requestTextArea.setGrowMax(400);
    requestTextArea.setWidth("100%");
    requestTextArea.setStyleName(textAreaSpan.getClassName());
    requestTextArea.addKeyPressListener(new com.gwtext.client.core.EventCallback() {
      public void execute(com.gwtext.client.core.EventObject event) {
        enableSaveRequestButton();
      }
    });
    requestTextAreaDiv.insertFirst(requestTextArea.getElement());
  }

  private void loadResponseTextArea() {
    responseTextArea = new TextArea();
    responseTextArea.setReadOnly(true);
    responseTextArea.setGrow(true);
    responseTextArea.setWidth("100%");
    responseTextArea.setStyleName(textAreaSpan.getClassName());
    responseTextAreaDiv.insertFirst(responseTextArea.getElement());
  }

  private void addRequest(Request request) {
    requestList.addItem(request.getName());
    requests.put(request.getName(), request);
  }

  private void removeSelectedRequest() {
    requestList.removeItem(getSelectedRequestIndex());
    requests.remove(getSelectedRequestName());
    clearRequestTextArea();
  }

  private boolean containsRequest(String name) {
    for (int i = 0; i < requestList.getItemCount(); i++) {
      String item = requestList.getItemText(i);
      if (item.trim().equalsIgnoreCase(name.trim()))
        return true;
    }
    return false;
  }

  private Request getCurrentRequest() {
    return isFirstRequestSelected() ?
      new Request("Default", requestTextArea.getText()) : getSelectedRequest();
  }

  private String getCurrentPayload() {
    return requestTextArea.getText();
  }

  private void clearRequestTextArea() {
    requestTextArea.setValue("");
  }

  private void clearResponseTextArea() {
    responseTextArea.setValue("");
  }

  private Request getSelectedRequest() {
    return requests.get(getSelectedRequestName());
  }

  private String getSelectedRequestName() {
    return requestList.getItemText(getSelectedRequestIndex());
  }

  private String getSelectedPayload() {
    return getSelectedRequest().getPayload();
  }

  private int getSelectedRequestIndex() {
    return requestList.getSelectedIndex();
  }

  private boolean isFirstRequestSelected() {
    return getSelectedRequestIndex() == 0;
  }

  private void selectLastRequest() {
    int lastIndex = requestList.getItemCount() - 1;
    requestList.setSelectedIndex(lastIndex);
  }

  private String askNewRequestName() {
    String newName = Window.prompt("Please specify the request name.", "My Request");
    while (containsRequest(newName)) {
      Window.alert("'" + newName + "' already exists, please choose a different name.");
      newName = askNewRequestName();
    }
    return newName;
  }
}