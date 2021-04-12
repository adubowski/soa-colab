package nl.utwente.soa.group_service.services;

import nl.utwente.soa.group_service.model.MemberRepository;
import nl.utwente.soa.group_service.model.Student;
import nl.utwente.soa.group_service.model.StudentGroupRepository;
import nl.utwente.soa.group_service.model.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class StudentService {

  private final StudentRepository studentRepository;
  private final StudentGroupRepository studentGroupRepository;
  private final MemberRepository memberRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository, StudentGroupRepository studentGroupRepository, MemberRepository memberRepository) {
    this.studentRepository = studentRepository;
    this.studentGroupRepository = studentGroupRepository;
    this.memberRepository = memberRepository;
  }

  public List<Student> getStudents() {
    return studentRepository.findAll();
  }

  public void assignStudentToGroup(Long studentId, Long groupId) {
    Optional<Student> optionalStudent = studentRepository.findById(studentId);
    if (!studentGroupRepository.existsById(groupId)) {
      throw new IllegalStateException("Group with id " + groupId + " does not exist.");
    }
    if (optionalStudent.isPresent()) {
      Student student = optionalStudent.get();
      student.setGroupId(groupId);
    } else {
      throw new IllegalStateException("Student with id " + studentId + " does not exist.");
    }
  }

  public void addNewStudent(Student student) {
    Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());
    if (studentByEmail.isPresent()) {
      throw new IllegalStateException("email taken");
    } else {
      studentRepository.save(student);
    }
  }

  public void deleteStudent(Long id) {
    if (studentRepository.existsById(id)) {
      studentRepository.deleteById(id);
      memberRepository.deleteAllByStudentId(id);
    } else {
      throw new IllegalStateException("student with id " + id + " does not exist");
    }
  }

  @Transactional
  public void updateStudent(Long studentId, String name, String email, Long groupId) {
    Student student = studentRepository.findById(studentId).orElseThrow(() ->
        new IllegalStateException("No student with that Id!")
    );

    if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
      student.setName(name);
    }
    if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {
      Optional<Student> studentAtEmail = studentRepository.findStudentByEmail(email);
      if (studentAtEmail.isPresent()) {
        throw new IllegalStateException("Email taken!");
      }
      student.setEmail(email);
    }
    if (groupId != null && !Objects.equals(student.getGroupId(), groupId)) {
      student.setGroupId(groupId);
    }
  }

  public Optional<Student> getStudentById(Long id) {
    if (!studentRepository.existsById(id)) {
      throw new IllegalStateException("Student with id " + id + " does not exist");
    } else {
      return studentRepository.findById(id);
    }
  }
}
