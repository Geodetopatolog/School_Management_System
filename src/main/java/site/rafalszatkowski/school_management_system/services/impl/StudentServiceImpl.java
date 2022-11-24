package site.rafalszatkowski.school_management_system.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.rafalszatkowski.school_management_system.domain.StudentEntity;
import site.rafalszatkowski.school_management_system.domain.TeacherEntity;
import site.rafalszatkowski.school_management_system.repositories.StudentRepository;
import site.rafalszatkowski.school_management_system.repositories.TeacherRepository;
import site.rafalszatkowski.school_management_system.services.StudentService;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    final StudentRepository studentRepository;
    final TeacherRepository teacherRepository;

    @Override
    public boolean addStudent(StudentEntity studentEntity) {

        Optional<StudentEntity> optionalStudent = studentRepository.getStudentByAllData(studentEntity.getName(), studentEntity.getSurname(), studentEntity.getEmail(), studentEntity.getAge());

        if (optionalStudent.isPresent()) {
            return false;
        } else {
            studentRepository.save(studentEntity);
            return true;
        }
    }

    @Override
    public Optional<StudentEntity> getStudent(Long idStudent) {
        return studentRepository.findById(idStudent);
    }

    @Override
    public Optional<List<StudentEntity>> getStudent(String id, String name, String surname, String email, String age, String degreeCourse) {

        return studentRepository.getStudentsBySpecificData(id, name, surname, email, age, degreeCourse);
    }

    @Override
    public boolean updateStudent(StudentEntity studentEntity) {
        Optional<StudentEntity> optionalStudent = studentRepository.findById(studentEntity.getIdStudent());

        if (optionalStudent.isPresent()){
            studentRepository.save(studentEntity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteStudent(Long id) {
        Optional<StudentEntity> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isPresent()) {

            studentRepository.delete(optionalStudent.get());
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<StudentEntity> getAllStudents() {
        return (List<StudentEntity>) studentRepository.findAll();
    }

    @Override
    public Page<StudentEntity> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public List<StudentEntity> getAllStudents(Sort sort) {
        return (List<StudentEntity>) studentRepository.findAll(sort);
    }

    @Override
    public boolean addStudentsTeacher(Long idStudent, Long idTeacher) {

        Optional<StudentEntity> optionalStudent = studentRepository.findById(idStudent);
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(idTeacher);

        if (optionalStudent.isPresent() && optionalTeacher.isPresent()) {

            StudentEntity studentEntity = optionalStudent.get();
            TeacherEntity teacherEntity = optionalTeacher.get();

            studentEntity.addTeacher(teacherEntity);
            teacherEntity.addStudent(studentEntity);

            studentRepository.save(studentEntity);
            return true;
        }
        else return false;
    }

    @Override
    public boolean deleteStudentsTeacher(Long idStudent, Long idTeacher) {

        Optional<StudentEntity> optionalStudent = studentRepository.findById(idStudent);
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(idTeacher);

        if (optionalStudent.isPresent() && optionalTeacher.isPresent()) {

            StudentEntity studentEntity = optionalStudent.get();
            TeacherEntity teacherEntity = optionalTeacher.get();

            studentEntity.removeTeacher(teacherEntity);
            teacherEntity.removeStudent(studentEntity);

            studentRepository.save(studentEntity);

            return true;
        }
        else return false;
    }

}
