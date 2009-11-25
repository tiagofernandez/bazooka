package bazooka.common.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Configuration implements Serializable {

  @Id @GeneratedValue
  private Integer id;

  @Column(unique = true)
  private String name;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Property> properties;

  public Configuration() {
    this(null);
  }

  public Configuration(String name) {
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

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(List<Property> properties) {
    this.properties = properties;
  }

  public void addProperty(Property property) {
    if (properties == null)
      properties = new ArrayList<Property>();

    properties.add(property);
  }

  public void removeProperty(Property property) {
    if (properties != null)
      properties.remove(property);
  }

  @Override public boolean equals(Object obj) {
    return obj instanceof Configuration
      && this.name.equals(((Configuration) obj).name);
  }

  @Override public int hashCode() {
    return name.hashCode();
  }

  @Override public String toString() {
    return name;
  }
}