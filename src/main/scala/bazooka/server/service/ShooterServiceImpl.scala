package bazooka.server.service

import bazooka.common.exception._
import bazooka.common.service._
import bazooka.server.data._

import com.google.inject._
import com.wideplay.warp.persist._

import javax.persistence._

class ShooterServiceImpl extends ShooterService {

  @Inject var em: Provider[EntityManager] = _

  @Transactional
  def saveShooter(name: String) {
    if (shooterExists(name))
      throw new ExistingShooterException

    em.get.persist(new ShooterData(name))
  }

  @Transactional
  def deleteShooter(name: String) {
    assumeShooterExists(name)

    val shooter = getShooterByName(name)
    em.get.remove(shooter)
  }

  def listShooters() = {
    em.get
      .createQuery("select s.name from Shooter s order by s.name")
      .getResultList
      .asInstanceOf[java.util.List[String]]
  }

  @Transactional
  def saveScript(script: String, shooter: String) {
    assumeShooterExists(shooter)

    val data = getShooterByName(shooter)
    data.script = script

    em.get.merge(data)
  }

  def getScript(shooter: String) = {
    em.get
      .createQuery("select s.script from Shooter s where s.name=:name")
      .setParameter("name", shooter)
      .getSingleResult
      .asInstanceOf[String]
  }

  private def assumeShooterExists(shooter: String) {
    if (!shooterExists(shooter))
      throw new NonExistingShooterException
  }

  private def shooterExists(shooter: String): Boolean = {
    try {
      em.get
        .createQuery("select s.id from Shooter s where s.name=:name")
        .setParameter("name", shooter)
        .getSingleResult
        .isInstanceOf[java.lang.Integer]
    }
    catch {
      case ex => false
    }
  }

  private def getShooterByName(shooter: String) = {
    em.get
      .createQuery("from Shooter s where s.name=:name")
      .setParameter("name", shooter)
      .getSingleResult
      .asInstanceOf[ShooterData]
  }
}