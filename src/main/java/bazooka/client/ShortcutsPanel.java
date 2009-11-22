package bazooka.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LayoutComposite;
import com.google.gwt.user.client.ui.StackLayoutPanel;

public class ShortcutsPanel extends LayoutComposite {

  interface Binder extends UiBinder<StackLayoutPanel, ShortcutsPanel> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField ShooterPanel shooterPanel;
  @UiField ConfigurationPanel configurationPanel;

  ShortcutsPanel() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override protected void onLoad() {
    shooterPanel.setConfiguration(configurationPanel);
  }

  void setContentPanel(ContentPanel contentPanel) {
    shooterPanel.setContent(contentPanel);
  }

  ShooterPanel getShooterPanel() {
    return shooterPanel;
  }
}