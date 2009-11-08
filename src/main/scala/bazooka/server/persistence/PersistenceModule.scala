package bazooka.server.persistence

import com.google.inject._
import com.wideplay.warp.persist.jpa._

class PersistenceModule extends AbstractModule {

  @Override def configure() = {
    bindConstant().annotatedWith(classOf[JpaUnit]).to(getPersistenceUnitName())
    bind(classOf[PersistenceInitializer]).asEagerSingleton()
  }

  protected def getPersistenceUnitName(): String = {
    "bazooka"
  }
}