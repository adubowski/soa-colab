package nl.utwente.soa.join_link_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Meeting {

  private Long id;
  private Long meetingId;
  private Long projectId;
  private Long goalId;
  private Date date;
  private String joinLink;

  public Meeting() {
  }

  public Meeting(Long meetingId, Long projectId, Long goalId, Date date, String joinLink) {
    this. meetingId = meetingId;
    this.projectId = projectId;
    this.goalId = goalId;
    this.date = date;
    this.joinLink = joinLink;
  }

}
