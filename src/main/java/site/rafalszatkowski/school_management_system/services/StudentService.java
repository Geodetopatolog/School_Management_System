package site.rafalszatkowski.school_management_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.rafalszatkowski.school_management_system.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    boolean addStudent(Student student);
    Optional<Student> getStudent(Long id_student);
    boolean updateStudent(Student student);
    boolean deleteStudent(Long id);
    List<Student> getAllStudents();
    Page<Student> getAllStudents(Pageable pageable);
    Optional<List<Student>> getStudent(String id, String name, String surname, String email, String age, String degreeCourse);
    boolean addStudentsTeacher(Long id_teacher, Long id_student);
    boolean deleteStudentsTeacher(Long id_teacher, Long id_student);

}
