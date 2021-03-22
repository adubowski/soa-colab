package nl.utwente.soa.taskservice.access;

import java.util.List;
import java.util.Optional;
import nl.utwente.soa.taskservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  @Query("SELECT t FROM Task t WHERE t.name = ?1") // this line can also be commented out
  Optional<Task> findTaskByName(String name);

  @Query("SELECT t FROM Task t WHERE t.goalId = ?1")
  List<Task> findAllByGoalId(Long goalId);

  @Query("DELETE FROM Task t WHERE t.goalId = ?1")
  void deleteAllByGoalId(Long goalId);
}
