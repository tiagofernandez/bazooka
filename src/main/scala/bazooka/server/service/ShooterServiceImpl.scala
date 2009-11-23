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
  def saveShooter(shooter: String, script: String) {
    assumeShooterNotExists(shooter)

    val data: ShooterData = new ShooterData(shooter)
    data.script = script

    repo.persist(data)
  }

  @Transactional
  def updateShooter(shooter: String, script: String) {
    assumeShooterExists(shooter)
    
    val data = repo.getShooterByName(shooter)
    data.script = script

    repo.merge(data)
  }

  @Transactional
  def deleteShooter(shooter: String) {
    assumeShooterExists(shooter)

    val data = repo.getShooterByName(shooter)
    repo.remove(data)
  }

  def listShooters() = {
    repo.listShooters
  }

  def getScript(shooter: String) = {
    assumeShooterExists(shooter)
    repo.getScript(shooter)
  }

  private def assumeShooterNotExists(shooter: String) {
    if (repo.shooterExists(shooter))
      throw new ExistingShooterException
  }

  private def assumeShooterExists(shooter: String) {
    if (!repo.shooterExists(shooter))
      throw new NonExistingShooterException
  }
}