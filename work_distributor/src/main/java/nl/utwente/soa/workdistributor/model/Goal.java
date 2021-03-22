package nl.utwente.soa.workdistributor.model;

import java.util.Date;
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
  private String name;
  private String description;
  private Date deadline;
  private Long projectId;
  private Boolean completed;

  public Goal() {
  }

  public Goal(String name, String description, Date deadline, Long projectId, Boolean completed) {
    this.name = name;
    this.description = description;
    this.deadline = deadline;
    this.projectId = projectId;
    this.completed = completed;
  }

  public Goal(Long id, String name, String description, Date deadline, Long projectId, Boolean completed) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.deadline = deadline;
    this.projectId = projectId;
    this.completed = completed;
  }


}
