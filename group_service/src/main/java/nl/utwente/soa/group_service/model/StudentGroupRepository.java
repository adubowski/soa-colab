package nl.utwente.soa.group_service.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {
    @Query("SELECT sg FROM StudentGroup sg WHERE sg.name = ?1")
    Optional<StudentGroup> findStudentGroupByName(String name);
}
