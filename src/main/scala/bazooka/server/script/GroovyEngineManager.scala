package bazooka.server.script

import javax.script._

object GroovyEngineManager {

  val engine = new GroovyEngine(new ScriptEngineManager)

  def get() = engine
}