package nl.utwente.soa.group_service.services;

import java.util.ArrayList;
import nl.utwente.soa.group_service.model.Member;
import nl.utwente.soa.group_service.model.MemberRepository;
import nl.utwente.soa.group_service.model.Student;
import nl.utwente.soa.group_service.model.StudentGroup;
import nl.utwente.soa.group_service.model.StudentGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class StudentGroupService {

  private final StudentGroupRepository studentGroupRepository;
  private final MemberRepository memberRepository;
  private final StudentService studentService;

  @Autowired
  public StudentGroupService(StudentGroupRepository studentGroupRepository, MemberRepository memberRepository, StudentService studentService) {
    this.studentGroupRepository = studentGroupRepository;
    this.memberRepository = memberRepository;
    this.studentService = studentService;
  }

  public List<StudentGroup> getGroups() {
    return studentGroupRepository.findAll();
  }

  public Optional<StudentGroup> getGroupById(Long id) {
    if (!studentGroupRepository.existsById(id)) {
      throw new IllegalStateException("Group with Id " + id + " does not exist.");
    } else {
      return studentGroupRepository.findById(id);
    }
  }

  public void addNewGroup(StudentGroup studentGroup) {

    if (studentGroupRepository.existsById(studentGroup.getId())) {
      throw new IllegalStateException("Group ID taken");
    } else {
      studentGroupRepository.save(studentGroup);
    }
  }

  public void deleteGroup(Long id) {
    if (studentGroupRepository.existsById(id)) {
      studentGroupRepository.deleteById(id);
      memberRepository.deleteAllByStudentGroupId(id);
    } else {
      throw new IllegalStateException("Group with id " + id + " does not exist");
    }
  }

  @Transactional
  public void updateGroup(Long groupId, String name) {
    StudentGroup studentGroup = studentGroupRepository.findById(groupId).orElseThrow(() ->
        new IllegalStateException("No group with that Id!")
    );

    if (name != null && name.length() > 0 && !Objects.equals(studentGroup.getName(), name)) {
      studentGroup.setName(name);
    } else {
      throw new IllegalStateException("You need to provide a name for the group");
    }

  }

  /*
  The following operations are about students that are a member of a group
   */

  public List<Long> getStudentsOfGroup(Long groupId) {
    // throw an exception if the group of which the students are a member
    StudentGroup studentGroup = this.getGroupById(groupId).get();
    List<Member> members = memberRepository.findAllByStudentGroupId(groupId);
    List<Long> studentIds = new ArrayList<>();
    for(Member member : members) {
      studentIds.add(member.getStudentId());
    }
    return studentIds;
  }

  public List<Long> getGroupsOfStudents(Long studentId) {
    // throw an exception if the student of the studentTasks does not exist
    Student student = studentService.getStudentById(studentId).get();
    List<Member> members = memberRepository.findAllByStudentId(studentId);
    List<Long> studentGroupIds = new ArrayList<>();
    for(Member member : members) {
      studentGroupIds.add(member.getStudentGroupId());
    }
    return studentGroupIds;
  }

  public void addStudentToGroup(Long groupId, Long studentId) {
    StudentGroup studentGroup = this.getGroupById(groupId).get();
    Student student = studentService.getStudentById(studentId).get();
    Member member = new Member(studentId, groupId);
    memberRepository.save(member);
  }

  @Transactional
  public void deleteStudentFromGroup(Long groupId, Long studentId) {
    Student student = studentService.getStudentById(studentId).get();
    Member member = memberRepository.findMemberByStudentGroupIdAndStudentId(groupId, studentId)
        .orElseThrow(() -> new IllegalStateException(
        "Student with Id " + studentId + " is not assigned to this task"
    ));
    memberRepository.deleteMemberByStudentGroupIdAndStudentId(groupId, studentId);
  }
}
