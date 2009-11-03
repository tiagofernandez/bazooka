package bazooka.client.service;

import bazooka.client.exception.ExistingShooterException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("shooter")
public interface ShooterService extends RemoteService {

  String createShooter(String name) throws ExistingShooterException;

}