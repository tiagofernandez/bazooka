package bazooka.client;

import bazooka.common.exception.ExistingShooterException;
import bazooka.common.service.ShooterService;
import bazooka.common.service.ShooterServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.*;

public class ShooterPanel extends Composite {

  interface Binder extends UiBinder<Widget, ShooterPanel> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField Button newButton;
  @UiField Button editButton;
  @UiField Button deleteButton;
  @UiField VerticalPanel shooterPanel;

  private final Map<String, RadioButton> shooters = new HashMap<String, RadioButton>();
  private final ShooterServiceAsync shooterService = GWT.create(ShooterService.class);

  private ConfigurationPanel configurationPanel;

  private ContentPanel contentPanel;

  ShooterPanel() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override protected void onLoad() {
    populateShooters();
    selectFirstShooter();
  }

  @UiHandler("newButton")
  void onNewButtonClicked(ClickEvent event) {
    String newShooterName = askNewShooterName();
    if (newShooterName != null) {
      saveShooter(newShooterName);
    }
  }

  @UiHandler("editButton")
  void onEditButtonClicked(ClickEvent event) {
    contentPanel.getScript(getSelectedShooter());
    disableEditButton();
    enableDeleteButton();
    showScriptPanel();
  }

  @UiHandler("deleteButton")
  void onDeleteButtonClicked(ClickEvent event) {
    String shooterName = getSelectedShooter();
    if (Window.confirm("Are you sure you want to delete '" + shooterName + "'?")) {
      deleteShooter(shooterName);
    }
  }

  void saveShooter(final String shooterName) {
    AsyncCallback<Void> callback = new AsyncCallback<Void>() {
      public void onFailure(Throwable caught) {
        if (caught instanceof ExistingShooterException)
          Window.alert("This name is already taken.");
        else
          Window.alert("Error while creating shooter: " + caught.getMessage());
      }
      public void onSuccess(Void success) {
        RadioButton newShooter = buildShooterRadioButton(shooterName);
        newShooter.setValue(true);
        addShooter(newShooter);
        onEditButtonClicked(null);
      }
    };
    shooterService.saveShooter(shooterName, callback);
  }

  void deleteShooter(final String shooterName) {
    AsyncCallback<Void> callback = new AsyncCallback<Void>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while deleting shooter: " + caught.getMessage());
      }
      public void onSuccess(Void success) {
        removeShooter(getSelectedShooterRadioButton());
        selectFirstShooter();
        showMessagePanel();
      }
    };
    shooterService.deleteShooter(shooterName, callback);
  }

  void populateShooters() {
    AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while retrieving shooters: " + caught.getMessage());
      }
      public void onSuccess(List<String> shooters) {
        for (String shooter : shooters)
          addShooter(buildShooterRadioButton(shooter));
      }
    };
    shooterService.listShooters(callback);
  }

  void showScriptPanel() {
    contentPanel.hideMessagePanel();
    contentPanel.showScriptPanel();
  }

  void showMessagePanel() {
    contentPanel.hideScriptPanel();
    contentPanel.showMessagePanel();
  }

  void enableEditButton() {
    editButton.setEnabled(true);
  }

  void disableEditButton() {
    editButton.setEnabled(false);
  }
  
  void enableDeleteButton() {
    deleteButton.setEnabled(true);
  }

  void disableDeleteButton() {
    deleteButton.setEnabled(false);
  }

  void setConfiguration(ConfigurationPanel configurationPanel) {
    this.configurationPanel = configurationPanel;
  }

  void setContent(ContentPanel contentPanel) {
    this.contentPanel = contentPanel;
  }

  boolean hasSelectedShooter() {
    for (RadioButton shooter : shooters.values()) {
      if (shooter.getValue())
        return true;
    }
    return false;
  }

  String getSelectedShooter() {
    return getSelectedShooterRadioButton().getText();
  }

  private void selectFirstShooter() {
    if (hasAtLeastOneShooter()) {
      getFirstShooter().setValue(true);
      enableEditButton();
      enableDeleteButton();
    }
    else {
      disableEditButton();
      disableDeleteButton();
    }
  }

  private boolean hasAtLeastOneShooter() {
    return shooterPanel.getWidgetCount() > 0;
  }

  private RadioButton getFirstShooter() {
    return (RadioButton) shooterPanel.getWidget(0);
  }

  private void addShooter(RadioButton shooter) {
    shooterPanel.add(shooter);
    shooters.put(shooter.getText().trim(), shooter);
  }

  private void removeShooter(RadioButton shooter) {
    shooterPanel.remove(shooter);
    shooters.remove(shooter.getText().trim());
  }

  private boolean containsShooter(String name) {
    for (String key : shooters.keySet()) {
      if (key.trim().equalsIgnoreCase(name))
        return true;
    }
    return false;
  }

  private RadioButton getSelectedShooterRadioButton() {
    for (RadioButton shooter : shooters.values()) {
      if (shooter.getValue())
        return shooter;
    }
    throw new IllegalStateException("There is no shooter selected!");
  }

  private RadioButton buildShooterRadioButton(String name) {
    RadioButton newShooter = new RadioButton("shooters", name);
    newShooter.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        onSelectedShooterClicked();
      }
    });
    return newShooter;
  }

  void onSelectedShooterClicked() {
    enableEditButton();
    enableDeleteButton();
    showMessagePanel();
  }

  private String askNewShooterName() {
    String newName = Window.prompt("Please enter the new shooter's name.", "My Shooter");
    while (containsShooter(newName)) {
      Window.alert("'" + newName + "' already exists, please choose a different name.");
      newName = askNewShooterName();
    }
    return newName;
  }
}