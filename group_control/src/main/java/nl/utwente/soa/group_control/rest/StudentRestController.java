package nl.utwente.soa.group_control.rest;

import nl.utwente.soa.group_control.model.Student;
import nl.utwente.soa.group_control.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "api/v1/student")
public class StudentRestController {

  private final StudentService studentService;

  @Autowired
  public StudentRestController(StudentService studentService) {
    this.studentService = studentService;
  }

  @GetMapping
  public List<Student> getStudents() {
    return studentService.getStudents();
  }

  @GetMapping(path = "{studentId}")
  public Optional<Student> getGroupById(@PathVariable("studentId") Long id) {
    return studentService.getStudentById(id);
  }

  @PostMapping
  public void registerNewStudent(@RequestBody Student student) {
    studentService.addNewStudent(student);
  }

  @DeleteMapping(path = "{studentId}")
  public void deleteStudent(@PathVariable("studentId") Long id) {
    studentService.deleteStudent(id);
  }

  @PutMapping(path = "{StudentId}")
  public void updateStudent(
      @PathVariable("StudentId") Long studentId,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) Long groupId) {
    studentService.updateStudent(studentId, name, email, groupId);
  }

}

