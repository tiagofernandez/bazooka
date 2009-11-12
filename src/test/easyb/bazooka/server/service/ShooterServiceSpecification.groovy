package bazooka.server.service

import bazooka.server.context.*
import bazooka.client.data.*
import bazooka.client.exception.*

final ShooterServiceImpl svc = new BazookaContext4Test().injector.getInstance(ShooterServiceImpl.class)

it "should create a new shooter", {
  def final name = "Hurl"
  def shooter = svc.createShooter(name)

  shooter.name.shouldBe name
  shooter.id.shouldNotBe null
}

it "should not create shooters with the same name", {
  def final name = "Curl"
  svc.createShooter(name)

  ensureThrows(ExistingShooterException) {
    svc.createShooter(name)
	}
}

it "should delete an existing shooter", {
  def final name = "ToDelete"
  def final shooter = svc.createShooter(name)
  svc.deleteShooter(shooter).shouldBe true
}

it "should not delete non-existing shooter", {
  ensureThrows(NonExistingShooterException) {
    svc.deleteShooter(new ShooterData("NonExisting"))
	}
}