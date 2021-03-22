package nl.utwente.soa.scheduler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Task {

  private Long id;
  private String name;
  private String description;
  private Long goalId;
  private Integer weight;
  private Boolean completed;

  public Task() {
  }

  public Task(String name, String description, Long goalId, Integer weight, Boolean completed) {
    this.name = name;
    this.description = description;
    this.goalId = goalId;
    this.weight = weight;
    this.completed = completed;
  }

  public Task(Long id, String name, String description, Long goalId, Integer weight, Boolean completed) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.goalId = goalId;
    this.weight = weight;
    this.completed = completed;
  }


}
