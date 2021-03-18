package com.services.group_control.rest;


import com.services.group_control.model.StudentGroup;
import com.services.group_control.services.StudentGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/group")
public class StudentGroupRestController {
    private final StudentGroupService studentGroupService;

    @Autowired
    public StudentGroupRestController(StudentGroupService studentGroupService) {
        this.studentGroupService = studentGroupService;
    }

    @GetMapping
    public List<StudentGroup> getStudents() {
        return studentGroupService.getGroups();
    }

    @PostMapping
    public void registerNewStudent(@RequestBody StudentGroup studentGroup) {
        studentGroupService.addNewGroup(studentGroup);
    }

    @DeleteMapping(path="{groupId}")
    public void deleteStudent(@PathVariable("groupId") Long id) {
        studentGroupService.deleteGroup(id);
    }

    @PutMapping(path="{groupId}")
    public void updateStudent(
            @PathVariable("groupId") Long groupId,
            @RequestParam(required = false) String name) {
        studentGroupService.updateGroup(groupId, name);
    }
}
