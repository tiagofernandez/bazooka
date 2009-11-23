package bazooka.client;

import bazooka.common.exception.NonExistingShooterException;
import bazooka.common.service.ShooterService;
import bazooka.common.service.ShooterServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class ContentPanel extends Composite {

  interface Binder extends UiBinder<Widget, ContentPanel> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField Panel scriptPanel;
  @UiField Button saveScriptButton;
  @UiField Button cancelButton;
  @UiField DivElement scriptTextAreaDiv;
  @UiField Panel messagePanel;

  @UiField ListBox requestList;
  @UiField Button deleteRequestButton;
  @UiField Button saveRequestAsButton;
  @UiField Button shootButton;
  @UiField DivElement requestTextAreaDiv;
  @UiField DivElement responseTextAreaDiv;

  private TextArea scriptTextArea;
  private TextArea requestTextArea;
  private TextArea responseTextArea;

  private final Map<String, String> requests = new HashMap<String, String>();
  private final ShooterServiceAsync shooterService = GWT.create(ShooterService.class);

  private ShooterPanel shooterPanel;

  ContentPanel() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override protected void onLoad() {
    loadTextAreas();
    populateRequestList();
  }

  @UiHandler("saveScriptButton")
  void onSaveScriptButtonClicked(ClickEvent event) {
    saveScript(scriptTextArea.getText(), shooterPanel.getSelectedShooter());
  }

  @UiHandler("cancelButton")
  void onCancelButtonClicked(ClickEvent event) {
    shooterPanel.onSelectedShooterClicked();
    getScript(shooterPanel.getSelectedShooter());
  }

  @UiHandler("saveRequestAsButton")
  void onSaveRequestAsButtonClicked(ClickEvent event) {
    String newRequestName = askNewRequestName();
    if (newRequestName != null) {
      addRequest(newRequestName, getRequest());
      selectLastRequest();
      enableDeleteRequestButton();
    }
  }

  @UiHandler("deleteRequestButton")
  void onDeleteRequestButtonClicked(ClickEvent event) {
    String selectedRequest = getSelectedRequestName();
    boolean validRequest = !"".equals(selectedRequest);
    if (validRequest && Window.confirm("Are you sure you want to delete '" + selectedRequest + "'?")) {
      removeSelectedRequest();
      disableDeleteRequestButton();
    }
  }

  @UiHandler("shootButton")
  void onShootButtonClicked(ClickEvent event) {
    if (shooterPanel.hasSelectedShooter()) {
      selectDefaultRequestIfChanged();
      responseTextArea.setValue("BOOM!");
    }
    else
      Window.alert("Please select a shooter.");
  }

  @UiHandler("requestList")
  void onRequestListChanged(ChangeEvent event) {
    requestTextArea.setValue(requests.get(getSelectedRequestName()));
    if (getSelectedRequestIndex() == 0)
      disableDeleteRequestButton();
    else
      enableDeleteRequestButton();
  }

  @UiHandler("requestList")
  void onRequestListKeyDown(KeyDownEvent event) {
    onRequestListChanged(null);
  }

  @UiHandler("requestList")
  void onRequestListKeyUp(KeyUpEvent event) {
    onRequestListChanged(null);
  }

  void saveScript(final String script, final String shooterName) {
    AsyncCallback<Void> callback = new AsyncCallback<Void>() {
      public void onFailure(Throwable caught) {
        if (caught instanceof NonExistingShooterException)
          Window.alert("Cannot save script for a non-existing shooter.");
        else
          Window.alert("Error while saving script: " + caught.getMessage());
      }
      public void onSuccess(Void success) {
        shooterPanel.showMessagePanel();
        shooterPanel.enableEditButton();
      }
    };
    shooterService.updateShooter(shooterName, script, callback);
  }

  void getScript(final String shooterName) {
    AsyncCallback<String> callback = new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while getting script: " + caught.getMessage());
      }
      public void onSuccess(String script) {
        scriptTextArea.setValue(script);
      }
    };
    shooterService.getScript(shooterName, callback);
  }

  void showScriptPanel() {
    scriptPanel.setVisible(true);
  }

  void showMessagePanel() {
    messagePanel.setVisible(true);
  }

  void hideScriptPanel() {
    scriptPanel.setVisible(false);
  }

  void hideMessagePanel() {
    messagePanel.setVisible(false);
  }

  void setShooterPanel(ShooterPanel shooterPanel) {
    this.shooterPanel = shooterPanel;
  }

  private void loadTextAreas() {
    loadScriptTextArea();
    loadRequestTextArea();
    loadResponseTextArea();
  }

  private void loadScriptTextArea() {
    scriptTextArea = new TextArea();
    scriptTextArea.setGrow(true);
    scriptTextArea.setGrowMin(300);
    scriptTextArea.setGrowMax(550);
    scriptTextArea.setWidth("100%");
    scriptTextAreaDiv.insertFirst(scriptTextArea.getElement());
  }

  private void loadRequestTextArea() {
    requestTextArea = new TextArea();
    requestTextArea.setGrow(true);
    requestTextArea.setGrowMax(240);
    requestTextArea.setWidth("100%");
    requestTextArea.addListener(new FieldListenerAdapter() {
      @Override public void onChange(Field field, Object newVal, Object oldVal) {
        selectDefaultRequestIfChanged();
      }
    });
    requestTextAreaDiv.insertFirst(requestTextArea.getElement());
  }

  private void loadResponseTextArea() {
    responseTextArea = new TextArea();
    responseTextArea.setReadOnly(true);
    responseTextArea.setGrow(true);
    responseTextArea.setGrowMax(240);
    responseTextArea.setWidth("100%");
    responseTextAreaDiv.insertFirst(responseTextArea.getElement());
  }

  private void populateRequestList() {
    addEmptyRequest(""); // default
    addRequest("Sample-1", "foo");
    addRequest("Sample-2", "bar");
    addRequest("Sample-3", "baz");
  }

  private void addEmptyRequest(String name) {
    addRequest(name, "");
  }

  private void addRequest(String name, String value) {
    requestList.addItem(name);
    requests.put(name, value);
  }

  private void removeSelectedRequest() {
    requestList.removeItem(getSelectedRequestIndex());
    requests.remove(getSelectedRequestName());
  }

  private boolean containsRequest(String name) {
    for (int i = 0; i < requestList.getItemCount(); i++) {
      String item = requestList.getItemText(i);
      if (item.trim().equalsIgnoreCase(name.trim()))
        return true;
    }
    return false;
  }

  private String getRequest() {
    return requestTextArea.getText();
  }

  private String getSelectedRequestName() {
    return requestList.getItemText(getSelectedRequestIndex());
  }

  private int getSelectedRequestIndex() {
    return requestList.getSelectedIndex();
  }

  private void selectDefaultRequestIfChanged() {
    if (hasRequestChanged())
      selectFirstRequest();
  }

  private void selectFirstRequest() {
    requestList.setSelectedIndex(0);
    disableDeleteRequestButton();
  }

  private void selectLastRequest() {
    int lastIndex = requestList.getItemCount() - 1;
    requestList.setSelectedIndex(lastIndex);
  }

  private boolean hasRequestChanged() {
    String savedRequest = requests.get(getSelectedRequestName());
    return !savedRequest.equals(getRequest());
  }

  private void enableDeleteRequestButton() {
    deleteRequestButton.setEnabled(true);
  }

  private void disableDeleteRequestButton() {
    deleteRequestButton.setEnabled(false);
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