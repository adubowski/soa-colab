package nl.utwente.soa.meeting_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class JoinLink {

  private Long projectId;
  private Long goalId;
  private Long meetingId;
  private String joinLink;

  public JoinLink() {

  }

  public JoinLink(Long projectId, Long goalId, Long meetingId, String joinLink) {
    this.projectId = projectId;
    this.goalId = goalId;
    this.meetingId = meetingId;
    this.joinLink = joinLink;
  }
}
