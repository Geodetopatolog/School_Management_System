package site.rafalszatkowski.school_management_system.controllers.student;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rafalszatkowski.school_management_system.components.Validator;
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

    public static final int PAGE_SIZE = 5;
    private final StudentService studentService;


    @PostMapping("/student")
    public ResponseEntity<?> addStudent(@RequestBody StudentCreationDTO studentCreationDTO) {

        if (Validator.getInstance().validateAllData(
                studentCreationDTO.getName(),
                studentCreationDTO.getSurname(),
                studentCreationDTO.getEmail(),
                studentCreationDTO.getAge())) {

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
    public @ResponseBody ResponseEntity<?> getStudent(@RequestParam(defaultValue = "%%") String id,
                                       @RequestParam(defaultValue = "%%") String name,
                                       @RequestParam(defaultValue = "%%") String surname,
                                       @RequestParam(defaultValue = "%%") String email,
                                       @RequestParam(defaultValue = "%%") String age,
                                       @RequestParam(defaultValue = "%%") String degreeCourse) {

        if (id.equals("%%") && name.equals("%%") && surname.equals("%%")
                && email.equals("%%") && age.equals("%%") && degreeCourse.equals("%%")) {
                return ResponseEntity.badRequest().body("Nie wprowadzono parametrów wyszukiwania");
        } else {
            Optional<List<Student>> optionalQueryResult = studentService.getStudent(id, name, surname, email, age, degreeCourse);
                if (optionalQueryResult.isPresent() && optionalQueryResult.get().size()>0) {
                    return ResponseEntity.status(200).body(StudentMapper.INSTANCE.StudentsToStudentDtos(optionalQueryResult.get()));
                } else {
                    return ResponseEntity.status(404).body("Nie znaleziono wpisów odpowiadających wyszukiwaniu");
            }
        }
    }

    @PatchMapping("/student")
    public ResponseEntity<?> updateStudent(@RequestBody StudentDTO studentDTO){

        if (Validator.getInstance().validateAllData(
                studentDTO.getName(),
                studentDTO.getSurname(),
                studentDTO.getEmail(),
                studentDTO.getAge())) {
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
    public ResponseEntity<?> removeStudent(@RequestParam Long id) {

        if (studentService.deleteStudent(id)) {
            return ResponseEntity.ok().build();
        } else  {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student, którego dane próbowano usunąć, " +
                    "nie występuje w bazie danych");
        }
    }

    @GetMapping("/student/all")
    public @ResponseBody List<StudentDTO> getAllStudents (@RequestParam Optional<String> page,
                                                          @RequestParam Optional<String> sortBy,
                                                          @RequestParam(defaultValue = "false") String descending
                                                          ) {

        if (sortBy.isPresent()) {
            if (page.isPresent()) {
                //strony i sortowanie
                int currentPage = Integer.parseInt(page.get());
                Sort sortOrder;
                Pageable pageRequest;
                if (descending.equals("false")) {
                    sortOrder = Sort.by(sortBy.get()).ascending();
                } else {
                    sortOrder = Sort.by(sortBy.get()).descending();
                }
                pageRequest = PageRequest.of(currentPage, PAGE_SIZE, sortOrder);
                Page<Student> students = studentService.getAllStudents(pageRequest);
                return StudentMapper.INSTANCE.StudentsToStudentDtos(students.getContent());

            } else {
                //samo sortowanie
                Sort sortOrder;
                if (descending.equals("false")) {
                    sortOrder = Sort.by(sortBy.get()).ascending();
                } else {
                    sortOrder = Sort.by(sortBy.get()).descending();
                }
                return StudentMapper.INSTANCE.StudentsToStudentDtos(studentService.getAllStudents(sortOrder));
            }
        }
         else {
            if (page.isPresent()) {
                //same strony
                int currentPage = Integer.parseInt(page.get());
                Pageable pageRequest = PageRequest.of(currentPage, PAGE_SIZE);
                Page<Student> students = studentService.getAllStudents(pageRequest);
                return StudentMapper.INSTANCE.StudentsToStudentDtos(students.getContent());
            } else {
                //bez niczego
                return StudentMapper.INSTANCE.StudentsToStudentDtos(studentService.getAllStudents());
            }

        }
    }





}
