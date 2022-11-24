package site.rafalszatkowski.school_management_system.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import site.rafalszatkowski.school_management_system.domain.TeacherEntity;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    boolean addTeacher(TeacherEntity teacherEntity);
    Optional<TeacherEntity> getTeacher(Long idTeacher);
    Optional<List<TeacherEntity>> getTeacher(String id, String name, String surname, String email, String age, String degreeCourse);
    boolean updateTeacher(TeacherEntity teacherEntity);
    boolean deleteTeacher(Long id);
    List<TeacherEntity> getAllTeachers();
    Page<TeacherEntity> getAllTeachers(Pageable pageable);
    List<TeacherEntity> getAllTeachers(Sort sort);
    boolean addTeachersStudent(Long idTeacher, Long idStudent);
    boolean deleteTeachersStudent(Long idTeacher, Long idStudent);

}
