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
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Parameter> parameters;

  public Configuration() {}

  public Configuration(String name) {
    this.name = name;
  }

  public Configuration(Configuration config) {
    this(config.name, config.parameters);
    this.id = config.id;
  }

  public Configuration(String name, List<Parameter> parameters) {
    this.name = name;

    if (parameters != null)
      for (Parameter param : parameters)
        addParameter(new Parameter(param));
  }

  /*
   * http://code.google.com/webtoolkit/articles/using_gwt_with_hibernate.html
   *
   * com.google.gwt.user.client.rpc.SerializationException: Type 'org.hibernate.collection.PersistentBag'
   * was not included in the set of types which can be serialized by this SerializationPolicy or its
   * Class object could not be loaded. For security purposes, this type will not be serialized.
   */
  public Configuration normalizeRelationships() {
    if (parameters != null)
      parameters = new ArrayList<Parameter>(parameters);

    return this;
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

  public boolean hasParameters() {
    return parameters != null && !parameters.isEmpty();
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