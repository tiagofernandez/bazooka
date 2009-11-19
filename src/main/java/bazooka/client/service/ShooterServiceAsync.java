package bazooka.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface ShooterServiceAsync {

  void saveShooter(String name, AsyncCallback<Void> async);

  void deleteShooter(String name, AsyncCallback<Void> async);

  void listShooters(AsyncCallback<List<String>> async);

  void saveScript(String script, String shooter, AsyncCallback<Void> async);

  void getShooterScript(String shooter, AsyncCallback<String> async);
}