package bazooka.server.data

import com.google.inject._
import javax.persistence._

class RequestRepository(entityManager: Provider[EntityManager]) extends Repository {

  em = entityManager
  
  def getRequestByName(request: String) = {
    getSingleResult("from Request r where r.name=:name", Map("name" -> request))
      .asInstanceOf[RequestData]
  }

  def getPayload(request: String) = {
    getSingleResult("select r.payload from Request r where r.name=:name", Map("name" -> request))
      .asInstanceOf[String]
  }

  def listRequests() = {
    getResultList("select r.name from Request r order by r.name")
      .asInstanceOf[java.util.List[String]]
  }

  def requestExists(request: String): Boolean = {
    try {
      getSingleResult("select r.id from Request r where r.name=:name", Map("name" -> request))
        .isInstanceOf[java.lang.Integer]
    }
    catch {
      case ex => false
    }
  }
}