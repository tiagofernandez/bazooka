package bazooka.server.service;

import bazooka.client.exception.ExistingShooterException;
import bazooka.client.service.ShooterService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ShooterServiceImpl extends RemoteServiceServlet implements ShooterService {

  public String createShooter(String name) throws ExistingShooterException {
    if ("oops".equalsIgnoreCase(name))
      throw new ExistingShooterException();

    return name;
  }
}