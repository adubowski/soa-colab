package nl.utwente.soa.meetinghandler.app.MQModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
// Lombok generates getters and setters
@Getter
@Setter
public class ScheduledMeeting {

  private Long meetingId;
  private Boolean planned;
  private Boolean finished;
  private Date date;
  private Goal goal;
  private List<Task> tasks;

  public ScheduledMeeting() {
  }

  public ScheduledMeeting(Long meetingId, Boolean planned, Boolean finished, Date date, Goal goal, List<Task> tasks) {
    this. meetingId = meetingId;
    this.planned = planned;
    this.finished = finished;
    this.date = date;
    this.goal = goal;
    this.tasks = tasks;
  }
}
