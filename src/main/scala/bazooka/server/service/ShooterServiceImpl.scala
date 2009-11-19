package bazooka.server.service

import bazooka.client.exception._
import bazooka.client.service._
import bazooka.server.data._

import com.google.inject._
import com.wideplay.warp.persist._

import javax.persistence._

class ShooterServiceImpl extends ShooterService {

  @Inject var em: Provider[EntityManager] = _

  @Transactional
  def saveShooter(shooter: String) {
    if (shooterExists(shooter))
      throw new ExistingShooterException

    em.get.persist(new ShooterData(shooter))
  }

  @Transactional
  def deleteShooter(shooter: String) {
    if (!shooterExists(shooter))
      throw new NonExistingShooterException

    val data = getShooterByName(shooter)
    em.get.remove(data)
  }

  def listShooters() = {
    em.get
      .createQuery("select s.name from Shooter s order by s.name")
      .getResultList
      .asInstanceOf[java.util.List[String]]
  }

  @Transactional
  def saveScript(script: String, shooter: String) {
    val data = getShooterByName(shooter)
    data.script = script

    em.get.merge(data)
  }

  def getShooterScript(shooter: String) = {
    em.get
      .createQuery("select s.script from Shooter s where s.name=:name")
      .setParameter("name", shooter)
      .getSingleResult
      .asInstanceOf[String]
  }

  private def shooterExists(name: String): Boolean = {
    try {
      em.get
        .createQuery("select s.id from Shooter s where s.name=:name")
        .setParameter("name", name)
        .getSingleResult
        .isInstanceOf[java.lang.Integer]
    }
    catch {
      case ex => false
    }
  }

  private def getShooterByName(name: String) = {
    try {
      em.get
        .createQuery("from Shooter s where s.name=:name")
        .setParameter("name", name)
        .getSingleResult
        .asInstanceOf[ShooterData]
    }
    catch {
      case ex => null
    }
  }
}