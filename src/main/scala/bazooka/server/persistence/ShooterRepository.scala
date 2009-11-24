package bazooka.server.persistence

import bazooka.common.model._
import com.google.inject._
import javax.persistence._

class ShooterRepository(entityManager: Provider[EntityManager]) extends Repository {

  em = entityManager

  def getShooterByName(shooter: String) = {
    getSingleResult("from Shooter s where s.name=:name", Map("name" -> shooter))
      .asInstanceOf[Shooter]
  }

  def getScript(shooter: String) = {
    getSingleResult("select s.script from Shooter s where s.name=:name", Map("name" -> shooter))
      .asInstanceOf[String]
  }

  def listShooters() = {
    getResultList("select s from Shooter s order by s.name")
      .asInstanceOf[java.util.List[Shooter]]
  }

  def shooterExists(shooter: String): Boolean = {
    try {
      getSingleResult("select s.id from Shooter s where s.name=:name", Map("name" -> shooter))
        .isInstanceOf[java.lang.Integer]
    }
    catch {
      case ex => false
    }
  }
}