package bazooka.server.service

import bazooka.common.exception._
import bazooka.common.model._
import bazooka.common.service._
import bazooka.server.persistence._

import com.google.inject._
import com.wideplay.warp.persist._
import javax.persistence._
import java.lang.String

class ShooterServiceImpl extends ShooterService {

  var repo: ShooterRepository = _

  @Inject
  def initRepositories(em: Provider[EntityManager]) {
    repo = new ShooterRepository(em)
  }

  @Transactional
  def saveShooter(shooter: Shooter) = {
    assumeShooterNotExists(shooter.getName)
    repo.persist(shooter)
    shooter
  }

  @Transactional
  def updateShooter(shooter: Shooter) = {
    assumeShooterExists(shooter.getName)
    repo.merge(shooter)
    shooter
  }

  @Transactional
  def deleteShooter(shooter: Shooter) {
    val name = shooter.getName
    assumeShooterExists(name)
    repo.remove(repo.getShooterByName(name))
  }

  def getShooter(name: String) = {
    assumeShooterExists(name)
    repo.getShooterByName(name)
  }

  def listShooters() = {
    repo.listShooters
  }

  private def assumeShooterNotExists(name: String) {
    if (repo.shooterExists(name))
      throw new ExistingShooterException
  }

  private def assumeShooterExists(name: String) {
    if (!repo.shooterExists(name))
      throw new NonExistingShooterException
  }
}