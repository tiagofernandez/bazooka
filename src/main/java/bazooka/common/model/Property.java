package bazooka.common.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Property implements Serializable {

  @Id @GeneratedValue
  private Integer id;

  @Column
  private String key;

  @Column
  private String value;

  @ManyToOne
  private Configuration configuration;

  public Property() {
    this(null, null);
  }

  public Property(String key, String value) {
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

  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override public boolean equals(Object obj) {
    return obj instanceof Property
      && this.key.equals(((Property) obj).key)
      && this.value.equals(((Property) obj).value);
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