package nl.utwente.soa.project_service.access;

import java.util.List;
import java.util.Optional;
import nl.utwente.soa.project_service.model.Goal;
import nl.utwente.soa.project_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  @Query("SELECT t FROM Task t WHERE t.name = ?1")
    // this line can also be commented out
  Optional<Task> findTaskByName(String name);

  @Query("SELECT t FROM Task t WHERE t.projectId = ?1 AND t.goalId = ?2 AND t.taskId = ?3")
  Optional<Task> findTaskByProjectIdAndGoalIdAndTaskId(Long projectId, Long goalId, Long taskId);

  @Query("SELECT t FROM Task t WHERE t.projectId = ?1 AND t.goalId = ?2")
  List<Task> findAllByProjectIdAndGoalId(Long projectId, Long goalId);

  @Modifying
  @Query("DELETE FROM Task t WHERE t.projectId = ?1 AND t.goalId = ?2 AND t.taskId = ?3")
  void deleteTaskByProjectIdAndGoalIdAndTaskId(Long projectId, Long goalId, Long taskId);

  @Modifying
  @Query("DELETE FROM Task t WHERE t.projectId = ?1 AND t.goalId = ?2")
  void deleteAllByProjectIdAndGoalId(Long projectId, Long goalId);

  @Modifying
  @Query("DELETE FROM Task t WHERE t.projectId = ?1")
  void deleteAllByProjectId(Long projectId);
}
