package bazooka.server.persistence

import com.google.inject._
import javax.persistence._

trait Repository {

  var em: Provider[EntityManager] = _

  def persist(entity: Any) {
    em.get.persist(entity)
  }

  def merge(entity: Any) {
    em.get.merge(entity)
  }

  def remove(entity: Any) {
    em.get.remove(entity)
  }

  def getResultList(query: String) = {
    em.get
      .createQuery(query)
      .getResultList
  }

  def getSingleResult(query: String, params: Map[String, Any]) = {
    val queryToRun = em.get.createQuery(query)

    //for ((key, value) <- params)
    params.foreach { case (key, value) =>
      queryToRun.setParameter(key, value)
    }
    queryToRun.getSingleResult
  }
}