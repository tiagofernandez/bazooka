package bazooka.server.servlet

import bazooka.client.service._
import bazooka.server.service._

import com.google.inject.servlet.ServletModule

class BazookaServletModule extends ServletModule {

  override protected def configureServlets() {
    serve("/bazooka/GWT.rpc").`with`(classOf[BazookaRemoteServiceServlet])

    // cannot use @ImplementedBy
    bind(classOf[ShooterService]).to(classOf[ShooterServiceImpl])
  }
}