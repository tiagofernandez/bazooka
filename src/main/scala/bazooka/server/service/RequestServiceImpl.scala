package bazooka.server.service

import bazooka.common.exception._
import bazooka.common.service._
import bazooka.common.model._
import bazooka.server.persistence._

import com.google.inject._
import com.wideplay.warp.persist._
import javax.persistence._

class RequestServiceImpl extends RequestService {

  var repo: RequestRepository = _

  @Inject
  def initRepositories(em: Provider[EntityManager]) {
    repo = new RequestRepository(em)
  }

  @Transactional
  def saveRequest(request: Request) = {
    assumeRequestNotExists(request.getName)
    repo.persist(request)
    request
  }

  @Transactional
  def updateRequest(request: Request) = {
    assumeRequestExists(request.getName)
    repo.merge(request)
    request
  }

  @Transactional
  def deleteRequest(request: Request) {
    val name = request.getName
    assumeRequestExists(name)
    repo.remove(repo.getRequestByName(name))
  }

  def getRequest(name: String) = {
    assumeRequestExists(name)
    repo.getRequestByName(name)
  }

  def listRequests() = {
    repo.listRequests
  }

  private def assumeRequestNotExists(name: String) {
    if (repo.requestExists(name))
      throw new ExistingRequestException
  }

  private def assumeRequestExists(name: String) {
    if (!repo.requestExists(name))
      throw new NonExistingRequestException
  }
}