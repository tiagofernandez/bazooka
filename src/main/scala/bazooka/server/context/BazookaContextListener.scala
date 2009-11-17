package bazooka.server.context

import com.google.inject._
import servlet.GuiceServletContextListener

class BazookaContextListener extends GuiceServletContextListener {

  override def getInjector(): Injector = {
    new BazookaContext().getInjector
  }
}