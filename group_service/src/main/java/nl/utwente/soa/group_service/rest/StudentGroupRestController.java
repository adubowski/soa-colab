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
}
