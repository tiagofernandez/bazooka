package bazooka.server.script

import org.apache.commons.lang.builder._

import javax.script._
import java.util._

class GroovyScript(scriptCode: String) {

  var code: String = scriptCode

  val parameters: Map[String, Object] = new HashMap[String, Object]

  def createBindings() = {
    new SimpleBindings(new HashMap[String, Object](parameters))
  }

  override def equals(obj: Any) = {
    if (obj.isInstanceOf[GroovyScript]) {
      val other = obj.asInstanceOf[GroovyScript]
      new EqualsBuilder()
        .append(this.code, other.code)
        .isEquals
    }
    else
      false
  }

  override def hashCode() =
    new HashCodeBuilder()
      .append(code)
      .toHashCode

  override def toString() = code
}