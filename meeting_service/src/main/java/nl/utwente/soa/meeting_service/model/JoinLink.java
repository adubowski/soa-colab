package nl.utwente.soa.meeting_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinLink {
    private Long projectId;
    private Long goalId;
    private Long meetingId;
    private String joinLink;
    private String url;

    public JoinLink(Long projectId, Long goalId, Long meetingId, String joinLink, String url) {
        this.projectId = projectId;
        this.goalId = goalId;
        this.meetingId = meetingId;
        this.joinLink = joinLink;
        this.url = url;
    }
}
