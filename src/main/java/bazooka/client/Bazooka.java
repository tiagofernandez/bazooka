package bazooka.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Bazooka implements EntryPoint {

  interface Binder extends UiBinder<DockLayoutPanel, Bazooka> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField TopPanel topPanel;
  @UiField Shortcuts shortcuts;
  @UiField Content content;

  public void onModuleLoad() {
    loadRootPanel();
    injectDependencies();
  }

  void loadRootPanel() {
    RootLayoutPanel root = RootLayoutPanel.get();
    root.add(binder.createAndBindUi(this));
    root.layout();
  }

  void injectDependencies() {
    shortcuts.shooter.content = content;
    content.shooter = shortcuts.shooter;
  }
}