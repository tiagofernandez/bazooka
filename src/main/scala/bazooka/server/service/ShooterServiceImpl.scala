package bazooka.server.service

import bazooka.client.exception._
import bazooka.client.data._
import bazooka.client.service._

import com.google.inject._
import com.wideplay.warp.persist._

import javax.persistence._

class ShooterServiceImpl extends ShooterService {

  @Inject var em: Provider[EntityManager] = _

  @Transactional
  def createShooter(name: String) = {
    if (shooterExists(name))
      throw new ExistingShooterException

    val shooter = new ShooterData(name)
    em.get.persist(shooter)
    shooter
  }

  @Transactional
  def deleteShooter(shooter: ShooterData) = {
    val shooterToDelete = getShooterById(shooter.getId)

    if (shooterToDelete == None)
      throw new NonExistingShooterException

    em.get.remove(shooterToDelete)
    !shooterExists(shooter.getName)
  }

  private def shooterExists(name: String) = {
    getShooter(name) != None
  }

  private def getShooter(name: String) = {
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

  private def getShooterById(id: java.lang.Integer) = {
    if (id != null)
      em.get.find(classOf[ShooterData], id)
    else
      None
  }
}