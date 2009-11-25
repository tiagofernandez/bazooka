package bazooka.server.persistence

import bazooka.common.model._
import com.google.inject._
import javax.persistence._

class ConfigurationRepository(entityManager: Provider[EntityManager]) extends Repository {

  em = entityManager

  def getConfigurationByName(configuration: String) = {
    getSingleResult("from Configuration c where c.name=:name", Map("name" -> configuration))
      .asInstanceOf[Configuration]
  }

  def listConfigurations() = {
    getResultList("select c from Configuration c order by c.name")
      .asInstanceOf[java.util.List[Configuration]]
  }

  def configurationExists(configuration: String): Boolean = {
    try {
      getSingleResult("select c.id from Configuration c where c.name=:name", Map("name" -> configuration))
        .isInstanceOf[java.lang.Integer]
    }
    catch {
      case ex => false
    }
  }
}