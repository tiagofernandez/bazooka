package bazooka.server.data

import javax.persistence._
import org.apache.commons.lang.builder._

@Entity{val name = "Request"}
class RequestData(requestName: String) {

  @Id
  @GeneratedValue
  var id: java.lang.Integer = null

  @Column{val unique = true}
  var name: String = requestName

  @Lob
  var payload: String = null

  def this() = this(null)

  override def equals(obj: Any): Boolean = {
    val other = obj.asInstanceOf[RequestData]
    new EqualsBuilder()
      .append(this.id, other.id)
      .isEquals()
  }

  override def hashCode(): Int = {
    new HashCodeBuilder()
      .append(id)
      .toHashCode
  }

  override def toString(): String = name
}