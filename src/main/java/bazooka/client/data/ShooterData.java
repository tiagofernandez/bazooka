package bazooka.client.data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

public class ShooterData implements Serializable {

  @Id @GeneratedValue Integer id;
  @Column String name;

  public ShooterData() {}

  public ShooterData(String name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override public boolean equals(Object obj) {
    if (obj instanceof ShooterData) {
      ShooterData other = (ShooterData) obj;
      return id != null && id.equals(other.id);
    }
    return false;
  }

  @Override public int hashCode() {
    return 6502 + id ^ (id >>> 32);
  }

  @Override public String toString() {
    return name;
  }
}