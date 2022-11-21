package site.rafalszatkowski.school_management_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.rafalszatkowski.school_management_system.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    public boolean addStudent(Student student);
    public Optional<Student> getStudent(Long id_student);
    public Optional<Student> getStudent(String name, String surname);
    public boolean updateStudent(Student student);
    public boolean deleteStudent(Long id);
    public boolean deleteStudent(Student student);

    public List<Student> getAllStudents();
    Page<Student> getAllStudents(Pageable pageable);

    Optional<List<Student>> getStudent(String id, String name, String surname, String email, String age, String degreeCourse);



}
