package bazooka.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.*;

public class AboutDialog extends DialogBox {

  interface Binder extends UiBinder<Widget, AboutDialog> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField Button closeButton;

  AboutDialog() {
    setText("Bazooka");
    setWidget(binder.createAndBindUi(this));
    setAnimationEnabled(true);
    setGlassEnabled(true);
  }

  @Override protected void onPreviewNativeEvent(NativePreviewEvent preview) {
    super.onPreviewNativeEvent(preview);
    NativeEvent evt = preview.getNativeEvent();
    
    if ("keydown".equals(evt.getType()))
      switch (evt.getKeyCode()) {
        case KeyCodes.KEY_ENTER:
        case KeyCodes.KEY_ESCAPE:
          onCloseButtonClicked(null);
          break;
      }
  }

  @UiHandler("closeButton")
  void onCloseButtonClicked(ClickEvent event) {
    hide();
  }
}