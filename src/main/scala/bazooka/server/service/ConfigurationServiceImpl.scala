package bazooka.server.service

import bazooka.common.exception._
import bazooka.common.model._
import bazooka.common.service._
import bazooka.server.persistence._

import com.google.inject._
import com.wideplay.warp.persist._

import javax.persistence._

import scala.collection.jcl.Conversions._

class ConfigurationServiceImpl extends ConfigurationService {

  var repo: ConfigurationRepository = _

  @Inject
  def initRepositories(em: Provider[EntityManager]) {
    repo = new ConfigurationRepository(em)
  }

  @Transactional
  def saveConfiguration(configuration: Configuration) = {
    assumeConfigurationNotExists(configuration.getName)
    repo.persist(configuration)
    new Configuration(configuration)
  }

  @Transactional
  def updateConfiguration(configuration: Configuration) = {
    assumeConfigurationExists(configuration.getName)
    repo.merge(configuration)
    new Configuration(configuration)
  }

  @Transactional
  def deleteConfiguration(configuration: Configuration) {
    val name = configuration.getName
    assumeConfigurationExists(name)
    repo.remove(repo.getConfigurationByName(name))
  }

  def getConfiguration(name: String) = {
    assumeConfigurationExists(name)
    new Configuration(repo.getConfigurationByName(name))
  }

  def listConfigurations() = {
    val configs = repo.listConfigurations
    convertList(configs).foreach(config => config.normalizeRelationships())
    configs
  }

  private def assumeConfigurationNotExists(name: String) {
    if (repo.configurationExists(name))
      throw new ExistingConfigurationException
  }

  private def assumeConfigurationExists(name: String) {
    if (!repo.configurationExists(name))
      throw new NonExistingConfigurationException
  }
}