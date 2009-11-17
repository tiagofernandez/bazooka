package bazooka.server.context

import javax.servlet._

class BazookaContextListener extends ServletContextListener {

  var context: BazookaContext = _

	def contextInitialized(event: ServletContextEvent) { context = new BazookaContext }

	def contextDestroyed(event: ServletContextEvent) { context = null }
}