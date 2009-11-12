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
    if (shooterExists(name))
      throw new ExistingShooterException

    val shooter = new ShooterData(name)
    em.get.persist(shooter)
    shooter
  }

  @Transactional
  def deleteShooter(shooter: ShooterData): java.lang.Boolean = {
    val shooterToDelete = getShooterById(shooter.getId)

    if (shooterToDelete == None)
      throw new NonExistingShooterException

    em.get.remove(shooterToDelete)
    !shooterExists(shooter.getName)
  }

  private def shooterExists(name: String): Boolean = {
    getShooter(name) != None
  }

  private def getShooter(name: String): Any = {
    try {
      em.get
        .createQuery("from Shooter s where s.name=:name")
        .setParameter("name", name)
        .getSingleResult
    }
    catch {
      case ex => None
    }
  }

  private def getShooterById(id: java.lang.Integer): Any = {
    if (id != null)
      em.get.find(classOf[ShooterData], id)
    else
      None
  }
}