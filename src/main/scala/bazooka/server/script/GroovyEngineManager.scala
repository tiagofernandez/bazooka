package bazooka.server.script

import javax.script._

object GroovyEngineManager {

  val engine = new GroovyEngine(new ScriptEngineManager, 30)

  def get() = engine
}