package bazooka.common.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Configuration implements Serializable {

  @Id @GeneratedValue
  private Integer id;

  @Column(unique = true)
  private String name;

  @SuppressWarnings({"NonJREEmulationClassesInClientCode"})
  @OneToMany(cascade = CascadeType.ALL)
  private List<Parameter> parameters;

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

  public List<Parameter> getParameters() {
    if (parameters != null)
    Collections.sort(parameters, new Comparator<Parameter>() {
      public int compare(Parameter p1, Parameter p2) {
        return p1.getKey().compareTo(p2.getKey());
      }
    });
    return parameters;
  }

  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }

  public void addParameter(Parameter parameter) {
    if (parameters == null)
      parameters = new ArrayList<Parameter>();

    parameters.add(parameter);
  }

  public void removeParameter(Parameter parameter) {
    if (parameters != null)
      parameters.remove(parameter);
  }

  public void clearParameters() {
    if (parameters != null)
      parameters.clear();
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