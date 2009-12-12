package bazooka.server.script

it "should evaluate a trivial script", {
  GroovyEngine.eval(new GroovyScript("1 + 1"))
}

it "should not perform evaluation when invalid scripts are provided", {
  ensureThrows(IllegalArgumentException) { GroovyEngine.eval(null) }
}

it "should not reuse bindings", {
  times2Script = new GroovyScript("number * 2")
  times2Script.parameters.putAll(["number": 2 as Object])

  GroovyEngine.eval(times2Script).shouldBe 4

  ensureThrows(GroovyEngineException) {
    times2Script.parameters.clear()
    GroovyEngine.eval(times2Script)
  }
}

it "should not evaluate a broken script", {
  ensureThrows(GroovyEngineException) { GroovyEngine.eval(new GroovyScript("<>")) }
}