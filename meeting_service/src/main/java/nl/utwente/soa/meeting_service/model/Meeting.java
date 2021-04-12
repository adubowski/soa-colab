package nl.utwente.soa.meeting_service.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Meeting {

  @Id
  @SequenceGenerator(
      name="meeting_sequence",
      sequenceName = "meeting_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "meeting_sequence"
  )
  private Long id;
  private Long meetingId;
  private Long projectId;
  private Long goalId;
  private Date date;
  private String joinLink;

  public Meeting() {
  }

  public Meeting(Long meetingId, Long projectId, Long goalId, Date date, String joinLink) {
    this.meetingId = meetingId;
    this.projectId = projectId;
    this.goalId = goalId;
    this.date = date;
    this.joinLink = joinLink;
  }
  public Meeting(Long meetingId, Long projectId, Long goalId, Date date) {
    this.meetingId = meetingId;
    this.projectId = projectId;
    this.goalId = goalId;
    this.date = date;
  }

}
