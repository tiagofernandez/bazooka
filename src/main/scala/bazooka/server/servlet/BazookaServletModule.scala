package bazooka.server.servlet

import bazooka.common.service._
import bazooka.server.service._

import com.google.inject.servlet.ServletModule

class BazookaServletModule extends ServletModule {

  override protected def configureServlets() {
    serve("/bazooka/GWT.rpc").`with`(classOf[BazookaRemoteServiceServlet])

    // cannot use @ImplementedBy
    bind(classOf[ShooterService]).to(classOf[ShooterServiceImpl])
    bind(classOf[RequestService]).to(classOf[RequestServiceImpl])
    bind(classOf[ConfigurationService]).to(classOf[ConfigurationServiceImpl])
  }
}