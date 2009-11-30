package bazooka.client;

import bazooka.common.model.Configuration;
import bazooka.common.model.Parameter;
import bazooka.common.service.ConfigurationService;
import bazooka.common.service.ConfigurationServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.*;

public class ConfigurationPanel extends Composite {

  interface Binder extends UiBinder<Widget, ConfigurationPanel> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField ListBox configList;
  @UiField Button saveButton;
  @UiField Button saveAsButton;
  @UiField Button deleteButton;
  @UiField Button addParamButton;
  @UiField Button removeParamButton;
  @UiField VerticalPanel parametersPanel;

  private final ConfigurationServiceAsync configurationService = GWT.create(ConfigurationService.class);

  private final Map<String, Configuration> configurations = new HashMap<String, Configuration>();

  ConfigurationPanel() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override protected void onLoad() {
    populateConfigList();
  }

  @UiHandler("configList")
  void onConfigListChanged(ChangeEvent event) {
    reloadParameters();
    disableSaveButton();

    if (isConfigListEmpty())
      disableDeleteButton();
    else
      enableDeleteButton();
  }

  @UiHandler("configList")
  void onConfigListKeyDown(KeyDownEvent event) {
    onConfigListChanged(null);
  }

  @UiHandler("configList")
  void onConfigListKeyUp(KeyUpEvent event) {
    onConfigListChanged(null);
  }

  @UiHandler("saveButton")
  void onSaveButtonClicked(ClickEvent event) {
    refreshParameters();
    updateConfiguration(getSelectedConfiguration());
  }

  @UiHandler("saveAsButton")
  void onSaveAsButtonClicked(ClickEvent event) {
    String newConfigName = askNewConfigName();
    if (newConfigName != null) {
      createConfiguration(new Configuration(newConfigName, getCurrentParameters()));
    }
  }

  @UiHandler("deleteButton")
  void onDeleteButtonClicked(ClickEvent event) {
    if (Window.confirm("Are you sure you want to delete '" + getSelectedConfig() + "'?")) {
      deleteConfiguration(getSelectedConfiguration());
    }
  }

  @UiHandler("addParamButton")
  void onAddParamClicked(ClickEvent event) {
    addParameter("", "");
    addParamButton.setFocus(false);
    enableSaveButton();
  }

