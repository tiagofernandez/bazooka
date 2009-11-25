package bazooka.client;

import bazooka.common.exception.ExistingShooterException;
import bazooka.common.model.Shooter;
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

  private final ShooterServiceAsync shooterService = GWT.create(ShooterService.class);

  private final Map<Shooter, RadioButton> shooters = new HashMap<Shooter, RadioButton>();

  private ConfigurationPanel configurationPanel;
  private ContentPanel contentPanel;

  ShooterPanel() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override protected void onLoad() {
    populateShooterList();
    selectFirstShooter();
  }

  @UiHandler("newButton")
  void onNewButtonClicked(ClickEvent event) {
    String newShooterName = askNewShooterName();
    if (newShooterName != null)
      createNewShooter(newShooterName.trim(), "");
  }

  @UiHandler("editButton")
  void onEditButtonClicked(ClickEvent event) {
    contentPanel.setScript(getSelectedShooter().getScript());
    disableEditButton();
    enableDeleteButton();
    showScriptPanel();
  }

  @UiHandler("deleteButton")
  void onDeleteButtonClicked(ClickEvent event) {
    Shooter shooter = getSelectedShooter();
    if (Window.confirm("Are you sure you want to delete '" + shooter + "'?")) {
      deleteExistingShooter(shooter, getSelectedShooterRadioButton());
    }
  }

  void createNewShooter(final String name, final String script) {
    final Shooter shooter = new Shooter(name, script);

    shooterService.saveShooter(shooter, new AsyncCallback<Shooter>() {
      public void onFailure(Throwable caught) {
        if (caught instanceof ExistingShooterException)
          Window.alert("This name is already taken.");
        else
          Window.alert("Error while creating shooter: " + caught.getMessage());
      }
      public void onSuccess(Shooter shooter) {
        RadioButton shooterButton = buildShooterRadioButton(name);
        shooterButton.setValue(true);
        addShooter(shooter, shooterButton);
        onEditButtonClicked(null);
      }
    });
  }

  void deleteExistingShooter(final Shooter shooter, final RadioButton shooterButton) {
    shooterService.deleteShooter(shooter, new AsyncCallback<Void>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while deleting shooter: " + caught.getMessage());
      }
      public void onSuccess(Void success) {
        removeShooter(shooter, shooterButton);
        selectFirstShooter();
        showMessagePanel();
      }
    });
  }

  void populateShooterList() {
    shooterService.listShooters(new AsyncCallback<List<Shooter>>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error while retrieving shooters: " + caught.getMessage());
      }
      public void onSuccess(List<Shooter> shooters) {
        for (Shooter shooter : shooters)
          addShooter(shooter, buildShooterRadioButton(shooter.getName()));
      }
    });
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

  private void addShooter(Shooter shooter, RadioButton shooterButton) {
    shooterPanel.add(shooterButton);
    shooters.put(shooter, shooterButton);
  }

  private void removeShooter(Shooter shooter, RadioButton button) {
    shooterPanel.remove(button);
    shooters.remove(shooter);
  }

  private boolean containsShooter(String name) {
    for (Shooter key : shooters.keySet()) {
      if (name.equalsIgnoreCase(key.getName()))
        return true;
    }
    return false;
  }

  boolean hasSelectedShooter() {
    for (RadioButton shooter : shooters.values()) {
      if (shooter.getValue())
        return true;
    }
    return false;
  }

  Shooter getSelectedShooter() {
    final String selectedShooter = getSelectedShooterName();

    for (Shooter shooter : shooters.keySet())
      if (selectedShooter.equals(shooter.getName()))
        return shooter;

    throw new IllegalStateException("There is no shooter selected!");
  }

  String getSelectedShooterName() {
    return getSelectedShooterRadioButton().getText();
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