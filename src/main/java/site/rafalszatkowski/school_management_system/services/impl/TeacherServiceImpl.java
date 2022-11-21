package site.rafalszatkowski.school_management_system.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.rafalszatkowski.school_management_system.domain.Student;
import site.rafalszatkowski.school_management_system.domain.Teacher;
import site.rafalszatkowski.school_management_system.repositories.TeacherRepository;
import site.rafalszatkowski.school_management_system.services.TeacherService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    final TeacherRepository teacherRepository;

    @Override
    public boolean addTeacher(Teacher teacher) {
        Optional<Teacher> optionalTeacher = teacherRepository.getTeacherByAllData(teacher.getName(), teacher.getSurname(), teacher.getEmail(), teacher.getAge());

        if (optionalTeacher.isPresent()) {
            return false;
        } else {
            teacherRepository.save(teacher);
            return true;
        }
    }

    @Override
    public Optional<Teacher> getTeacher(Long id_teacher) {
        return Optional.empty();
    }

    @Override
    public Optional<Teacher> getTeacher(String name, String surname) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Teacher>> getTeacher(String id, String name, String surname, String email, String age, String schoolSubject) {
        return teacherRepository.getTeacherBySpecificData(id, name, surname, email, age, schoolSubject);
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacher.getId_teacher());

        if (optionalTeacher.isPresent()){
            teacherRepository.save(teacher);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteTeacher(Long id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);

        if (optionalTeacher.isPresent()) {

            teacherRepository.delete(optionalTeacher.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        return false;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return (List<Teacher>) teacherRepository.findAll();
    }

    @Override
    public Page<Teacher> getAllTeachers(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }


}
