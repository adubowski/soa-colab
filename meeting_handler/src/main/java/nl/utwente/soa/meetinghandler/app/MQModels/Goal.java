package nl.utwente.soa.meetinghandler.app.MQModels;

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

//  public Goal(Long id, Long goalId, Long projectId, String name, String description, Date deadline, Boolean completed) {
//    this.id = id;
//    this.goalId = goalId;
//    this.projectId = projectId;
//    this.name = name;
//    this.description = description;
//    this.deadline = deadline;
//    this.completed = completed;
//  }


}
