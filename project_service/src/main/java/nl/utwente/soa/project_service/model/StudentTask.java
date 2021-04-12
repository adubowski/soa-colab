package nl.utwente.soa.project_service.model;

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
 * This class represents the Task that is assigned to a Student.
 * Task-Student is a many-to-many relationship, so this StudentTask object also represents the
 * relationship table for this Task-Student relationship.
 */
public class StudentTask {

  @Id
  @SequenceGenerator(
      name="student_task_sequence",
      sequenceName = "student_task_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "student_task_sequence"
  )
  private Long id;
  private Long studentId;
  private Long taskId;
  private Long projectId;
  private Long goalId;

  public StudentTask() {
  }

  public StudentTask(Long studentId, Long taskId, Long projectId, Long goalId) {
    this.studentId = studentId;
    this.taskId = taskId;
    this.projectId = projectId;
    this.goalId = goalId;
  }

}
