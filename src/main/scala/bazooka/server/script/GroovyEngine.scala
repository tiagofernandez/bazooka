package bazooka.server.script

import javax.script._
import java.util.concurrent._

class GroovyEngine(manager: ScriptEngineManager) {

  var engine: ScriptEngine = manager.getEngineByName("groovy")

  var timeoutInSeconds: Int = 0

  val compiledScripts = new ConcurrentHashMap[GroovyScript, CompiledScript]

  def compile(script: GroovyScript) {
    ensureScriptIsValid(script)

    try {
      val compiledScript = engine.asInstanceOf[Compilable].compile(script.code)
      compiledScripts.put(script, compiledScript)
    }
    catch {
      case ex: Throwable =>
        throw new GroovyEngineException(getErrorMessage("Error while compiling script", script, ex), ex)
    }
  }

  def compileAndEval(script: GroovyScript) = {
    compile(script)
    eval(script)
  }

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
        throw new GroovyEngineException(getErrorMessage("Error while evaluating script", script, ex), ex)
      }
    }
    finally {
      pool.shutdown
    }
  }

  private def createCallable(script: GroovyScript) = {
    val bindings = script.createBindings

    if (compiledScripts.containsKey(script))
      createCompilableCallable(compiledScripts.get(script), bindings)
    else
      createInterpretableCallable(script.code, bindings)
  }

  private def createCompilableCallable(script: CompiledScript, bindings: Bindings) = {
    new Callable[Object] {
      def call() = {
        setEngineBindings(bindings)
        script.eval()
      }
    }
  }

  private def createInterpretableCallable(script: String, bindings: Bindings) = {
    new Callable[Object]() {
      def call() = {
        setEngineBindings(bindings)
        engine.eval(script)
      }
    }
  }

  private def setEngineBindings(bindings: Bindings) {
    engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE)
  }

  private def ensureScriptIsValid(script: GroovyScript) {
    require(script != null, "The script must not be null")
    require(script.name != null, "The script name must not be null")
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