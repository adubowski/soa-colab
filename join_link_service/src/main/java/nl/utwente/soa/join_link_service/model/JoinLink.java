package nl.utwente.soa.join_link_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinLink {

  private Long projectId;
  private Long goalId;
  private Long meetingId;
  private String url;

  public JoinLink() {

  }

  public JoinLink(Long projectId, Long goalId, Long meetingId) {
    this.projectId = projectId;
    this.goalId = goalId;
    this.meetingId = meetingId;
    this.url = "dummy.join.link?meetingId=" + meetingId + "&projectId=" + projectId + "&goalId=" + goalId;
  }
}
