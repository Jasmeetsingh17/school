package com.example.school.service;





import com.example.school.model.Student;
import com.example.school.model.Teacher;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    // Delete all student from given teacherId
public void deleteAllStudentsByTeacherId(Long teacherId) {
    Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
    if (!teacherOptional.isPresent()) {
        throw new RuntimeException("Teacher not found");
    }

    Teacher teacher = teacherOptional.get();
    Set<Student> students = teacher.getStudents();
    students.forEach(student -> student.getTeachers().remove(teacher));
    teacher.getStudents().clear();

    teacherRepository.save(teacher);
}

    // Delete student with mentioned studentId from given teacherId
    public void deleteStudentByTeacherId(Long teacherId, Long studentId) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
        if (!teacherOptional.isPresent()) {
            throw new RuntimeException("Teacher not found");
        }

        Teacher teacher = teacherOptional.get();
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (!studentOptional.isPresent()) {
            throw new RuntimeException("Student not found");
        }

        Student student = studentOptional.get();
        teacher.getStudents().remove(student);
        student.getTeachers().remove(teacher);

        teacherRepository.save(teacher);
    }

    //  adding list of students to teacher
    public Teacher addStudentsToTeacher(Long teacherId, List<Student> students) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
        if (!teacherOptional.isPresent()) {
            throw new RuntimeException("Teacher not found");
        }

        Teacher teacher = teacherOptional.get();

        for (Student student : students) {
            Optional<Student> existingStudent = studentRepository.findById(student.getId());
            if (existingStudent.isPresent()) {
                student = existingStudent.get();
            } else {
                studentRepository.save(student); // Save new student if not already present
            }
            teacher.getStudents().add(student);
            student.getTeachers().add(teacher);
        }

        teacherRepository.save(teacher);
        return teacher;
    }


}
