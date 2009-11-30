package bazooka.common.service;

import bazooka.common.exception.*;
import bazooka.common.model.*;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("GWT.rpc")
public interface ShooterService extends RemoteService {

  Shooter saveShooter(Shooter shooter) throws ExistingShooterException;

  Shooter updateShooter(Shooter shooter) throws NonExistingShooterException;

  void deleteShooter(Shooter shooter) throws NonExistingShooterException;

  Shooter getShooter(String name) throws NonExistingShooterException;

  List<Shooter> listShooters();

  String shoot(Shooter shooter, Request request, Configuration config) throws ShootingException;
}