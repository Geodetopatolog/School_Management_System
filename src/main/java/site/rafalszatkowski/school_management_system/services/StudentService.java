package site.rafalszatkowski.school_management_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import site.rafalszatkowski.school_management_system.domains.StudentEntity;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    boolean addStudent(StudentEntity studentEntity);
    Optional<StudentEntity> getStudent(Long idStudent);
    boolean updateStudent(StudentEntity studentEntity);
    boolean deleteStudent(Long id);
    List<StudentEntity> getAllStudents();
    Page<StudentEntity> getAllStudents(Pageable pageable);
    List<StudentEntity> getAllStudents(Sort sort);
    Optional<List<StudentEntity>> getStudent(String id, String name, String surname, String email, String age, String degreeCourse);
    boolean addStudentsTeacher(Long idTeacher, Long idStudent);
    boolean deleteStudentsTeacher(Long idTeacher, Long idStudent);

}
