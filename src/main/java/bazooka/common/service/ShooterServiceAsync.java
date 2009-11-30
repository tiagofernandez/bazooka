package bazooka.common.service;

import bazooka.common.model.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface ShooterServiceAsync {

  void saveShooter(Shooter shooter, AsyncCallback<Shooter> async);

  void updateShooter(Shooter shooter, AsyncCallback<Shooter> async);

  void deleteShooter(Shooter shooter, AsyncCallback<Void> async);

  void getShooter(String name, AsyncCallback<Shooter> async);

  void listShooters(AsyncCallback<List<Shooter>> async);

  void shoot(Shooter shooter, Request request, Configuration config, AsyncCallback<String> async);
}