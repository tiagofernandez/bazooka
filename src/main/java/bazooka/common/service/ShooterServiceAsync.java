package bazooka.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface ShooterServiceAsync {

  void saveShooter(String request, String script, AsyncCallback<Void> async);

  void updateShooter(String request, String script, AsyncCallback<Void> async);

  void getScript(String shooter, AsyncCallback<String> async);

  void deleteShooter(String request, AsyncCallback<Void> async);

  void listShooters(AsyncCallback<List<String>> async);
}