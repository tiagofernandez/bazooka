package bazooka.server.data

import javax.persistence._
import org.apache.commons.lang.builder._

@Entity{val name = "Shooter"}
class ShooterData(shooterName: String) {
  
  @Id
  @GeneratedValue
  var id: java.lang.Integer = null

  @Column{val unique = true}
  var name: String = shooterName

  @Lob
  var script: String = null

  def this() = this("")

  override def equals(obj: Any): Boolean = {
    val other = obj.asInstanceOf[ShooterData]
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