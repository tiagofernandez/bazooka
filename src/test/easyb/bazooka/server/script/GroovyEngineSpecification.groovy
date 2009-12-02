package bazooka.server.script

it "should evaluate a trivial script", {
  GroovyEngine.eval(new GroovyScript("OnePlusOne", "1 + 1"))
}

it "should compile and eval a trivial script", {
  GroovyEngine.compileAndEval(new GroovyScript("TwoPlusTwo", "2 + 2"))
}

it "should not perform evaluation when invalid scripts are provided", {
  ensureThrows(IllegalArgumentException) { GroovyEngine.eval(null) }
}

it "should not reuse bindings", {
  times2Script = new GroovyScript("Times2", "number * 2")
  times2Script.parameters.putAll(["number": 2 as Object])

  GroovyEngine.eval(times2Script).shouldBe 4

  ensureThrows(GroovyEngineException) {
    times2Script.parameters.clear()
    GroovyEngine.eval(times2Script)
  }
}

it "should not evaluate a broken script", {
  ensureThrows(GroovyEngineException) { GroovyEngine.eval(new GroovyScript("broken", "<>")) }
}

it "should not compile a broken script", {
  ensureThrows(GroovyEngineException) { GroovyEngine.compile(new GroovyScript("broken", "<>")) }
}