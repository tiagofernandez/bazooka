package bazooka.client.service;

import bazooka.client.data.ShooterData;
import bazooka.client.exception.ExistingShooterException;
import bazooka.client.exception.NonExistingShooterException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("GWT.rpc")
public interface ShooterService extends RemoteService {

  ShooterData createShooter(String name) throws ExistingShooterException;

  Boolean deleteShooter(ShooterData shooter) throws NonExistingShooterException;
}