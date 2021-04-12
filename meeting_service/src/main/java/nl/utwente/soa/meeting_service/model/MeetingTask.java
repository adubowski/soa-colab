package nl.utwente.soa.meeting_service.model;

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
/**
 * This class represents the Task that is discussed at a Meeting.
 * Task-Meeting is a many-to-many relationship, so this MeetingTask object also represents the
 * relationship table for this Task-Meeting relationship.
 */
public class MeetingTask {

  @Id
  @SequenceGenerator(
      name="meeting_task_sequence",
      sequenceName = "meeting_task_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "meeting_task_sequence"
  )
  private Long id;
  private Long meetingId;
  private Long taskId;
  private Long projectId;
  private Long goalId;
  private String notes;

  public MeetingTask() {
  }

  public MeetingTask(Long meetingId, Long taskId, Long projectId, Long goalId, String notes) {
    this.meetingId = meetingId;
    this.taskId = taskId;
    this.projectId = projectId;
    this.goalId = goalId;
    this.notes = notes;
  }

}