  private void createConfiguration(final Configuration configuration) {
    configurationService.saveConfiguration(configuration, new AsyncCallback<Configuration>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while creating configuration: " + caught.getMessage());
      }
      public void onSuccess(Configuration configuration) {
        addConfig(configuration);
        selectLastConfig();
      }
    });
  }

  private void updateConfiguration(final Configuration configuration) {
    configurationService.updateConfiguration(configuration, new AsyncCallback<Configuration>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while saving configuration: " + caught.getMessage());
      }
      public void onSuccess(Configuration configuration) {
        configurations.put(configuration.getName(), configuration);
        disableSaveButton();
      }
    });
  }

  private void deleteConfiguration(final Configuration configuration) {
    configurationService.deleteConfiguration(configuration, new AsyncCallback<Void>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while deleting configuration: " + caught.getMessage());
      }
      public void onSuccess(Void success) {
        removeSelectedConfig();
        selectFirstConfig();
        reloadParameters();
      }
    });
  }

  private void populateConfigList() {
    configurationService.listConfigurations(new AsyncCallback<List<Configuration>>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while retrieving configurations: " + caught.getMessage());
      }
      public void onSuccess(List<Configuration> configurations) {
        for (Configuration config : configurations)
          addConfig(config);
        
        reloadParameters();
      }
    });
  }

  private void reloadParameters() {
    clearParameters();

    if (!isConfigListEmpty())
      for (Parameter param : getSelectedParameters())
        addParameter(param.getKey(), param.getValue());
  }

  private void refreshParameters() {
    getSelectedConfiguration().clearParameters();

    for (int i = parametersPanel.getWidgetCount() - 1; i >= 0 ; i--) {
      HorizontalPanel entry = (HorizontalPanel) parametersPanel.getWidget(i);
      String key = getParameterKey(entry);

      if (mustDiscardParameter(key))
        removeParameter(entry);
      else
        getSelectedConfiguration().addParameter(new Parameter(key, getParameterValue(entry)));
    }
  }

  private void clearParameters() {
    parametersPanel.clear();
  }

  private void addParameter(String key, String value) {
    parametersPanel.add(buildParameterEntry(key, value));
  }

  private void removeParameter(HorizontalPanel entry) {
    parametersPanel.remove(entry);
  }

  private Configuration getSelectedConfiguration() {
    return configurations.get(getSelectedConfig());
  }

  private List<Parameter> getSelectedParameters() {
    return getSelectedConfiguration().getParameters();
  }

  Configuration getCurrentConfiguration() {
    if (isConfigListEmpty()) {
      return new Configuration("None", getCurrentParameters());
    }
    else {
      Configuration config = getSelectedConfiguration();
      config.setParameters(getCurrentParameters());
      return config;
    }
  }

  private List<Parameter> getCurrentParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();

    for (int i = 0; i < parametersPanel.getWidgetCount(); i++) {
      HorizontalPanel entry = (HorizontalPanel) parametersPanel.getWidget(i);
      parameters.add(new Parameter(getParameterKey(entry), getParameterValue(entry)));
    }
    return parameters;
  }
  
  private String getParameterKey(HorizontalPanel entry) {
    TextBox key = (TextBox) entry.getWidget(0);
    return key.getText();
  }

  private String getParameterValue(HorizontalPanel entry) {
    TextBox key = (TextBox) entry.getWidget(2);
    return key.getText();
  }

  private boolean mustDiscardParameter(String key) {
    return "".equals(key.trim());
  }

  private void enableSaveButton() {
    if (!isConfigListEmpty())
      saveButton.setEnabled(true);
  }

  private void disableSaveButton() {
    saveButton.setEnabled(false);
  }

  private void enableDeleteButton() {
    deleteButton.setEnabled(true);
  }

  private void disableDeleteButton() {
    deleteButton.setEnabled(false);
  }

  private void addConfig(Configuration configuration) {
    configList.addItem(configuration.getName());
    configurations.put(configuration.getName(), configuration);
  }

  private void removeSelectedConfig() {
    configurations.remove(getSelectedConfig());
    configList.removeItem(getSelectedConfigIndex());
  }

  private boolean containsConfig(String name) {
    return configurations.containsKey(name);
  }

  private String getSelectedConfig() {
    return configList.getItemText(getSelectedConfigIndex());
  }

  private int getSelectedConfigIndex() {
    return configList.getSelectedIndex();
  }

  private void selectFirstConfig() {
    if (isConfigListEmpty()) {
      disableDeleteButton();
    }
    else {
      enableDeleteButton();
      configList.setSelectedIndex(0);
    }
  }

  private void selectLastConfig() {
    int lastIndex = configList.getItemCount() - 1;
    configList.setSelectedIndex(lastIndex);
  }

  private boolean isConfigListEmpty() {
    return configList.getItemCount() == 0;
  }

  private HorizontalPanel buildParameterEntry(String key, String value) {
    HorizontalPanel entry = new HorizontalPanel();
    entry.setSpacing(5);
    entry.add(buildParameterKeyTextBox(key));
    entry.add(buildEqualsLabel());
    entry.add(buildParameterValueTextBox(value));
    entry.add(buildRemoveParameterButton(entry));
    return entry;
  }

  private TextBox buildParameterKeyTextBox(String key) {
    return buildParameterTextBox(key);
  }

  private TextBox buildParameterValueTextBox(String key) {
    TextBox valueBox = buildParameterTextBox(key);
    valueBox.addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
        if (event.getCharCode() == 9) // horizontal tab
          onAddParamClicked(null);
      }
    });
    return valueBox;
  }

  private TextBox buildParameterTextBox(String text) {
    TextBox paramBox = new TextBox();
    paramBox.setText(text);
    paramBox.setWidth("135");
    paramBox.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        enableSaveButton();
      }
    });
    return paramBox;
  }

  private Label buildEqualsLabel() {
    Label label = new Label("=");
    label.setWidth("5");
    label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    return label;
  }

  private Button buildRemoveParameterButton(final HorizontalPanel entry) {
    Button remButton = new Button();
    remButton.setStyleName(removeParamButton.getStyleName());
    remButton.setHTML(removeParamButton.getHTML());
    remButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        removeParameter(entry);
        enableSaveButton();
      }
    });
    return remButton;
  }

  private String askNewConfigName() {
    String newName = Window.prompt("Please specify the configuration name.", "My Config");
    while (containsConfig(newName)) {
      Window.alert("'" + newName + "' already exists, please choose a different name.");
      newName = askNewConfigName();
    }
    return newName;
  }
}