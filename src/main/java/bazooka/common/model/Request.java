package bazooka.common.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Request implements Serializable {

  @Id @GeneratedValue
  private Integer id;

  @Column(unique = true)
  private String name;

  @Lob
  private String payload;

  public Request() {
    this(null);
  }

  public Request(String name) {
    this(name, null);
  }

  public Request(String name, String payload) {
    this.name = name;
    this.payload = payload;
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

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  @Override public boolean equals(Object obj) {
    return obj instanceof Request
      && this.name.equals(((Request) obj).name);
  }

  @Override public int hashCode() {
    return name.hashCode();
  }

  @Override public String toString() {
    return name;
  }
}