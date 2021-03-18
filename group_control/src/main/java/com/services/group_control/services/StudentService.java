package com.services.group_control.services;

import com.services.group_control.model.Student;
import com.services.group_control.model.StudentGroup;
import com.services.group_control.model.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentRepository.findAll();
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
        } else {
            throw new IllegalStateException("student with id " + id + " does not exist");
        }
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
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
    }

    public Optional<Student> getStudentById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalStateException("Student with id " + id + "exists");
        } else {
            return studentRepository.findById(id);
        }
    }
}
