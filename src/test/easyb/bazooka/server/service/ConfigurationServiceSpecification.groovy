package bazooka.server.service

import bazooka.common.exception.ExistingConfigurationException
import bazooka.common.exception.NonExistingConfigurationException
import bazooka.common.model.Configuration
import bazooka.common.model.Parameter
import bazooka.server.context.BazookaContext
import bazooka.server.persistence.PersistenceModule

service = new BazookaContext(new PersistenceModule("bazooka-test")).injector.getInstance(ConfigurationServiceImpl.class)

it "should save a new configuration", {
  service.saveConfiguration(new Configuration("My Config")).id.shouldNotBe null
}

it "should not save configurations with the same name", {
  name = "Another Config"
  service.saveConfiguration(new Configuration(name))

  ensureThrows(ExistingConfigurationException) {
    service.saveConfiguration(new Configuration(name))
	}
}

it "should update the parameters for an existing configuration", {
  configuration = service.saveConfiguration(new Configuration("Pqr"))
  parameters = [new Parameter("xyz1", "abc1"), new Parameter("xyz2", "abc2")]

  configuration.parameters = parameters

  service.updateConfiguration(configuration).parameters.each { parameter ->
    parameters.contains parameter
  }
}

it "should save a configuration and get its parameters", {
  configuration = new Configuration("Yet Another Config")
  parameters = [new Parameter("foo1", "bar1"), new Parameter("foo2", "bar2")]

  configuration.parameters = parameters

  service.saveConfiguration(configuration)
  service.getConfiguration(name).parameters.each { parameter ->
    parameters.contains parameter
  }
}

it "should delete an existing configuration", {
  name = "Special Config"
  configuration = service.saveConfiguration(new Configuration(name))
  service.deleteConfiguration(configuration)

  ensureThrows(NonExistingConfigurationException) {
    service.getConfiguration(name)
	}
}

it "should not delete a configuration that does not exist", {
  ensureThrows(NonExistingConfigurationException) {
    service.deleteConfiguration(new Configuration("NonExisting"))
	}
}

it "should list the existing configurations", {
  service.saveConfiguration(new Configuration("Foo"))
  service.saveConfiguration(new Configuration("Bar"))
  service.listConfigurations().size().shouldBeGreaterThan 2
}