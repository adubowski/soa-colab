package nl.utwente.soa.project_service.model;

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
public class Task {
  @Id
  @SequenceGenerator(
      name = "task_sequence",
      sequenceName = "task_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "task_sequence"
  )
  private Long id;
  private Long taskId;
  private Long goalId;
  private Long projectId;
  private String name;
  private String description;
  private Integer weight;
  private Boolean completed;

  public Task() {
  }

  public Task(Long taskId, Long goalId, Long projectId, String name, String description, Integer weight, Boolean completed) {
    this.taskId = taskId;
    this.goalId = goalId;
    this.projectId = projectId;
    this.name = name;
    this.description = description;
    this.weight = weight;
    this.completed = completed;
  }

//  public Task(Long id, Long taskId, Long goalId, Long projectId, String name, String description, Integer weight, Boolean completed) {
//    this.id = id;
//    this.taskId = taskId;
//    this.goalId = goalId;
//    this.projectId = projectId;
//    this.name = name;
//    this.description = description;
//    this.weight = weight;
//    this.completed = completed;
//  }


}
