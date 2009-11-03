package bazooka.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ShooterServiceAsync {

  void createShooter(String name, AsyncCallback<String> async);
  
}
