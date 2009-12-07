package bazooka.server.service

import bazooka.common.exception._
import bazooka.common.model._
import bazooka.common.service._
import bazooka.server.persistence._
import bazooka.server.script._

import com.google.inject._
import com.wideplay.warp.persist._

import javax.persistence._

import scala.collection.jcl.Conversions._

class ShooterServiceImpl extends ShooterService {

  var shooterRepo: ShooterRepository = _
  var requestRepo: RequestRepository = _

  @Inject
  def initRepositories(em: Provider[EntityManager]) {
    shooterRepo = new ShooterRepository(em)
    requestRepo = new RequestRepository(em)
  }

  @Transactional
  def saveShooter(shooter: Shooter) = {
    assumeShooterNotExists(shooter.getName)
    shooterRepo.persist(shooter)
    shooter
  }

  @Transactional
  def updateShooter(shooter: Shooter) = {
    assumeShooterExists(shooter.getName)
    shooterRepo.merge(shooter)
    shooter
  }

  @Transactional
  def deleteShooter(shooter: Shooter) {
    val name = shooter.getName
    assumeShooterExists(name)
    shooterRepo.remove(shooterRepo.getShooterByName(name))
  }

  def getShooter(name: String) = {
    assumeShooterExists(name)
    shooterRepo.getShooterByName(name)
  }

  def listShooters() = {
    shooterRepo.listShooters
  }

  def shoot(shooter: Shooter, request: Request, config: Configuration) = {
    val script = new GroovyScript(shooter.getName + " : " + request.getName, shooter.getScript)

    script.parameters.put("request", request.getPayload)
    script.parameters.put("requests", requestRepo.listRequests)
    script.parameters.put("parameters", config.getParametersMap)

    if (config.hasParameters())
      convertList(config.getParameters).foreach ( param =>
        script.parameters.put(param.getKey, param.getValue))

    try {
      GroovyEngine.compileAndEval(script).toString
    }
    catch {
      case ex => throw new ShootingException(ex.getMessage, ex)
    }
  }

  private def assumeShooterNotExists(name: String) {
    if (shooterRepo.shooterExists(name))
      throw new ExistingShooterException
  }

  private def assumeShooterExists(name: String) {
    if (!shooterRepo.shooterExists(name))
      throw new NonExistingShooterException
  }
}