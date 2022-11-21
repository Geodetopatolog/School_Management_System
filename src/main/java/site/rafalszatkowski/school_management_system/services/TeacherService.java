package site.rafalszatkowski.school_management_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.rafalszatkowski.school_management_system.domain.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    boolean addTeacher(Teacher teacher);
    Optional<Teacher> getTeacher(Long id_teacher);
    Optional<Teacher> getTeacher(String name, String surname);
    Optional<List<Teacher>> getTeacher(String id, String name, String surname, String email, String age, String degreeCourse);
    boolean updateTeacher(Teacher teacher);
    boolean deleteTeacher(Long id);
    boolean deleteTeacher(Teacher teacher);

    List<Teacher> getAllTeachers();
    Page<Teacher> getAllTeachers(Pageable pageable);


}
