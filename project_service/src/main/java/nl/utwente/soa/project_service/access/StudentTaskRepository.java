package nl.utwente.soa.project_service.access;

import java.util.List;
import java.util.Optional;
import nl.utwente.soa.project_service.model.Student;
import nl.utwente.soa.project_service.model.StudentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTaskRepository extends JpaRepository<StudentTask, Long>  {

  @Query("SELECT st FROM StudentTask st WHERE st.projectId = ?1 AND st.goalId = ?2 AND st.studentId = ?3 AND st.taskId = ?4")
  Optional<StudentTask> findStudentTaskByProjectIdAndGoalIdAndStudentIdAndTaskId(Long projectId, Long goalId, Long studentId, Long taskId);

  @Query("SELECT st FROM StudentTask st WHERE st.projectId = ?1 AND st.goalId = ?2 AND st.taskId = ?3")
  List<StudentTask> findAllByProjectIdAndGoalIdAndTaskId(Long projectId, Long goalId, Long taskId);

  @Query("SELECT st FROM StudentTask st WHERE st.projectId = ?1 AND st.studentId = ?2")
  List<StudentTask> findAllByProjectIdAndStudentId(Long projectId, Long studentId);

  @Modifying
  @Query("DELETE FROM StudentTask st WHERE st.projectId = ?1 AND st.goalId = ?2 AND st.studentId = ?3 AND st.taskId = ?4")
  void deleteStudentTaskByProjectIdAndGoalIdAndStudentIdAndTaskId(Long projectId, Long goalId, Long studentId, Long taskId);

  @Modifying
  @Query("DELETE FROM StudentTask st WHERE st.projectId = ?1 AND st.goalId = ?2 AND st.taskId = ?3")
  void deleteAllByProjectIdAndGoalIdAndTaskId(Long projectId, Long goalId, Long taskId);

  @Modifying
  @Query("DELETE FROM StudentTask st WHERE st.projectId = ?1 AND st.goalId = ?2")
  void deleteAllByProjectIdAndGoalId(Long projectId, Long goalId);

}
