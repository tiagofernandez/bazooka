package bazooka.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LayoutComposite;
import com.google.gwt.user.client.ui.StackLayoutPanel;

public class Shortcuts extends LayoutComposite {

  interface Binder extends UiBinder<StackLayoutPanel, Shortcuts> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField Shooter shooter;
  @UiField Configuration configuration;

  Shortcuts() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override protected void onLoad() {
    shooter.configuration = configuration;
  }
}