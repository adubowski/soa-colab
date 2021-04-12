package nl.utwente.soa.group_service.rest;


import nl.utwente.soa.group_service.model.StudentGroup;
import nl.utwente.soa.group_service.services.StudentGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/groups")
public class StudentGroupRestController {
  private final StudentGroupService studentGroupService;

  @Autowired
  public StudentGroupRestController(StudentGroupService studentGroupService) {
    this.studentGroupService = studentGroupService;
  }

  @GetMapping
  public List<StudentGroup> getGroups() {
    return studentGroupService.getGroups();
  }

  @GetMapping(path = "{groupId}")
  public Optional<StudentGroup> getGroupById(@PathVariable("groupId") Long id) {
    return studentGroupService.getGroupById(id);
  }

  @PostMapping
  public void registerNewStudentGroup(@RequestBody StudentGroup studentGroup) {
    studentGroupService.addNewGroup(studentGroup);
  }

  @DeleteMapping(path = "{groupId}")
  public void deleteStudentGroup(@PathVariable("groupId") Long id) {
    studentGroupService.deleteGroup(id);
  }

  @PutMapping(path = "{groupId}")
  public void updateStudentGroup(
      @PathVariable("groupId") Long groupId,
      @RequestParam(required = false) String name) {
    studentGroupService.updateGroup(groupId, name);
  }

  /*
  The following operations are about students MEMBER of a studentGroup
   */

  @GetMapping("{groupId}/students")
  public List<Long> getStudentsOfGroup(@PathVariable("groupId") Long groupId) {
    return studentGroupService.getStudentsOfGroup(groupId);
  }

  @PostMapping("{groupId}/students")
  public void addStudentToGroup(@PathVariable("groupId") Long groupId,
                                @RequestBody Long studentId) {
    studentGroupService.addStudentToGroup(groupId, studentId);
  }

  @DeleteMapping("{groupId}/students/{studentId}")
  public void deleteStudentFromGroup(@PathVariable("groupId") Long groupId,
                                     @PathVariable("studentId") Long studentId) {
    studentGroupService.deleteStudentFromGroup(groupId, studentId);
  }
}
