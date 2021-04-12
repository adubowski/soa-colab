package nl.utwente.soa.project_service.access;

import java.util.List;
import java.util.Optional;
import nl.utwente.soa.project_service.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

  @Query("SELECT g FROM Goal g WHERE g.name = ?1")
    // this line can also be commented out
  Optional<Goal> findGoalByName(String name);

  @Query("SELECT g FROM Goal g WHERE g.projectId = ?1 AND g.goalId = ?2")
  Optional<Goal> findGoalByProjectIdAndGoalId(Long projectId, Long goalId);

  @Query("SELECT g FROM Goal g WHERE g.projectId = ?1")
  List<Goal> findAllByProjectId(Long projectId);

  @Modifying
  @Query("DELETE FROM Goal g WHERE g.projectId = ?1 AND g.goalId = ?2")
  void deleteGoalByProjectIdAndGoalId(Long projectId, Long goalId);

  @Modifying
  @Query("DELETE FROM Goal g WHERE g.projectId = ?1")
  void deleteAllByProjectId(Long projectId);
}
