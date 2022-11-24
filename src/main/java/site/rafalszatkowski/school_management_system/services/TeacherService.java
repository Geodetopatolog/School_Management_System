package site.rafalszatkowski.school_management_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import site.rafalszatkowski.school_management_system.domain.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    boolean addTeacher(Teacher teacher);
    Optional<Teacher> getTeacher(Long idTeacher);
    Optional<List<Teacher>> getTeacher(String id, String name, String surname, String email, String age, String degreeCourse);
    boolean updateTeacher(Teacher teacher);
    boolean deleteTeacher(Long id);
    List<Teacher> getAllTeachers();
    Page<Teacher> getAllTeachers(Pageable pageable);
    List<Teacher> getAllTeachers(Sort sort);
    boolean addTeachersStudent(Long idTeacher, Long idStudent);
    boolean deleteTeachersStudent(Long idTeacher, Long idStudent);

}
