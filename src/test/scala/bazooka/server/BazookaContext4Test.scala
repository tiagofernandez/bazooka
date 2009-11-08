package bazooka.server

import persistence._

class BazookaContext4Test extends BazookaContext {

  override protected def getPersistenceModule(): PersistenceModule = {
    new PersistenceModule4Test()
  }
}