package nl.utwente.soa.meeting_service.access;

import java.util.List;
import java.util.Optional;
import nl.utwente.soa.meeting_service.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

  @Query("SELECT m FROM Meeting m WHERE m.projectId = ?1 AND m.goalId = ?2")
  List<Meeting> findAllByProjectIdAndGoalId(Long projectId, Long goalId);

  @Query("SELECT m FROM Meeting m WHERE m.projectId = ?1 AND m.goalId = ?2 AND m.meetingId = ?3")
  Optional<Meeting> findMeetingByProjectIdAndGoalIdAndMeetingId(Long projectId, Long goalId, Long meetingId);

  @Modifying
  @Query("DELETE FROM Meeting m WHERE m.projectId = ?1 AND m.goalId = ?2 AND m.meetingId = ?3")
  void deleteMeetingByProjectIdAndGoalIdAndMeetingId(Long projectId, Long goalId, Long meetingId);

}
