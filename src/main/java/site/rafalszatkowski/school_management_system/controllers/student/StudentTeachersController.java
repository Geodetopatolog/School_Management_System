package site.rafalszatkowski.school_management_system.controllers.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rafalszatkowski.school_management_system.dto.TeacherDTO;
import site.rafalszatkowski.school_management_system.mappers.TeacherMapper;
import site.rafalszatkowski.school_management_system.domain.Student;
import site.rafalszatkowski.school_management_system.domain.Teacher;
import site.rafalszatkowski.school_management_system.services.StudentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StudentTeachersController {

    private final StudentService studentService;

    @GetMapping("/student/teacher")
    public ResponseEntity<?> getStudentsTeacher(@RequestParam Long idStudent) {

        Optional<Student> optionalStudent = studentService.getStudent(idStudent);


        if (optionalStudent.isPresent()) {
            List<Teacher> studentTeachers = optionalStudent.get().getTeachers().stream().toList();
            List<TeacherDTO> teacherDTOS = TeacherMapper.INSTANCE.TeachersToTeacherDtos(studentTeachers);
            return ResponseEntity.status(200).body(teacherDTOS);
        } else {
            return ResponseEntity.badRequest().body("Student nie występuje w bazie");
        }

    }

    @PatchMapping("/student/teacher")
    public ResponseEntity<?> addStudentsTeacher(Long idStudent, Long idTeacher) {

        if (idStudent != null && idTeacher != null) {
            boolean anyTeacherAdded = studentService.addStudentsTeacher(idStudent, idTeacher);
            if (anyTeacherAdded) {
                return ResponseEntity.accepted().build();
            }
            return ResponseEntity.status(404).body("W bazie danych nie istnieje podany nauczyciel lub student");
        } else {
            return ResponseEntity.badRequest().body("Niekompletne dane");
        }
    }

    @DeleteMapping("/student/teacher")
    public ResponseEntity<?> deleteStudentsTeacher(Long idStudent, Long idTeacher) {

        if (idStudent != null && idTeacher != null) {
            boolean anyTeacherRemoved = studentService.deleteStudentsTeacher(idStudent, idTeacher);
            if (anyTeacherRemoved) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(404).body("W bazie danych nie istnieje podany nauczyciel lub student");
        } else {
            return ResponseEntity.badRequest().body("Niekompletne dane");
        }
    }


}
