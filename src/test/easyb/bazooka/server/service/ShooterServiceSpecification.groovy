package bazooka.server.service

import bazooka.client.data.ShooterData
import bazooka.client.exception.ExistingShooterException
import bazooka.client.exception.NonExistingShooterException
import bazooka.server.context.BazookaContext
import bazooka.server.persistence.PersistenceModule

service = new BazookaContext(new PersistenceModule("bazooka-test")).injector.getInstance(ShooterServiceImpl.class)

it "should create a new shooter", {
  def final name = "Hurl"
  def shooter = service.createShooter(name)

  shooter.name.shouldBe name
  shooter.id.shouldNotBe null
}

it "should not create shooters with the same name", {
  def final name = "Curl"
  service.createShooter(name)

  ensureThrows(ExistingShooterException) {
    service.createShooter(name)
	}
}

it "should delete an existing shooter", {
  def final name = "ToDelete"
  def final shooter = service.createShooter(name)
  service.deleteShooter(shooter).shouldBe true
}

it "should not delete non-existing shooter", {
  ensureThrows(NonExistingShooterException) {
    service.deleteShooter(new ShooterData("NonExisting"))
	}
}