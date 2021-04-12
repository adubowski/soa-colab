package nl.utwente.soa.group_service.model;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  @Query("SELECT m FROM Member m WHERE m.studentGroupId = ?1 AND m.studentId = ?2")
  Optional<Member> findMemberByStudentGroupIdAndStudentId(Long studentGroupId, Long studentId);

  @Query("SELECT m FROM Member m WHERE m.studentGroupId = ?1")
  List<Member> findAllByStudentGroupId(Long studentGroupId);

  @Query("SELECT m FROM Member m WHERE m.studentId = ?1")
  List<Member> findAllByStudentId(Long studentId);

  @Modifying
  @Query("DELETE FROM Member m WHERE m.studentGroupId = ?1 AND m.studentId = ?2")
  void deleteMemberByStudentGroupIdAndStudentId(Long studentGroupId, Long studentId);

  @Modifying
  @Query("DELETE FROM Member m WHERE m.studentId = ?1")
  void deleteAllByStudentId(Long studentId);

  @Modifying
  @Query("DELETE FROM Member m WHERE m.studentGroupId = ?1")
  void deleteAllByStudentGroupId(Long studentGroupId);
}
