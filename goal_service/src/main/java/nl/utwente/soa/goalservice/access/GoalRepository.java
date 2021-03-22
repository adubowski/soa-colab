package nl.utwente.soa.goalservice.access;

import java.util.List;
import java.util.Optional;
import nl.utwente.soa.goalservice.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

  @Query("SELECT g FROM Goal g WHERE g.name = ?1")
    // this line can also be commented out
  Optional<Goal> findGoalByName(String name);

  @Query("SELECT g FROM Goal g WHERE g.projectId = ?1")
  List<Goal> findAllByProjectId(Long projectId);

  @Query("DELETE FROM Goal g WHERE g.projectId = ?1")
  void deleteAllByProjectId(Long projectId);
}
