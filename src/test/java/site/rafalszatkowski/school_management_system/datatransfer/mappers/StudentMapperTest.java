package site.rafalszatkowski.school_management_system.datatransfer.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import site.rafalszatkowski.school_management_system.dto.StudentCreationDTO;
import site.rafalszatkowski.school_management_system.dto.StudentDTO;
import site.rafalszatkowski.school_management_system.domain.Student;
import site.rafalszatkowski.school_management_system.domain.Teacher;
import site.rafalszatkowski.school_management_system.mappers.StudentMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@DirtiesContext
class StudentMapperTest {

    @Test
    void studentCreationDtoToStudent() {
        StudentCreationDTO studentCreationDTO = StudentCreationDTO.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .degreeCourse("K")
                .build();

        Student student = new Student();
                student.setName("I");
                student.setSurname("N");
                student.setEmail("e@e.e");
                student.setAge(22);
                student.setDegreeCourse("K");

        assertEquals(student, StudentMapper.INSTANCE.StudentCreationDtoToStudent(studentCreationDTO));
    }

    @Test
    void studentToStudentDto() {

        Student student = new Student();
                student.setName("I");
                student.setSurname("N");
                student.setEmail("e@e.e");
                student.setAge(22);
                student.setDegreeCourse("K");
                student.addTeacher(new Teacher());

        StudentDTO studentDTO = StudentDTO.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .degreeCourse("K")
                .numberOfTeachers(1)
                .build();

        assertEquals(studentDTO, StudentMapper.INSTANCE.StudentToStudentDto(student));
    }

    @Test
    void studentDtoToStudent() {

        StudentDTO studentDTO = StudentDTO.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .degreeCourse("K")
                .numberOfTeachers(1)
                .build();

        Student student = new Student();
                student.setName("I");
                student.setSurname("N");
                student.setEmail("e@e.e");
                student.setAge(22);
                student.setDegreeCourse("K");

        assertEquals(student, StudentMapper.INSTANCE.StudentDtoToStudent(studentDTO));
    }

    @Test
    void studentsToStudentDtos() {

        Student student1 = new Student();
        student1.setName("Ii");
        student1.setSurname("Nn");
        student1.setEmail("e@e.e");
        student1.setAge(22);
        student1.setDegreeCourse("Kk");

        Student student2 = new Student();
        student2.setName("I");
        student2.setSurname("N");
        student2.setEmail("e@e.e");
        student2.setAge(22);
        student2.setDegreeCourse("K");

        //użyto Listy zamiast Setu, żeby uniknąć różnicy w kolejności elementów przy zamianie kolekcji
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        StudentDTO studentDTO1 = StudentDTO.builder()
                .name("Ii")
                .surname("Nn")
                .email("e@e.e")
                .age(22)
                .degreeCourse("Kk")
                .numberOfTeachers(0)
                .build();

        StudentDTO studentDTO2 = StudentDTO.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .degreeCourse("K")
                .numberOfTeachers(0)
                .build();

        List<StudentDTO> studentDTOS = new ArrayList<>();
        studentDTOS.add(studentDTO1);
        studentDTOS.add(studentDTO2);

        assertEquals(studentDTOS, StudentMapper.INSTANCE.StudentsToStudentDtos(students));
    }

}