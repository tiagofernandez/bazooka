package bazooka.server.persistence

import com.google.inject._
import com.wideplay.warp.persist._

@Singleton
class PersistenceInitializer @Inject() (service: PersistenceService) {

  service.start()

}