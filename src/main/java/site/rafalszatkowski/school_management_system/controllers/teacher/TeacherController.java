package site.rafalszatkowski.school_management_system.controllers.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rafalszatkowski.school_management_system.components.Validator;
import site.rafalszatkowski.school_management_system.datatransfer.dtos.TeacherCreationDTO;
import site.rafalszatkowski.school_management_system.datatransfer.dtos.TeacherDTO;
import site.rafalszatkowski.school_management_system.datatransfer.mappers.TeacherMapper;
import site.rafalszatkowski.school_management_system.domain.Teacher;
import site.rafalszatkowski.school_management_system.services.TeacherService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class TeacherController {

    public static final int PAGE_SIZE = 5;

    final TeacherService teacherService;

    @PostMapping("/teacher")
    public ResponseEntity<?> addTeacher(@RequestBody TeacherCreationDTO teacherCreationDTO) {

        if (Validator.getInstance().validateAllData(
                teacherCreationDTO.getName(),
                teacherCreationDTO.getSurname(),
                teacherCreationDTO.getEmail(),
                teacherCreationDTO.getAge())) {
            if (teacherService.addTeacher(TeacherMapper.INSTANCE.TeacherCreationDtoToTeacher(teacherCreationDTO))) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResponseEntity.badRequest().body("Nauczyciel już istnieje w bazie");
            }

        } else {
            return ResponseEntity.badRequest().body("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, " +
                    "wiek < 18 lat lub  niepoprawny adres email");
        }
    }

    @GetMapping("/teacher/query")
    public @ResponseBody ResponseEntity<?> getTeacher(@RequestParam(defaultValue = "%%") String id,
                                                      @RequestParam(defaultValue = "%%") String name,
                                                      @RequestParam(defaultValue = "%%") String surname,
                                                      @RequestParam(defaultValue = "%%") String email,
                                                      @RequestParam(defaultValue = "%%") String age,
                                                      @RequestParam(defaultValue = "%%") String schoolSubject) {

        if (id.equals("%%") && name.equals("%%") && surname.equals("%%")
                && email.equals("%%") && age.equals("%%") && schoolSubject.equals("%%")) {
            return ResponseEntity.badRequest().body("Nie wprowadzono parametrów wyszukiwania");
        } else {
            Optional<List<Teacher>> optionalQueryResult = teacherService.getTeacher(id, name, surname, email, age, schoolSubject);
            if (optionalQueryResult.isPresent() && optionalQueryResult.get().size()>0) {
                return ResponseEntity.status(200).body(TeacherMapper.INSTANCE.TeachersToTeacherDtos(optionalQueryResult.get()));
            } else {
                return ResponseEntity.status(404).body("Nie znaleziono wpisów odpowiadających wyszukiwaniu");
            }
        }
    }


    @PatchMapping("/teacher")
    public ResponseEntity<?> updateTeacher(@RequestBody TeacherDTO teacherDTO){

        if (Validator.getInstance().validateAllData(
                teacherDTO.getName(),
                teacherDTO.getSurname(),
                teacherDTO.getEmail(),
                teacherDTO.getAge())) {
            Teacher teacher = TeacherMapper.INSTANCE.TeacherDtoToTeacher(teacherDTO);

            if (teacherService.updateTeacher(teacher)){
                return ResponseEntity.status(HttpStatus.ACCEPTED).build();
            } else {
                return ResponseEntity.badRequest().body("Nauczyciel, którego dane próbowano uaktualnić, " +
                        "nie występuje w bazie danych");
            }

        } else {
            return ResponseEntity.badRequest().body("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, " +
                    "wiek < 18 lat lub  niepoprawny adres email");
        }
    }


    @DeleteMapping("/teacher")
    public ResponseEntity<?> removeTeacher(@RequestParam Long id) {

        if (teacherService.deleteTeacher(id)) {
            return ResponseEntity.ok().build();
        } else  {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nauczyciel, którego dane próbowano usunąć, " +
                    "nie występuje w bazie danych");
        }
    }


    @GetMapping("/teacher/all")
    public @ResponseBody List<TeacherDTO> getAllTeachers (@RequestParam Optional<String> page,
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
                Page<Teacher> teachers = teacherService.getAllTeachers(pageRequest);
                return TeacherMapper.INSTANCE.TeachersToTeacherDtos(teachers.getContent());

            } else {
                //samo sortowanie
                Sort sortOrder;
                if (descending.equals("false")) {
                    sortOrder = Sort.by(sortBy.get()).ascending();
                } else {
                    sortOrder = Sort.by(sortBy.get()).descending();
                }
                return TeacherMapper.INSTANCE.TeachersToTeacherDtos(teacherService.getAllTeachers(sortOrder));
            }
        }
        else {
            if (page.isPresent()) {
                //same strony
                int currentPage = Integer.parseInt(page.get());
                Pageable pageRequest = PageRequest.of(currentPage, PAGE_SIZE);
                Page<Teacher> teachers = teacherService.getAllTeachers(pageRequest);
                return TeacherMapper.INSTANCE.TeachersToTeacherDtos(teachers.getContent());
            } else {
                //bez niczego
                return TeacherMapper.INSTANCE.TeachersToTeacherDtos(teacherService.getAllTeachers());
            }
        }
    }




}
