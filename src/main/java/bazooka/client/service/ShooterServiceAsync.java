package bazooka.client.service;

import bazooka.client.data.ShooterData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ShooterServiceAsync {

  void createShooter(String name, AsyncCallback<ShooterData> async);

  void deleteShooter(ShooterData shooter, AsyncCallback<Boolean> async);
}