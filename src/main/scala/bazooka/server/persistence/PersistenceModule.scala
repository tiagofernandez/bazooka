package bazooka.server.persistence

import com.google.inject._
import com.wideplay.warp.persist.jpa._

class PersistenceModule(val unitName: String) extends AbstractModule {

  override def configure() {
    bindConstant.annotatedWith(classOf[JpaUnit]).to(unitName)
    bind(classOf[PersistenceInitializer]).asEagerSingleton
  }
}