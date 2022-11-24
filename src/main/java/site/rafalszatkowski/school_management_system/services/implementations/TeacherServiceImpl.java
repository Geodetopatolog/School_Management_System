package site.rafalszatkowski.school_management_system.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.rafalszatkowski.school_management_system.domains.StudentEntity;
import site.rafalszatkowski.school_management_system.domains.TeacherEntity;
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
    public boolean addTeacher(TeacherEntity teacherEntity) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.getTeacherByAllData(teacherEntity.getName(), teacherEntity.getSurname(), teacherEntity.getEmail(), teacherEntity.getAge());

        if (optionalTeacher.isPresent()) {
            return false;
        } else {
            teacherRepository.save(teacherEntity);
            return true;
        }
    }

    @Override
    public Optional<TeacherEntity> getTeacher(Long idTeacher) {
        return teacherRepository.findById(idTeacher);
    }

    @Override
    public Optional<List<TeacherEntity>> getTeacher(String id, String name, String surname, String email, String age, String schoolSubject) {
        return teacherRepository.getTeacherBySpecificData(id, name, surname, email, age, schoolSubject);
    }

    @Override
    public boolean updateTeacher(TeacherEntity teacherEntity) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(teacherEntity.getIdTeacher());

        if (optionalTeacher.isPresent()){
            teacherRepository.save(teacherEntity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteTeacher(Long id) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(id);

        if (optionalTeacher.isPresent()) {

            TeacherEntity teacherEntity = optionalTeacher.get();

            teacherEntity.getStudents().forEach(student -> student.getTeachers().remove(teacherEntity));

            teacherRepository.delete(teacherEntity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<TeacherEntity> getAllTeachers() {
        return (List<TeacherEntity>) teacherRepository.findAll();
    }

    @Override
    public Page<TeacherEntity> getAllTeachers(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }
    @Override
    public List<TeacherEntity> getAllTeachers(Sort sort) {
        return (List<TeacherEntity>) teacherRepository.findAll(sort);
    }


    @Override
    public boolean addTeachersStudent(Long idTeacher, Long idStudent) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(idTeacher);
        Optional<StudentEntity> optionalStudent = studentRepository.findById(idStudent);

        if (optionalTeacher.isPresent() && optionalStudent.isPresent()) {
            TeacherEntity teacherEntity = optionalTeacher.get();
            StudentEntity studentEntity = optionalStudent.get();

            teacherEntity.addStudent(studentEntity);
            studentEntity.addTeacher(teacherEntity);

            teacherRepository.save(teacherEntity);
            return true;
        }
        else return false;
    }

    @Override
    public boolean deleteTeachersStudent(Long idTeacher, Long idStudent) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(idTeacher);
        Optional<StudentEntity> optionalStudent = studentRepository.findById(idStudent);

        if (optionalTeacher.isPresent() && optionalStudent.isPresent()) {
            TeacherEntity teacherEntity = optionalTeacher.get();
            StudentEntity studentEntity = optionalStudent.get();

            teacherEntity.removeStudent(studentEntity);
            studentEntity.removeTeacher(teacherEntity);
            teacherRepository.save(teacherEntity);

            return true;
        }
        else return false;
    }




}
