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
import site.rafalszatkowski.school_management_system.services.StudentService;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    final StudentRepository studentRepository;
    final TeacherRepository teacherRepository;

    @Override
    public boolean addStudent(Student student) {

        Optional<Student> optionalStudent = studentRepository.getStudentByAllData(student.getName(), student.getSurname(), student.getEmail(), student.getAge());

        if (optionalStudent.isPresent()) {
            return false;
        } else {
            studentRepository.save(student);
            return true;
        }
    }

    @Override
    public Optional<Student> getStudent(Long idStudent) {
        return studentRepository.findById(idStudent);
    }

    @Override
    public Optional<List<Student>> getStudent(String id, String name, String surname, String email, String age, String degreeCourse) {

        return studentRepository.getStudentsBySpecificData(id, name, surname, email, age, degreeCourse);
    }

    @Override
    public boolean updateStudent(Student student) {
        Optional<Student> optionalStudent = studentRepository.findById(student.getIdStudent());

        if (optionalStudent.isPresent()){
            studentRepository.save(student);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteStudent(Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isPresent()) {

            studentRepository.delete(optionalStudent.get());
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<Student> getAllStudents() {
        return (List<Student>) studentRepository.findAll();
    }

    @Override
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public List<Student> getAllStudents(Sort sort) {
        return (List<Student>) studentRepository.findAll(sort);
    }

    @Override
    public boolean addStudentsTeacher(Long idStudent, Long idTeacher) {

        Optional<Student> optionalStudent = studentRepository.findById(idStudent);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(idTeacher);

        if (optionalStudent.isPresent() && optionalTeacher.isPresent()) {

            Student student = optionalStudent.get();
            Teacher teacher = optionalTeacher.get();

            student.addTeacher(teacher);
            teacher.addStudent(student);

            studentRepository.save(student);
            return true;
        }
        else return false;
    }

    @Override
    public boolean deleteStudentsTeacher(Long idStudent, Long idTeacher) {

        Optional<Student> optionalStudent = studentRepository.findById(idStudent);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(idTeacher);

        if (optionalStudent.isPresent() && optionalTeacher.isPresent()) {

            Student student = optionalStudent.get();
            Teacher teacher = optionalTeacher.get();

            student.removeTeacher(teacher);
            teacher.removeStudent(student);

            studentRepository.save(student);

            return true;
        }
        else return false;
    }

}
