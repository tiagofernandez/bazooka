package bazooka.common.service;

import bazooka.common.exception.ExistingShooterException;
import bazooka.common.exception.NonExistingShooterException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("GWT.rpc")
public interface ShooterService extends RemoteService {

  void saveShooter(String name) throws ExistingShooterException;

  void deleteShooter(String name) throws NonExistingShooterException;

  List<String> listShooters();

  void saveScript(String script, String shooter);

  String getScript(String shooter);
}