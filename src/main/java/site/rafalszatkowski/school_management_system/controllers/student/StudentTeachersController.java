package site.rafalszatkowski.school_management_system.controllers.student;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rafalszatkowski.school_management_system.dtos.Teacher;
import site.rafalszatkowski.school_management_system.mappers.TeacherMapper;
import site.rafalszatkowski.school_management_system.domains.StudentEntity;
import site.rafalszatkowski.school_management_system.domains.TeacherEntity;
import site.rafalszatkowski.school_management_system.services.StudentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StudentTeachersController {

    private final StudentService studentService;

    @GetMapping("/student/teacher")
    public ResponseEntity<?> getStudentsTeacher(@RequestParam Long idStudent) {

        Optional<StudentEntity> optionalStudent = studentService.getStudent(idStudent);


        if (optionalStudent.isPresent()) {
            List<TeacherEntity> studentTeachers = optionalStudent.get().getTeachers().stream().toList();
            List<Teacher> teachers = TeacherMapper.INSTANCE.TeachersToTeacherDtos(studentTeachers);
            return ResponseEntity.status(200).body(teachers);
        } else {
            return ResponseEntity.badRequest().body("StudentEntity nie występuje w bazie");
        }

    }

    @PatchMapping("/student/teacher")
    public ResponseEntity<?> addStudentsTeacher(String idStudent, String idTeacher) {

        if (StringUtils.isNoneBlank(idStudent) && StringUtils.isNoneBlank(idTeacher)) {
            boolean anyTeacherAdded = studentService.addStudentsTeacher(Long.parseLong(idStudent), Long.parseLong(idTeacher));
            if (anyTeacherAdded) {
                return ResponseEntity.accepted().build();
            }
            return ResponseEntity.status(404).body("W bazie danych nie istnieje podany nauczyciel lub student");
        } else {
            return ResponseEntity.badRequest().body("Niekompletne dane");
        }
    }

    @DeleteMapping("/student/teacher")
    public ResponseEntity<?> deleteStudentsTeacher(String idStudent, String idTeacher) {

        if (StringUtils.isNoneBlank(idStudent) && StringUtils.isNoneBlank(idTeacher)) {
            boolean anyTeacherRemoved = studentService.deleteStudentsTeacher(Long.parseLong(idStudent), Long.parseLong(idTeacher));
            if (anyTeacherRemoved) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(404).body("W bazie danych nie istnieje podany nauczyciel lub student");
        } else {
            return ResponseEntity.badRequest().body("Niekompletne dane");
        }
    }


}
