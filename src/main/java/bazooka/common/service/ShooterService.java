package bazooka.common.service;

import bazooka.common.exception.ExistingShooterException;
import bazooka.common.exception.NonExistingShooterException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("GWT.rpc")
public interface ShooterService extends RemoteService {

  void saveShooter(String request, String script) throws ExistingShooterException;

  void updateShooter(String request, String script) throws NonExistingShooterException;

  String getScript(String shooter) throws NonExistingShooterException;

  void deleteShooter(String request) throws NonExistingShooterException;

  List<String> listShooters();
}