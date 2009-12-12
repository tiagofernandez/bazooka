package bazooka.server.script

import javax.script._
import java.util.concurrent._

object GroovyEngine {

  val timeoutInSeconds: Int = 15

  def eval(script: GroovyScript) = {
    ensureScriptIsValid(script)

    val pool = Executors.newSingleThreadExecutor
    val task = pool.submit(createCallable(script))

    try {
      if (timeoutInSeconds > 0)
        task.get(timeoutInSeconds, TimeUnit.SECONDS)
      else
        task.get
    }
    catch {
      case ex: Throwable => {
        task.cancel(true)
        
        val errorMessage = getErrorMessage("Error while evaluating script", script, ex)
        throw new GroovyEngineException(errorMessage, ex)
      }
    }
    finally {
      pool.shutdown
    }
  }

  private def createCallable(script: GroovyScript) = {
    new Callable[Object]() {
      def call() = {
        val engine = new ScriptEngineManager().getEngineByName("groovy")
        engine.setBindings(script.createBindings, ScriptContext.ENGINE_SCOPE)
        engine.eval(script.code)
      }
    }
  }

  private def ensureScriptIsValid(script: GroovyScript) {
    require(script != null, "The script must not be null")
    require(script.code != null, "The script code must not be null")
  }

  private def getErrorMessage(message: String, script: GroovyScript, ex: Throwable) = {
    new StringBuilder(message)
      .append(" [script=").append(script)
      .append(", parameters=").append(script.parameters)
      .append(", class=").append(ex.getClass)
      .append(", message=").append(ex.getMessage)
      .append(", cause=").append(ex.getCause)
      .append("]").toString
  }
}