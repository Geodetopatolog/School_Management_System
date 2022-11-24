package site.rafalszatkowski.school_management_system.datatransfer.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import site.rafalszatkowski.school_management_system.dto.TeacherCreationDTO;
import site.rafalszatkowski.school_management_system.dto.TeacherDTO;
import site.rafalszatkowski.school_management_system.domain.Student;
import site.rafalszatkowski.school_management_system.domain.Teacher;
import site.rafalszatkowski.school_management_system.mappers.TeacherMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DirtiesContext
class TeacherMapperTest {

    @Test
    void teacherCreationDtoToTeacher() {

        TeacherCreationDTO teacherCreationDTO = TeacherCreationDTO.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .schoolSubject("P")
                .build();

        Teacher teacher = new Teacher();
        teacher.setName("I");
        teacher.setSurname("N");
        teacher.setEmail("e@e.e");
        teacher.setAge(22);
        teacher.setSchoolSubject("P");

        assertEquals(teacher, TeacherMapper.INSTANCE.TeacherCreationDtoToTeacher(teacherCreationDTO));

    }

    @Test
    void teacherToTeacherDto() {

        Teacher teacher = new Teacher();
        teacher.setName("I");
        teacher.setSurname("N");
        teacher.setEmail("e@e.e");
        teacher.setAge(22);
        teacher.setSchoolSubject("P");
        teacher.addStudent(new Student());

        TeacherDTO teacherDTO = TeacherDTO.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .schoolSubject("P")
                .numberOfStudents(1)
                .build();

        assertEquals(teacherDTO, TeacherMapper.INSTANCE.TeacherToTeacherDto(teacher));


    }

    @Test
    void teacherDtoToTeacher() {

        TeacherDTO teacherDTO = TeacherDTO.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .schoolSubject("P")
                .numberOfStudents(1)
                .build();

        Teacher teacher = new Teacher();
        teacher.setName("I");
        teacher.setSurname("N");
        teacher.setEmail("e@e.e");
        teacher.setAge(22);
        teacher.setSchoolSubject("P");

        assertEquals(teacher, TeacherMapper.INSTANCE.TeacherDtoToTeacher(teacherDTO));

    }

    @Test
    void teachersToTeacherDtos() {

        Teacher teacher1 = new Teacher();
        teacher1.setName("Ii");
        teacher1.setSurname("Nn");
        teacher1.setEmail("e@e.e");
        teacher1.setAge(22);
        teacher1.setSchoolSubject("Pp");

        Teacher teacher2 = new Teacher();
        teacher2.setName("I");
        teacher2.setSurname("N");
        teacher2.setEmail("e@e.e");
        teacher2.setAge(22);
        teacher2.setSchoolSubject("P");


        //użyto Listy zamiast Setu, żeby uniknąć różnicy w kolejności elementów przy zamianie kolekcji
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher1);
        teachers.add(teacher2);

        TeacherDTO teacherDTO1 = TeacherDTO.builder()
                .name("Ii")
                .surname("Nn")
                .email("e@e.e")
                .age(22)
                .schoolSubject("Pp")
                .numberOfStudents(0)
                .build();

        TeacherDTO teacherDTO2 = TeacherDTO.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .schoolSubject("P")
                .numberOfStudents(0)
                .build();


        List<TeacherDTO> teacherDTOS = new ArrayList<>();
        teacherDTOS.add(teacherDTO1);
        teacherDTOS.add(teacherDTO2);

        assertEquals(teacherDTOS, TeacherMapper.INSTANCE.TeachersToTeacherDtos(teachers));
    }

}