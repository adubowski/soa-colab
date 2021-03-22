package nl.utwente.soa.goalservice.model;


import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Project {

  @Id
  @SequenceGenerator(
      name = "project_sequence",
      sequenceName = "project_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "project_sequence"
  )
  private Long id;
  private Long studentGroupID;
  private String name;
  private String description;
  private Date deadline;
  private Boolean completed;

  public Project() {
  }

  public Project(Long studentGroupID, String name, String description, Date deadline, Boolean completed) {
    this.studentGroupID = studentGroupID;
    this.name = name;
    this.description = description;
    this.deadline = deadline;
    this.completed = completed;
  }

  public Project(Long id, Long studentGroupID, String name, String description, Date deadline, Boolean completed) {
    this.id = id;
    this.studentGroupID = studentGroupID;
    this.name = name;
    this.description = description;
    this.deadline = deadline;
    this.completed = completed;
  }
}
