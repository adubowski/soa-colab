package nl.utwente.soa.meeting_service.access;

import java.util.List;
import java.util.Optional;
import nl.utwente.soa.meeting_service.model.Discussed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussedRepository extends JpaRepository<Discussed, Long> {

  @Query("SELECT d FROM Discussed d WHERE d.projectId = ?1 AND d.goalId = ?2 AND d.meetingId = ?3 AND d.taskId = ?4")
  Optional<Discussed> findDiscussedByProjectIdAndGoalIdAndMeetingIdAndTaskId(Long projectId, Long goalId, Long meetingId, Long taskId);

  @Query("SELECT d FROM Discussed d WHERE d.projectId = ?1 AND d.goalId = ?2 AND d.meetingId = ?3")
  List<Discussed> findAllByProjectIdAndGoalIdAndMeetingId(Long projectId, Long goalId, Long meetingId);

  @Modifying
  @Query("DELETE FROM Discussed d WHERE d.projectId = ?1 AND d.goalId = ?2 AND d.meetingId = ?3 AND d.taskId = ?4")
  void deleteDiscussedByProjectIdAndGoalIdAndMeetingIdAndTaskId(Long projectId, Long goalId, Long meetingId, Long taskId);

  @Modifying
  @Query("DELETE FROM Discussed d WHERE d.projectId = ?1 AND d.goalId = ?2 AND d.meetingId = ?3")
  void deleteAllByProjectIdAndGoalIdAndMeetingId(Long projectId, Long goalId, Long meetingId);
}