package bazooka.server.persistence

import com.google.inject._
import com.wideplay.warp.persist.jpa._

class PersistenceModule(persistenceUnitName: String) extends AbstractModule {

  val unitName = persistenceUnitName

  override def configure() {
    bindConstant.annotatedWith(classOf[JpaUnit]).to(unitName)
    bind(classOf[PersistenceInitializer]).asEagerSingleton
  }
}