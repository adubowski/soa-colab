package nl.utwente.soa.workdistributor.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// Lombok generates getters and setters
@Getter
@Setter
// Entity and Tabel are used by JPA to generate a Goal table based on this java class
@Entity // used by Hibernate (JPA is an abstraction on Hibernate)
@Table
public class Goal {

  @Id
  @SequenceGenerator(
      name = "goal_sequence",
      sequenceName = "goal_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "goal_sequence"
  )
  private Long id;
  //private List<String> tasks; // should be of type List<Task>. task should have goal, not other way around
  private String name;
  private String description;
  private Date deadline;
  private String project; // should be of type Project
  private Boolean completed;

  public Goal() {
  }

  public Goal(Long id, String name, String description, Date deadline, String project, Boolean completed) {
    this.id = id;
    // this.tasks = tasks;
    this.name = name;
    this.description = description;
    this.deadline = deadline;
    this.project = project;
    this.completed = completed;
  }

  public Goal(String name, String description, Date deadline, String project, Boolean completed) {
    // this.tasks = tasks;
    this.description = description;
    this.deadline = deadline;
    this.deadline = deadline;
    this.project = project;
    this.completed = completed;
  }


}
