package bazooka.server.service

import bazooka.common.exception.ExistingShooterException
import bazooka.common.exception.NonExistingShooterException
import bazooka.server.context.BazookaContext
import bazooka.server.persistence.PersistenceModule
import bazooka.common.model.*

service = new BazookaContext(new PersistenceModule("bazooka-test")).injector.getInstance(ShooterServiceImpl.class)

it "should save a new shooter", {
  service.saveShooter(new Shooter("Hurl")).id.shouldNotBe null
}

it "should not save shooters with the same name", {
  name = "Curl"
  service.saveShooter(new Shooter(name))

  ensureThrows(ExistingShooterException) {
    service.saveShooter(new Shooter(name))
	}
}

it "should update a script for an existing shooter", {
  shooter = service.saveShooter(new Shooter("Xyz", "while (true) {}"))

  newScript = "while (false) {}"
  shooter.script = newScript

  service.updateShooter(shooter).script.shouldBe newScript
}

it "should not save a script for a non-existing shooter", {
  ensureThrows(NonExistingShooterException) {
    service.updateShooter(new Shooter("Void", "println 'Some script'"))
  }
}

it "should save a shooter and get its script", {
  name = "AK-47"
  script = "println 'Anything you want'"

  service.saveShooter(new Shooter(name, script))
  service.getShooter(name).script.shouldBe script
}

it "should delete an existing shooter", {
  name = "AR-15"
  shooter = service.saveShooter(new Shooter(name))
  service.deleteShooter(shooter)

  ensureThrows(NonExistingShooterException) {
    service.getShooter(name)
	}
}

it "should not delete a shooter that does not exist", {
  ensureThrows(NonExistingShooterException) {
    service.deleteShooter(new Shooter("NonExisting"))
	}
}

it "should list the existing shooters", {
  service.saveShooter(new Shooter("Foo"))
  service.saveShooter(new Shooter("Bar"))
  service.listShooters().size().shouldBeGreaterThan 2
}

it "should take a successful shot", {
  shooter = new Shooter("Multiplier", """\
  result = first.toDouble() * second.toDouble()
  request + result
  """)

  request = new Request("Output Prefix", "Output: ")
  config = new Configuration("Numbers", [new Parameter("first", "2"), new Parameter("second", "4")])

  service.shoot(shooter, request, config).shouldBe "Output: 8.0"
}