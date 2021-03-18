package nl.utwente.soa.workdistributor.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

  @Query("SELECT g FROM Goal g WHERE g.name = ?1") // this line can also be commented out
  Optional<Goal> findGoalByName(String name);
}
