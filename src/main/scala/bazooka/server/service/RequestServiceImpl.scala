package bazooka.server.service

import bazooka.common.exception._
import bazooka.common.service._
import bazooka.server.data._

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
  def saveRequest(request: String, payload: String) {
    assumeRequestNotExists(request)

    val data: RequestData = new RequestData(request)
    data.payload = payload

    repo.persist(data)
  }

  @Transactional
  def updateRequest(request: String, payload: String) {
    assumeRequestExists(request)

    val data = repo.getRequestByName(request)
    data.payload = payload

    repo.merge(data)
  }

  @Transactional
  def deleteRequest(request: String) {
    assumeRequestExists(request)

    val data = repo.getRequestByName(request)
    repo.remove(data)
  }

  def listRequests() = {
    repo.listRequests
  }

  def getPayload(request: String) = {
    assumeRequestExists(request)
    repo.getPayload(request)
  }

  private def assumeRequestNotExists(request: String) {
    if (repo.requestExists(request))
      throw new ExistingRequestException
  }

  private def assumeRequestExists(request: String) {
    if (!repo.requestExists(request))
      throw new NonExistingRequestException
  }
}