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
  def saveRequest(name: String, payload: String) {
    if (repo.requestExists(name))
      throw new ExistingRequestException

    val request = new RequestData(name)
    request.payload = payload
    
    repo.persist(request)
  }

  @Transactional
  def deleteRequest(name: String) {
    assumeRequestExists(name)

    val request = repo.getRequestByName(name)
    repo.remove(request)
  }

  def listRequests() = {
    repo.listRequests
  }

  @Transactional
  def savePayload(payload: String, request: String) {
    assumeRequestExists(request)

    val data = repo.getRequestByName(request)
    data.payload = payload

    repo.merge(data)
  }

  def getPayload(request: String) = {
    repo.getPayload(request)
  }

  private def assumeRequestExists(request: String) {
    if (!repo.requestExists(request))
      throw new NonExistingRequestException
  }
}