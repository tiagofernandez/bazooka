package bazooka.server.context

import bazooka.server.persistence._
import bazooka.server.servlet._

import com.google.inject._

import com.wideplay.warp.persist._

class BazookaContext(persistenceModule: PersistenceModule) {

  val injector = Guice.createInjector(
    new BazookaServletModule,
    persistenceModule,
    PersistenceService.usingJpa.across(UnitOfWork.TRANSACTION).buildModule)

  def this() = this(new PersistenceModule("bazooka"))

  def getInjector() = injector
}