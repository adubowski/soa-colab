package nl.utwente.soa.scheduler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
// Lombok generates getters and setters
@Getter
@Setter
public class Goal {

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
