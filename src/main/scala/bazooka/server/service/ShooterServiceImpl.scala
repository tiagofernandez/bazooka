package bazooka.server.service

import bazooka.client.exception._
import bazooka.client.data._
import bazooka.client.service._

import com.google.gwt.user.server.rpc._
import com.google.inject._
import com.wideplay.warp.persist._

import javax.persistence._

class ShooterServiceImpl extends RemoteServiceServlet with ShooterService {

  @Inject var em: Provider[EntityManager] = _

  @Transactional
  def createShooter(name: String): ShooterData = {
    if ("oops".equalsIgnoreCase(name))
      throw new ExistingShooterException()

    //em.get().persist...

    new ShooterData(name)
  }
}