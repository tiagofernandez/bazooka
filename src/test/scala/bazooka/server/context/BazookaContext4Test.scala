package bazooka.server.context

import bazooka.server.persistence._

class BazookaContext4Test extends BazookaContext {

  override protected def getPersistenceModule(): PersistenceModule = {
    new PersistenceModule4Test
  }
}