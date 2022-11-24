package site.rafalszatkowski.school_management_system.controllers.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rafalszatkowski.school_management_system.dto.StudentDTO;
import site.rafalszatkowski.school_management_system.mappers.StudentMapper;
import site.rafalszatkowski.school_management_system.domain.Student;
import site.rafalszatkowski.school_management_system.domain.Teacher;
import site.rafalszatkowski.school_management_system.services.TeacherService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TeacherStudentsController {

    private final TeacherService teacherService;

    @GetMapping("/teacher/student")
    public ResponseEntity<?> getTeachersStudent(@RequestParam Long idTeacher) {

        Optional<Teacher> optionalTeacher = teacherService.getTeacher(idTeacher);

        if (optionalTeacher.isPresent()) {
            List<Student> teacherStudents = optionalTeacher.get().getStudents().stream().toList();
            List<StudentDTO> studentDTOS = StudentMapper.INSTANCE.StudentsToStudentDtos(teacherStudents);
            return ResponseEntity.status(200).body(studentDTOS);
        } else {
            return ResponseEntity.badRequest().body("Nauczyciel nie występuje w bazie");
        }

    }

    @PatchMapping("/teacher/student")
    public ResponseEntity<?> addTeachersStudent(Long idTeacher, Long idStudent) {

        if (idTeacher != null && idStudent != null) {
            boolean anyStudentAdded = teacherService.addTeachersStudent(idTeacher, idStudent);
            if (anyStudentAdded) {
                return ResponseEntity.accepted().build();
            }
            return ResponseEntity.status(404).body("W bazie danych nie istnieje podany nauczyciel lub student");
        } else {
            return ResponseEntity.badRequest().body("Niekompletne dane");
        }
    }

    @DeleteMapping("/teacher/student")
    public ResponseEntity<?> deleteTeachersStudent(Long idTeacher, Long idStudent) {

        if (idTeacher != null && idStudent != null) {
            boolean anyStudentRemoved = teacherService.deleteTeachersStudent(idTeacher, idStudent);
            if (anyStudentRemoved) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(404).body("W bazie danych nie istnieje podany nauczyciel lub student");
        } else {
            return ResponseEntity.badRequest().body("Niekompletne dane");
        }
    }
}
