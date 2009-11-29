package bazooka.common.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Shooter implements Serializable {

  @Id @GeneratedValue
  private Integer id;

  @Column(unique = true)
  private String name;

  @Lob
  private String script;

  public Shooter() {}

  public Shooter(String name) {
    this(name, null);
  }

  public Shooter(String name, String script) {
    this.name = name;
    this.script = script;
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

  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  @Override public boolean equals(Object obj) {
    return obj instanceof Shooter
      && this.name.equals(((Shooter) obj).name);
  }

  @Override public int hashCode() {
    return name.hashCode();
  }

  @Override public String toString() {
    return name;
  }
}