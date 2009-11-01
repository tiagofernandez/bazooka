package bazooka.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class Content extends Composite {

  interface Binder extends UiBinder<Widget, Content> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField Panel scriptPanel;
  @UiField Button saveScriptButton;
  @UiField DivElement scriptTextAreaDiv;
  @UiField Panel messagePanel;

  @UiField ListBox requestList;
  @UiField Button deleteRequestButton;
  @UiField Button saveRequestAsButton;
  @UiField Button shootButton;
  @UiField DivElement requestTextAreaDiv;
  @UiField DivElement responseTextAreaDiv;

  TextArea scriptTextArea;
  TextArea requestTextArea;
  TextArea responseTextArea;

  Map<String, String> requests = new HashMap<String, String>();

  Shooter shooter;

  Content() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override protected void onLoad() {
    loadTextAreas();
    loadRequestList();
    populateRequestList();
  }

  @UiHandler("saveScriptButton")
  void onSaveScriptButtonClicked(ClickEvent event) {
    shooter.showMessagePanel();
    shooter.enableEditButton();
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
    selectDefaultRequestIfChanged();
    responseTextArea.setValue("BOOM!");
  }

  void loadTextAreas() {
    loadScriptTextArea();
    loadRequestTextArea();
    loadResponseTextArea();
  }

  void loadScriptTextArea() {
    scriptTextArea = new TextArea();
    scriptTextArea.setGrow(true);
    scriptTextArea.setGrowMin(300);
    scriptTextArea.setGrowMax(550);
    scriptTextArea.setWidth("100%");
    scriptTextAreaDiv.insertFirst(scriptTextArea.getElement());
  }

  void loadRequestTextArea() {
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

  void loadResponseTextArea() {
    responseTextArea = new TextArea();
    responseTextArea.setReadOnly(true);
    responseTextArea.setGrow(true);
    responseTextArea.setGrowMax(240);
    responseTextArea.setWidth("100%");
    responseTextAreaDiv.insertFirst(responseTextArea.getElement());
  }

  private void loadRequestList() {
    requestList.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        requestTextArea.setValue(requests.get(getSelectedRequestName()));
        if (getSelectedRequestIndex() == 0)
          disableDeleteRequestButton();
        else
          enableDeleteRequestButton();
      }
    });
  }

  void populateRequestList() {
    addEmptyRequest(""); // default
    addRequest("Sample-1", "foo");
    addRequest("Sample-2", "bar");
    addRequest("Sample-3", "baz");
  }

  void addEmptyRequest(String name) {
    addRequest(name, "");
  }

  void addRequest(String name, String value) {
    requestList.addItem(name);
    requests.put(name, value);
  }

  void removeSelectedRequest() {
    requestList.removeItem(getSelectedRequestIndex());
    requests.remove(getSelectedRequestName());
  }

  boolean containsRequest(String name) {
    for (int i = 0; i < requestList.getItemCount(); i++) {
      String item = requestList.getItemText(i);
      if (item.trim().equalsIgnoreCase(name.trim()))
        return true;
    }
    return false;
  }

  String getRequest() {
    return requestTextArea.getText();
  }

  String getSelectedRequestName() {
    return requestList.getItemText(getSelectedRequestIndex());
  }

  int getSelectedRequestIndex() {
    return requestList.getSelectedIndex();
  }

  void selectDefaultRequestIfChanged() {
    if (hasRequestChanged())
      selectFirstRequest();
  }

  void selectFirstRequest() {
    requestList.setSelectedIndex(0);
    disableDeleteRequestButton();
  }

  void selectLastRequest() {
    int lastIndex = requestList.getItemCount() - 1;
    requestList.setSelectedIndex(lastIndex);
  }

  boolean hasRequestChanged() {
    String savedRequest = requests.get(getSelectedRequestName());
    if (!savedRequest.equals(getRequest()))
      return true;
    else
      return false;
  }

  void enableDeleteRequestButton() {
    deleteRequestButton.setEnabled(true);
  }

  void disableDeleteRequestButton() {
    deleteRequestButton.setEnabled(false);
  }

  String askNewRequestName() {
    String newName = Window.prompt("Please specify the request name.", "My Request");
    while (containsRequest(newName)) {
      Window.alert("'" + newName + "' already exists, please choose a different name.");
      newName = askNewRequestName();
    }
    return newName;
  }
}