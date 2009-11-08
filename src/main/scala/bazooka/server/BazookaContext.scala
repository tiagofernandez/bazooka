package bazooka.server

import bazooka.server.persistence._
import com.google.inject._
import com.wideplay.warp.persist._

class BazookaContext {

  val injector: Injector = initInjector()

  private def initInjector(): Injector = {
    Guice.createInjector(getPersistenceModule(), PersistenceService
      .usingJpa()
      .across(UnitOfWork.TRANSACTION)
      .buildModule())
  }

  protected def getPersistenceModule(): PersistenceModule = {
    new PersistenceModule()
  }

  def getInjector(): Injector = {
    injector
  }
}