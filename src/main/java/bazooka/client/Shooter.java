package bazooka.client;

import bazooka.client.exception.ExistingShooterException;
import bazooka.client.service.ShooterService;
import bazooka.client.service.ShooterServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.HashMap;
import java.util.Map;

public class Shooter extends Composite {

  interface Binder extends UiBinder<Widget, Shooter> {}

  private static final Binder binder = GWT.create(Binder.class);

  private ShooterServiceAsync shooterService = GWT.create(ShooterService.class);

  @UiField Button newButton;
  @UiField Button editButton;
  @UiField Button deleteButton;
  @UiField VerticalPanel shooterPanel;

  Map<String, RadioButton> shooters = new HashMap<String, RadioButton>();

  Configuration configuration;
  Content content;

  Shooter() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override protected void onLoad() {
    populateShooters();
    selectFirstShooter();
  }

  @UiHandler("newButton")
  void onNewButtonClicked(ClickEvent event) {
    createShooter(askNewShooterName());
  }

  private void createShooter(String name) {
    AsyncCallback<String> callback = new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        if (caught instanceof ExistingShooterException)
          Window.alert("This name is already taken.");
      }
      public void onSuccess(String name) {
        RadioButton newShooter = buildShooterRadioButton(name);
        newShooter.setValue(true);
        addShooter(newShooter);
        enableDeleteButton();
        onEditButtonClicked(null);
      }
    };
    shooterService.createShooter(name, callback);
  }

  @UiHandler("editButton")
  void onEditButtonClicked(ClickEvent event) {
    disableEditButton();
    showScriptPanel();
  }

  @UiHandler("deleteButton")
  void onDeleteButtonClicked(ClickEvent event) {
    RadioButton selectedShooter = getSelectedShooter();
    if (Window.confirm("Are you sure you want to delete '" + selectedShooter.getText()  + "'?")) {
      removeShooter(selectedShooter);
      selectFirstShooter();
    }
  }

  void populateShooters() {
    addShooter(buildShooterRadioButton("Hurl"));
    addShooter(buildShooterRadioButton("AK-47"));
  }

  void selectFirstShooter() {
    if (shooterPanel.getWidgetCount() > 0) {
      RadioButton firstShooter = (RadioButton) shooterPanel.getWidget(0);
      firstShooter.setValue(true);
    }
  }

  void showScriptPanel() {
    content.messagePanel.setVisible(false);
    content.scriptPanel.setVisible(true);
  }

  void showMessagePanel() {
    content.scriptPanel.setVisible(false);
    content.messagePanel.setVisible(true);
  }

  void addShooter(RadioButton shooter) {
    shooterPanel.add(shooter);
    shooters.put(shooter.getText().trim(), shooter);
  }

  void removeShooter(RadioButton shooter) {
    shooterPanel.remove(shooter);
    shooters.remove(shooter.getText().trim());
  }

  boolean containsShooter(String name) {
    for (String key : shooters.keySet()) {
      if (key.trim().equalsIgnoreCase(name))
        return true;
    }
    return false;
  }

  RadioButton getSelectedShooter() {
    for (RadioButton shooter : shooters.values()) {
      if (shooter.getValue())
        return shooter;
    }
    throw new IllegalStateException("There is no shooter selected");
  }

  void enableDeleteButton() {
    deleteButton.setEnabled(true);
  }

  void disableDeleteButton() {
    deleteButton.setEnabled(false);
  }

  void enableEditButton() {
    editButton.setEnabled(true);
  }

  void disableEditButton() {
    editButton.setEnabled(false);
  }

  RadioButton buildShooterRadioButton(String name) {
    RadioButton newShooter = new RadioButton("shooters", name);
    newShooter.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        enableEditButton();
        enableDeleteButton();
        showMessagePanel();
      }
    });
    return newShooter;
  }

  String askNewShooterName() {
    String newName = Window.prompt("Please enter the new shooter's name.", "My Shooter");
    while (containsShooter(newName)) {
      Window.alert("'" + newName + "' already exists, please choose a different name.");
      newName = askNewShooterName();
    }
    return newName;
  }
}