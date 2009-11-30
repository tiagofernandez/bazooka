package bazooka.server.script;

public class GroovyEngineException extends RuntimeException {

  public GroovyEngineException() {
    super();
  }

  public GroovyEngineException(Throwable cause) {
    super(cause.getMessage(), cause);
  }

  public GroovyEngineException(String message, Throwable cause) {
    super(message, cause);
  }
}