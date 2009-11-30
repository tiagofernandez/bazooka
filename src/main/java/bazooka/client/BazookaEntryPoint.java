package bazooka.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BazookaEntryPoint implements EntryPoint {

  interface Binder extends UiBinder<DockLayoutPanel, BazookaEntryPoint> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField TopPanel topPanel;
  @UiField ShortcutsPanel shortcutsPanel;
  @UiField ContentPanel contentPanel;

  public void onModuleLoad() {
    loadRootPanel();
    wireUiComponents();
  }

  private void loadRootPanel() {
    RootLayoutPanel root = RootLayoutPanel.get();
    root.add(binder.createAndBindUi(this));
    root.layout();
  }

  private void wireUiComponents() {
    shortcutsPanel.setContentPanel(contentPanel);
    contentPanel.setShooterPanel(shortcutsPanel.getShooterPanel());
    contentPanel.setConfigurationPanel(shortcutsPanel.getConfigurationPanel());
  }
}