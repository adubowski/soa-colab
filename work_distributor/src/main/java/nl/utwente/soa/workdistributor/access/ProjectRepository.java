package nl.utwente.soa.workdistributor.access;

import java.util.Optional;
import nl.utwente.soa.workdistributor.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Query("SELECT p FROM Project p WHERE p.name = ?1")
    // this line can also be commented out
  Optional<Project> findProjectByName(String name);

}
