package bazooka.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

public class TopPanel extends Composite {

  interface Binder extends UiBinder<Widget, TopPanel> {}
  
  private static final Binder binder = GWT.create(Binder.class);

  @UiField Anchor aboutLink;

  TopPanel() {
    initWidget(binder.createAndBindUi(this));
  }

  @UiHandler("aboutLink")
  void onAboutClicked(ClickEvent event) {
    AboutDialog dlg = new AboutDialog();
    dlg.show();
    dlg.center();
  }
}