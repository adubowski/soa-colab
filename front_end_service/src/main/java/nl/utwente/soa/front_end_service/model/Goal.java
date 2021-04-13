package nl.utwente.soa.front_end_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Goal {

  private Long id;
  private Long goalId;
  private Long projectId;
  private String name;
  private String description;
  private Date deadline;
  private Boolean completed;

  public Goal() {
  }

  public Goal(Long goalId, Long projectId, String name, String description, Date deadline, Boolean completed) {
    this.goalId = goalId;
    this.projectId = projectId;
    this.name = name;
    this.description = description;
    this.deadline = deadline;
    this.completed = completed;
  }

}
