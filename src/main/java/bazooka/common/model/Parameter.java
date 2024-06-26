package bazooka.common.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Parameter implements Serializable {

  @Id @GeneratedValue
  private Integer id;

  @Column
  private String key;

  @Column
  private String value;

  public Parameter() {}

  public Parameter(Parameter param) {
    this.key = param.key;
    this.value = param.value;
  }

  public Parameter(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override public boolean equals(Object obj) {
    return obj instanceof Parameter
      && this.key.equals(((Parameter) obj).key)
      && this.value.equals(((Parameter) obj).value);
  }

  @Override public int hashCode() {
    return key.hashCode() * value.hashCode();
  }

  @Override public String toString() {
    return new StringBuilder()
      .append("key=").append(key)
      .append(", value=").append(value)
      .toString();
  }
}