package site.rafalszatkowski.school_management_system.datatransfer.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import site.rafalszatkowski.school_management_system.domains.StudentEntity;
import site.rafalszatkowski.school_management_system.dtos.Student;
import site.rafalszatkowski.school_management_system.dtos.StudentCreation;
import site.rafalszatkowski.school_management_system.domains.TeacherEntity;
import site.rafalszatkowski.school_management_system.mappers.StudentMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@DirtiesContext
class StudentEntityMapperTest {

    @Test
    void studentCreationDtoToStudent() {
        StudentCreation studentCreation = StudentCreation.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .degreeCourse("K")
                .build();

        StudentEntity studentEntity = new StudentEntity();
                studentEntity.setName("I");
                studentEntity.setSurname("N");
                studentEntity.setEmail("e@e.e");
                studentEntity.setAge(22);
                studentEntity.setDegreeCourse("K");

        assertEquals(studentEntity, StudentMapper.INSTANCE.StudentCreationDtoToStudent(studentCreation));
    }

    @Test
    void studentToStudentDto() {

        StudentEntity studentEntity = new StudentEntity();
                studentEntity.setName("I");
                studentEntity.setSurname("N");
                studentEntity.setEmail("e@e.e");
                studentEntity.setAge(22);
                studentEntity.setDegreeCourse("K");
                studentEntity.addTeacher(new TeacherEntity());

        Student student = Student.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .degreeCourse("K")
                .numberOfTeachers(1)
                .build();

        assertEquals(student, StudentMapper.INSTANCE.StudentToStudentDto(studentEntity));
    }

    @Test
    void studentDtoToStudent() {

        Student student = Student.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .degreeCourse("K")
                .numberOfTeachers(1)
                .build();

        StudentEntity studentEntity = new StudentEntity();
                studentEntity.setName("I");
                studentEntity.setSurname("N");
                studentEntity.setEmail("e@e.e");
                studentEntity.setAge(22);
                studentEntity.setDegreeCourse("K");

        assertEquals(studentEntity, StudentMapper.INSTANCE.StudentDtoToStudent(student));
    }

    @Test
    void studentsToStudentDtos() {

        StudentEntity studentEntity1 = new StudentEntity();
        studentEntity1.setName("Ii");
        studentEntity1.setSurname("Nn");
        studentEntity1.setEmail("e@e.e");
        studentEntity1.setAge(22);
        studentEntity1.setDegreeCourse("Kk");

        StudentEntity studentEntity2 = new StudentEntity();
        studentEntity2.setName("I");
        studentEntity2.setSurname("N");
        studentEntity2.setEmail("e@e.e");
        studentEntity2.setAge(22);
        studentEntity2.setDegreeCourse("K");

        //użyto Listy zamiast Setu, żeby uniknąć różnicy w kolejności elementów przy zamianie kolekcji
        List<StudentEntity> studentEntities = new ArrayList<>();
        studentEntities.add(studentEntity1);
        studentEntities.add(studentEntity2);

        Student student1 = Student.builder()
                .name("Ii")
                .surname("Nn")
                .email("e@e.e")
                .age(22)
                .degreeCourse("Kk")
                .numberOfTeachers(0)
                .build();

        Student student2 = Student.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .degreeCourse("K")
                .numberOfTeachers(0)
                .build();

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        assertEquals(students, StudentMapper.INSTANCE.StudentsToStudentDtos(studentEntities));
    }

}