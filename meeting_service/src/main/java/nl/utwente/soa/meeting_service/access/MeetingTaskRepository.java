package nl.utwente.soa.meeting_service.access;

import java.util.List;
import java.util.Optional;
import nl.utwente.soa.meeting_service.model.MeetingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingTaskRepository extends JpaRepository<MeetingTask, Long> {

  @Query("SELECT mt FROM MeetingTask mt WHERE mt.projectId = ?1 AND mt.goalId = ?2 AND mt.meetingId = ?3 AND mt.taskId = ?4")
  Optional<MeetingTask> findMeetingTaskByProjectIdAndGoalIdAndMeetingIdAndTaskId(Long projectId, Long goalId, Long meetingId, Long taskId);

  @Query("SELECT mt FROM MeetingTask mt WHERE mt.projectId = ?1 AND mt.goalId = ?2 AND mt.meetingId = ?3")
  List<MeetingTask> findAllByProjectIdAndGoalIdAndMeetingId(Long projectId, Long goalId, Long meetingId);

  @Modifying
  @Query("DELETE FROM MeetingTask mt WHERE mt.projectId = ?1 AND mt.goalId = ?2 AND mt.meetingId = ?3 AND mt.taskId = ?4")
  void deleteMeetingTaskByProjectIdAndGoalIdAndMeetingIdAndTaskId(Long projectId, Long goalId, Long meetingId, Long taskId);

  @Modifying
  @Query("DELETE FROM MeetingTask mt WHERE mt.projectId = ?1 AND mt.goalId = ?2 AND mt.meetingId = ?3")
  void deleteAllByProjectIdAndGoalIdAndMeetingId(Long projectId, Long goalId, Long meetingId);
}