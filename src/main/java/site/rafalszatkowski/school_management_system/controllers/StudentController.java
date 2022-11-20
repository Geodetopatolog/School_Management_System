package site.rafalszatkowski.school_management_system.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.rafalszatkowski.school_management_system.datatransfer.dtos.StudentCreationDTO;
import site.rafalszatkowski.school_management_system.datatransfer.dtos.StudentDTO;
import site.rafalszatkowski.school_management_system.datatransfer.mappers.StudentMapper;
import site.rafalszatkowski.school_management_system.domain.Student;
import site.rafalszatkowski.school_management_system.services.StudentService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;


    @PostMapping("/student")
    public ResponseEntity<?> addStudent(@RequestBody StudentCreationDTO studentCreationDTO) {

        if (studentCreationDTO.validateData()) {
            if (studentService.addStudent(StudentMapper.INSTANCE.StudentCreationDtoToStudent(studentCreationDTO))) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResponseEntity.badRequest().body("Student już istnieje w bazie");
            }

        } else {
            return ResponseEntity.badRequest().body("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, " +
                    "wiek < 18 lat lub  niepoprawny adres email");
        }
    }

    @GetMapping("/student/query")
    public @ResponseBody List<StudentDTO> getStudent(@RequestParam(defaultValue = "%%") String id,
                                       @RequestParam(defaultValue = "%%") String name,
                                       @RequestParam(defaultValue = "%%") String surname,
                                       @RequestParam(defaultValue = "%%") String email,
                                       @RequestParam(defaultValue = "%%") String age,
                                       @RequestParam(defaultValue = "%%") String degreeCourse) {

        if (id.equals("%%") && name.equals("%%") && surname.equals("%%")
                && email.equals("%%") && age.equals("%%") && degreeCourse.equals("%%")) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nie wprowadzono parametrów wyszukiwania");
        } else {
            Optional<List<Student>> optionalQueryResult = studentService.getStudent(id, name, surname, email, age, degreeCourse);
                if (optionalQueryResult.isPresent()) {
                    return StudentMapper.INSTANCE.StudentsToStudentDtos(optionalQueryResult.get());
                } else {
                    throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono wpisów odpowiadających wyszukiwaniu");
            }
        }

    }


    @PatchMapping("/student")
    public ResponseEntity<?> updateStudent(@RequestBody StudentDTO studentDTO){

        if (studentDTO.validateData()) {
            Student student = StudentMapper.INSTANCE.StudentDtoToStudent(studentDTO);

            if (studentService.updateStudent(student)){
                return ResponseEntity.status(HttpStatus.ACCEPTED).build();
            } else {
                return ResponseEntity.badRequest().body("Student, którego dane próbowano uaktualnić, " +
                        "nie występuje w bazie danych");
            }

        } else {
            return ResponseEntity.badRequest().body("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, " +
                    "wiek < 18 lat lub  niepoprawny adres email");
        }

    }


    @DeleteMapping("/student")
    public ResponseEntity<?> removeStudent(@RequestParam Long id_student) {

        if (studentService.deleteStudent(id_student)) {
            return ResponseEntity.ok().build();
        } else  {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student, którego dane próbowano usunąć, " +
                    "nie występuje w bazie danych");
        }
    }


    @GetMapping("/student/all")
    public @ResponseBody List<StudentDTO> getAllStudents () {
        return StudentMapper.INSTANCE.StudentsToStudentDtos(studentService.getAllStudents());
    }





}
