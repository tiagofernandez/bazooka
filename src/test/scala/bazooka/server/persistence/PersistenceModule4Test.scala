package bazooka.server.persistence

class PersistenceModule4Test extends PersistenceModule {

  override protected def getPersistenceUnitName(): String = {
    "bazooka-test"
  }
}