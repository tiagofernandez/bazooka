package bazooka.server.script

import org.apache.commons.lang.builder._

import javax.script._
import java.util._

class GroovyScript(scriptName: String, scriptCode: String) {

  var name: String = scriptName
  var code: String = scriptCode

  val parameters: Map[String, Object] = new HashMap[String, Object]

  def this() = this(null, null)

  def createBindings() = {
    new SimpleBindings(new HashMap[String, Object](parameters))
  }

  override def equals(obj: Any) = {
    if (obj.isInstanceOf[GroovyScript]) {
      val other = obj.asInstanceOf[GroovyScript]
      new EqualsBuilder()
        .append(this.name, other.name)
        .append(this.code, other.code)
        .isEquals
    }
    else
      false
  }

  override def hashCode() =
    new HashCodeBuilder()
      .append(name)
      .append(code)
      .toHashCode

  override def toString() = name
}