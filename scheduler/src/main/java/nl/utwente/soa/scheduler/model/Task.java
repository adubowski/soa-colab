package nl.utwente.soa.scheduler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Task {
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

}