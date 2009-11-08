package bazooka.client.service;

import bazooka.client.data.ShooterData;
import bazooka.client.exception.ExistingShooterException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("shooter")
public interface ShooterService extends RemoteService {

  ShooterData createShooter(String name) throws ExistingShooterException;

}