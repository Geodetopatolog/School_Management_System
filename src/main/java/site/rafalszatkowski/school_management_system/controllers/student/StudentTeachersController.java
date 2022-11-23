package site.rafalszatkowski.school_management_system.controllers.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rafalszatkowski.school_management_system.datatransfer.dtos.TeacherDTO;
import site.rafalszatkowski.school_management_system.datatransfer.mappers.TeacherMapper;
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
    public ResponseEntity<?> getStudentsTeacher(@RequestParam Long id_student) {

        Optional<Student> optionalStudent = studentService.getStudent(id_student);


        if (optionalStudent.isPresent()) {
            List<Teacher> studentTeachers = optionalStudent.get().getTeachers().stream().toList();
            List<TeacherDTO> teacherDTOS = TeacherMapper.INSTANCE.TeachersToTeacherDtos(studentTeachers);
            return ResponseEntity.status(200).body(teacherDTOS);
        } else {
            return ResponseEntity.badRequest().body("Student nie występuje w bazie");
        }

    }

    @PatchMapping("/student/teacher")
    public ResponseEntity<?> addStudentsTeacher(Long id_student, Long id_teacher) {

        if (id_student != null && id_teacher != null) {
            boolean anyTeacherAdded = studentService.addStudentsTeacher(id_student, id_teacher);
            if (anyTeacherAdded) {
                return ResponseEntity.accepted().build();
            }
            return ResponseEntity.status(404).body("W bazie danych nie istnieje podany nauczyciel lub student");
        } else {
            return ResponseEntity.badRequest().body("Niekompletne dane");
        }
    }

    @DeleteMapping("/student/teacher")
    public ResponseEntity<?> deleteStudentsTeacher(Long id_student, Long id_teacher) {

        if (id_student != null && id_teacher != null) {
            boolean anyTeacherRemoved = studentService.deleteStudentsTeacher(id_student, id_teacher);
            if (anyTeacherRemoved) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(404).body("W bazie danych nie istnieje podany nauczyciel lub student");
        } else {
            return ResponseEntity.badRequest().body("Niekompletne dane");
        }
    }


}
