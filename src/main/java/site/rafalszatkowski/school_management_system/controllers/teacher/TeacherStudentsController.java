package site.rafalszatkowski.school_management_system.controllers.teacher;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rafalszatkowski.school_management_system.domains.StudentEntity;
import site.rafalszatkowski.school_management_system.domains.TeacherEntity;
import site.rafalszatkowski.school_management_system.dtos.Student;
import site.rafalszatkowski.school_management_system.mappers.StudentMapper;
import site.rafalszatkowski.school_management_system.services.TeacherService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TeacherStudentsController {

    private final TeacherService teacherService;

    @GetMapping("/teacher/student")
    public ResponseEntity<?> getTeachersStudent(@RequestParam Long idTeacher) {

        Optional<TeacherEntity> optionalTeacher = teacherService.getTeacher(idTeacher);

        if (optionalTeacher.isPresent()) {
            List<StudentEntity> teacherStudentEntities = optionalTeacher.get().getStudents().stream().toList();
            List<Student> students = StudentMapper.INSTANCE.StudentsToStudentDtos(teacherStudentEntities);
            return ResponseEntity.status(200).body(students);
        } else {
            return ResponseEntity.badRequest().body("Nauczyciel nie występuje w bazie");
        }

    }

    @PatchMapping("/teacher/student")
    public ResponseEntity<?> addTeachersStudent(String idTeacher, String idStudent) {

        if (StringUtils.isNoneBlank(idTeacher) && StringUtils.isNoneBlank(idStudent)) {
            boolean anyStudentAdded = teacherService.addTeachersStudent(Long.parseLong(idTeacher), Long.parseLong(idStudent));
            if (anyStudentAdded) {
                return ResponseEntity.accepted().build();
            }
            return ResponseEntity.status(404).body("W bazie danych nie istnieje podany nauczyciel lub student");
        } else {
            return ResponseEntity.badRequest().body("Niekompletne dane");
        }
    }

    @DeleteMapping("/teacher/student")
    public ResponseEntity<?> deleteTeachersStudent(String idTeacher, String idStudent) {

        if (StringUtils.isNoneBlank(idTeacher) && StringUtils.isNoneBlank(idStudent)) {
            boolean anyStudentRemoved = teacherService.deleteTeachersStudent(Long.parseLong(idTeacher), Long.parseLong(idStudent));
            if (anyStudentRemoved) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(404).body("W bazie danych nie istnieje podany nauczyciel lub student");
        } else {
            return ResponseEntity.badRequest().body("Niekompletne dane");
        }
    }
}
