package bazooka.server.service

import bazooka.common.exception._
import bazooka.common.service._
import bazooka.server.data._

import com.google.inject._
import com.wideplay.warp.persist._
import javax.persistence._

class ShooterServiceImpl extends ShooterService {

  var repo: ShooterRepository = _

  @Inject
  def initRepositories(em: Provider[EntityManager]) {
    repo = new ShooterRepository(em)
  }

  @Transactional
  def saveShooter(name: String) {
    if (repo.shooterExists(name))
      throw new ExistingShooterException

    repo.persist(new ShooterData(name))
  }

  @Transactional
  def deleteShooter(name: String) {
    assumeShooterExists(name)

    val shooter = repo.getShooterByName(name)
    repo.remove(shooter)
  }

  def listShooters() = {
    repo.listShooters
  }

  @Transactional
  def saveScript(script: String, shooter: String) {
    assumeShooterExists(shooter)

    val data = repo.getShooterByName(shooter)
    data.script = script

    repo.merge(data)
  }

  def getScript(shooter: String) = {
    repo.getScript(shooter)
  }

  private def assumeShooterExists(shooter: String) {
    if (!repo.shooterExists(shooter))
      throw new NonExistingShooterException
  }
}