package bazooka.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class AboutDialog extends DialogBox {

  interface Binder extends UiBinder<Widget, AboutDialog> {}

  private static final Binder binder = GWT.create(Binder.class);

  @UiField Button closeButton;

  AboutDialog() {
    setText("Bazooka");
    setWidget(binder.createAndBindUi(this));
  }

  @Override protected void onPreviewNativeEvent(NativePreviewEvent preview) {
    super.onPreviewNativeEvent(preview);
    NativeEvent evt = preview.getNativeEvent();
    if (isEventTypeKeydown(evt.getType()))
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

  private boolean isEventTypeKeydown(String type) {
    return "keydown".equals(type);
  }
}