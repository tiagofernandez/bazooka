package bazooka.server.service

import bazooka.common.exception.ExistingShooterException
import bazooka.common.exception.NonExistingShooterException
import bazooka.server.context.BazookaContext
import bazooka.server.persistence.PersistenceModule

service = new BazookaContext(new PersistenceModule("bazooka-test")).injector.getInstance(ShooterServiceImpl.class)

it "should save a new shooter", {
  final def shooter = "Hurl"
  service.saveShooter(shooter)
}

it "should not save shooters with the same name", {
  final def shooter = "Curl"
  service.saveShooter(shooter)

  ensureThrows(ExistingShooterException) {
    service.saveShooter(shooter)
	}
}

it "should delete an existing shooter", {
  final def shooter = "AR-15"
  service.saveShooter(shooter)
  service.deleteShooter(shooter)
}

it "should not delete a shooter that does not exist", {
  ensureThrows(NonExistingShooterException) {
    service.deleteShooter("NonExisting")
	}
}

it "should list the existing shooters", {
  service.saveShooter("Foo")
  service.saveShooter("Bar")
  service.listShooters().size().shouldBeGreaterThan 2
}

it "should save and get script", {
  final def shooter = "AK-47"
  final def script = "println 'Anything you want'"

  service.saveShooter(shooter)
  service.saveScript(script, shooter)
  service.getScript(shooter).shouldBe script
}

it "should not save a script for a non-existing shooter", {
  ensureThrows(NonExistingShooterException) {
    service.saveScript("Void", "println 'Some script'")
  }
}