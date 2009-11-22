package bazooka.server.service

import bazooka.common.exception._
import bazooka.common.service._
import bazooka.server.data._

import com.google.inject._
import com.wideplay.warp.persist._

import javax.persistence._

class RequestServiceImpl extends RequestService {

  @Inject var em: Provider[EntityManager] = _

  @Transactional
  def saveRequest(name: String, payload: String) {
    if (requestExists(name))
      throw new ExistingRequestException

    em.get.persist(new RequestData(name))
  }

  @Transactional
  def deleteRequest(name: String) {
    assumeRequestExists(name)

    val request = getRequestByName(name)
    em.get.remove(request)
  }

  def listRequests() = {
    em.get
      .createQuery("select r.name from Request r order by r.name")
      .getResultList
      .asInstanceOf[java.util.List[String]]
  }

  @Transactional
  def savePayload(payload: String, request: String) {
    assumeRequestExists(request)

    val data = getRequestByName(request)
    data.payload = payload

    em.get.merge(data)
  }

  def getPayload(request: String) = {
    em.get
      .createQuery("select r.payload from Request r where r.name=:name")
      .setParameter("name", request)
      .getSingleResult
      .asInstanceOf[String]
  }

  private def assumeRequestExists(request: String) {
    if (!requestExists(request))
      throw new NonExistingRequestException
  }

  private def requestExists(request: String): Boolean = {
    try {
      em.get
        .createQuery("select r.id from Request r where r.name=:name")
        .setParameter("name", request)
        .getSingleResult
        .isInstanceOf[java.lang.Integer]
    }
    catch {
      case ex => false
    }
  }

  private def getRequestByName(request: String) = {
    em.get
      .createQuery("from Request r where r.name=:name")
      .setParameter("name", request)
      .getSingleResult
      .asInstanceOf[RequestData]
  }
}