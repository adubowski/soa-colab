package nl.utwente.soa.group_control.model;


import javax.persistence.*;

@Entity
@Table
public class StudentGroup {

  @Id
  @SequenceGenerator(
      name = "group_sequence",
      sequenceName = "group_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "group_sequence"
  )
  private Long id;

  @Column
  private String name;

  public StudentGroup(String name) {
    this.name = name;
  }

  public StudentGroup() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Group{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
