package site.rafalszatkowski.school_management_system.datatransfer.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import site.rafalszatkowski.school_management_system.domain.StudentEntity;
import site.rafalszatkowski.school_management_system.dto.StudentCreationDTO;
import site.rafalszatkowski.school_management_system.dto.StudentDTO;
import site.rafalszatkowski.school_management_system.domain.TeacherEntity;
import site.rafalszatkowski.school_management_system.mappers.StudentMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@DirtiesContext
class StudentEntityMapperTest {

    @Test
    void studentCreationDtoToStudent() {
        StudentCreationDTO studentCreationDTO = StudentCreationDTO.builder()
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

        assertEquals(studentEntity, StudentMapper.INSTANCE.StudentCreationDtoToStudent(studentCreationDTO));
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

        StudentDTO studentDTO = StudentDTO.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .degreeCourse("K")
                .numberOfTeachers(1)
                .build();

        assertEquals(studentDTO, StudentMapper.INSTANCE.StudentToStudentDto(studentEntity));
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

        StudentEntity studentEntity = new StudentEntity();
                studentEntity.setName("I");
                studentEntity.setSurname("N");
                studentEntity.setEmail("e@e.e");
                studentEntity.setAge(22);
                studentEntity.setDegreeCourse("K");

        assertEquals(studentEntity, StudentMapper.INSTANCE.StudentDtoToStudent(studentDTO));
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

        assertEquals(studentDTOS, StudentMapper.INSTANCE.StudentsToStudentDtos(studentEntities));
    }

}