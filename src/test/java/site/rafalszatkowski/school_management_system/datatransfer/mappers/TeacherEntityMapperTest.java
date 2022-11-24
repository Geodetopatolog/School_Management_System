package site.rafalszatkowski.school_management_system.datatransfer.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import site.rafalszatkowski.school_management_system.domains.TeacherEntity;
import site.rafalszatkowski.school_management_system.dtos.Teacher;
import site.rafalszatkowski.school_management_system.dtos.TeacherCreation;
import site.rafalszatkowski.school_management_system.domains.StudentEntity;
import site.rafalszatkowski.school_management_system.mappers.TeacherMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DirtiesContext
class TeacherEntityMapperTest {

    @Test
    void teacherCreationDtoToTeacher() {

        TeacherCreation teacherCreation = TeacherCreation.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .schoolSubject("P")
                .build();

        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setName("I");
        teacherEntity.setSurname("N");
        teacherEntity.setEmail("e@e.e");
        teacherEntity.setAge(22);
        teacherEntity.setSchoolSubject("P");

        assertEquals(teacherEntity, TeacherMapper.INSTANCE.TeacherCreationDtoToTeacher(teacherCreation));

    }

    @Test
    void teacherToTeacherDto() {

        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setName("I");
        teacherEntity.setSurname("N");
        teacherEntity.setEmail("e@e.e");
        teacherEntity.setAge(22);
        teacherEntity.setSchoolSubject("P");
        teacherEntity.addStudent(new StudentEntity());

        Teacher teacher = Teacher.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .schoolSubject("P")
                .numberOfStudents(1)
                .build();

        assertEquals(teacher, TeacherMapper.INSTANCE.TeacherToTeacherDto(teacherEntity));


    }

    @Test
    void teacherDtoToTeacher() {

        Teacher teacher = Teacher.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .schoolSubject("P")
                .numberOfStudents(1)
                .build();

        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setName("I");
        teacherEntity.setSurname("N");
        teacherEntity.setEmail("e@e.e");
        teacherEntity.setAge(22);
        teacherEntity.setSchoolSubject("P");

        assertEquals(teacherEntity, TeacherMapper.INSTANCE.TeacherDtoToTeacher(teacher));

    }

    @Test
    void teachersToTeacherDtos() {

        TeacherEntity teacherEntity1 = new TeacherEntity();
        teacherEntity1.setName("Ii");
        teacherEntity1.setSurname("Nn");
        teacherEntity1.setEmail("e@e.e");
        teacherEntity1.setAge(22);
        teacherEntity1.setSchoolSubject("Pp");

        TeacherEntity teacherEntity2 = new TeacherEntity();
        teacherEntity2.setName("I");
        teacherEntity2.setSurname("N");
        teacherEntity2.setEmail("e@e.e");
        teacherEntity2.setAge(22);
        teacherEntity2.setSchoolSubject("P");


        //użyto Listy zamiast Setu, żeby uniknąć różnicy w kolejności elementów przy zamianie kolekcji
        List<TeacherEntity> teacherEntities = new ArrayList<>();
        teacherEntities.add(teacherEntity1);
        teacherEntities.add(teacherEntity2);

        Teacher teacher1 = Teacher.builder()
                .name("Ii")
                .surname("Nn")
                .email("e@e.e")
                .age(22)
                .schoolSubject("Pp")
                .numberOfStudents(0)
                .build();

        Teacher teacher2 = Teacher.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .schoolSubject("P")
                .numberOfStudents(0)
                .build();


        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher1);
        teachers.add(teacher2);

        assertEquals(teachers, TeacherMapper.INSTANCE.TeachersToTeacherDtos(teacherEntities));
    }

}