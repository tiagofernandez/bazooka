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

it "should save a configuration and get its parameters", {
  configuration = new Configuration("Yet Another Config", [new Parameter("foo1", "bar1"), new Parameter("foo2", "bar2")])

  service.saveConfiguration(configuration)
  service.getConfiguration(configuration.name).parameters.each { param ->
    configuration.parameters.contains param
  }
}

it "should update the parameters of an existing configuration", {
  configuration = new Configuration("Pqr", [new Parameter("xyz1", "abc1"), new Parameter("xyz2", "abc2")])
  service.saveConfiguration(configuration)

  configuration.parameters << new Parameter("xyz3", "abc3")
  service.updateConfiguration(configuration)

  service.getConfiguration(configuration.name).parameters.each { param ->
    configuration.parameters.contains param
  }
}

it "should clone an existing configuration", {
  configuration = new Configuration("Original", [new Parameter("abc1", "xyz1"), new Parameter("abc2", "xyz2")])
  configToClone = new Configuration("Clone", configuration.parameters)

  service.saveConfiguration(configToClone).id.shouldNotBe(configuration.id)
}

it "should delete an existing configuration", {
  name = "Special Config"
  configuration = new Configuration(name)
  
  service.saveConfiguration(configuration)
  service.deleteConfiguration(configuration)

  ensureThrows(NonExistingConfigurationException) { service.getConfiguration(name) }
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