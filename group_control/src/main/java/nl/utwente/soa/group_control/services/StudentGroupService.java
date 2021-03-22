package nl.utwente.soa.group_control.services;

import nl.utwente.soa.group_control.model.StudentGroup;
import nl.utwente.soa.group_control.model.StudentGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentGroupService {

  private final StudentGroupRepository studentGroupRepository;

  @Autowired
  public StudentGroupService(StudentGroupRepository studentGroupRepository) {
    this.studentGroupRepository = studentGroupRepository;
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

}
