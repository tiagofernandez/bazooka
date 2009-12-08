package bazooka.server.script

import javax.script._
import java.util.concurrent._

object GroovyEngine {

  var engine: ScriptEngine = new ScriptEngineManager().getEngineByName("groovy")

  var timeoutInSeconds: Int = 15

  val compiledScripts = new ConcurrentHashMap[GroovyScript, CompiledScript]

  def compile(script: GroovyScript) {
    ensureScriptIsValid(script)

    try {
      if (!isScriptCompiled(script)) {
        val compiledScript = engine.asInstanceOf[Compilable].compile(script.code)
        compiledScripts.put(script, compiledScript)
      }
    }
    catch {
      case ex: Throwable => {
        val errorMessage = getErrorMessage("Error while compiling script", script, ex)
        throw new GroovyEngineException(errorMessage, ex)
      }
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
        val errorMessage = getErrorMessage("Error while evaluating script", script, ex)
        throw new GroovyEngineException(errorMessage, ex)
      }
    }
    finally {
      pool.shutdown
    }
  }

  private def isScriptCompiled(script: GroovyScript) = compiledScripts.containsKey(script)

  private def isScriptNotCompiled(script: GroovyScript) = !isScriptCompiled(script)

  private def createCallable(script: GroovyScript) = {
    val bindings = script.createBindings

    if (isScriptCompiled(script))
      createCompilableCallable(compiledScripts.get(script), script)
    else
      createInterpretableCallable(script)
  }

  private def createCompilableCallable(compiledScript: CompiledScript, script: GroovyScript) = {
    new Callable[Object] {
      def call() = {
        setContext(script)
        compiledScript.eval()
      }
    }
  }

  private def createInterpretableCallable(script: GroovyScript) = {
    new Callable[Object]() {
      def call() = {
        setContext(script)
        engine.eval(script.code)
      }
    }
  }

  private def setContext(script: GroovyScript) {
    val context = new SimpleScriptContext
    context.setBindings(script.createBindings, ScriptContext.ENGINE_SCOPE)

    engine.setContext(context)
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