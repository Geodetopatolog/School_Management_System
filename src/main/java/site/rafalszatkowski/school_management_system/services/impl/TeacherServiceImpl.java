package site.rafalszatkowski.school_management_system.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.rafalszatkowski.school_management_system.domain.Student;
import site.rafalszatkowski.school_management_system.domain.Teacher;
import site.rafalszatkowski.school_management_system.repositories.StudentRepository;
import site.rafalszatkowski.school_management_system.repositories.TeacherRepository;
import site.rafalszatkowski.school_management_system.services.TeacherService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    final TeacherRepository teacherRepository;
    final StudentRepository studentRepository;

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
    public Optional<Teacher> getTeacher(Long idTeacher) {
        return teacherRepository.findById(idTeacher);
    }

    @Override
    public Optional<List<Teacher>> getTeacher(String id, String name, String surname, String email, String age, String schoolSubject) {
        return teacherRepository.getTeacherBySpecificData(id, name, surname, email, age, schoolSubject);
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacher.getIdTeacher());

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

            Teacher teacher = optionalTeacher.get();

            teacher.getStudents().forEach(student -> student.getTeachers().remove(teacher));

            teacherRepository.delete(teacher);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return (List<Teacher>) teacherRepository.findAll();
    }

    @Override
    public Page<Teacher> getAllTeachers(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }
    @Override
    public List<Teacher> getAllTeachers(Sort sort) {
        return (List<Teacher>) teacherRepository.findAll(sort);
    }


    @Override
    public boolean addTeachersStudent(Long idTeacher, Long idStudent) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(idTeacher);
        Optional<Student> optionalStudent = studentRepository.findById(idStudent);

        if (optionalTeacher.isPresent() && optionalStudent.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            Student student = optionalStudent.get();

            teacher.addStudent(student);
            student.addTeacher(teacher);

            teacherRepository.save(teacher);
            return true;
        }
        else return false;
    }

    @Override
    public boolean deleteTeachersStudent(Long idTeacher, Long idStudent) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(idTeacher);
        Optional<Student> optionalStudent = studentRepository.findById(idStudent);

        if (optionalTeacher.isPresent() && optionalStudent.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            Student student = optionalStudent.get();

            teacher.removeStudent(student);
            student.removeTeacher(teacher);
            teacherRepository.save(teacher);

            return true;
        }
        else return false;
    }




}
