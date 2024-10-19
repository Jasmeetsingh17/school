package com.example.school.service;





import com.example.school.model.Student;
import com.example.school.model.Teacher;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SchoolService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Teacher addStudentToTeacher(Long teacherId, Student student) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
        if (!teacherOptional.isPresent()) {
            throw new RuntimeException("Teacher not found");
        }

        Teacher teacher = teacherOptional.get();

        // Check if the student already exists
        Optional<Student> existingStudent = studentRepository.findById(student.getId());
        if (existingStudent.isPresent()) {
            student = existingStudent.get();
        } else {
            // If the student is new, save it
            studentRepository.save(student);
        }

        teacher.getStudents().add(student);
        student.getTeachers().add(teacher);

        teacherRepository.save(teacher);
        studentRepository.save(student);

        return teacher;
    }

    public Set<Student> getStudentsByTeacherId(Long teacherId) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
        if (!teacherOptional.isPresent()) {
            throw new RuntimeException("Teacher not found");
        }

        return teacherOptional.get().getStudents();
    }
    public Set<Teacher> getTeachersByStudentId(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (!studentOptional.isPresent()) {
            throw new RuntimeException("Student not found");
        }

        return studentOptional.get().getTeachers().stream()
                .map(teacher -> {
                    Teacher simplifiedTeacher = new Teacher();
                    simplifiedTeacher.setId(teacher.getId());
                    simplifiedTeacher.setName(teacher.getName());
                    return simplifiedTeacher;
                })
                .collect(Collectors.toSet());
    }

//    public Set<Teacher> getTeachersByStudentId(Long studentId) {
//        Optional<Student> studentOptional = studentRepository.findById(studentId);
//        if (!studentOptional.isPresent()) {
//            throw new RuntimeException("Student not found");
//        }
//
//        return studentOptional.get().getTeachers();
//    }
}
