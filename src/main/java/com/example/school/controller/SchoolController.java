package com.example.school.controller;

import com.example.school.model.Student;
import com.example.school.model.Teacher;
import com.example.school.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.Set;

@RestController
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @PostMapping(value = "/teacher/{teacherId}/add", consumes = "application/json")
    public ResponseEntity<Teacher> addStudentToTeacher(@PathVariable Long teacherId, @RequestBody Student student) {
        Teacher teacher = schoolService.addStudentToTeacher(teacherId, student);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/teacher/{teacherId}/students")
    public ResponseEntity<Set<Student>> getStudentsByTeacherId(@PathVariable Long teacherId) {
        Set<Student> students = schoolService.getStudentsByTeacherId(teacherId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/student/{studentId}/teachers")
    public ResponseEntity<Set<Teacher>> getTeachersByStudentId(@PathVariable Long studentId) {
        Set<Teacher> teachers = schoolService.getTeachersByStudentId(studentId);
        return ResponseEntity.ok(teachers);
    }
}
