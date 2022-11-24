package site.rafalszatkowski.school_management_system.datatransfer.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import site.rafalszatkowski.school_management_system.domain.TeacherEntity;
import site.rafalszatkowski.school_management_system.dto.TeacherCreationDTO;
import site.rafalszatkowski.school_management_system.dto.TeacherDTO;
import site.rafalszatkowski.school_management_system.domain.StudentEntity;
import site.rafalszatkowski.school_management_system.mappers.TeacherMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DirtiesContext
class TeacherEntityMapperTest {

    @Test
    void teacherCreationDtoToTeacher() {

        TeacherCreationDTO teacherCreationDTO = TeacherCreationDTO.builder()
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

        assertEquals(teacherEntity, TeacherMapper.INSTANCE.TeacherCreationDtoToTeacher(teacherCreationDTO));

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

        TeacherDTO teacherDTO = TeacherDTO.builder()
                .name("I")
                .surname("N")
                .email("e@e.e")
                .age(22)
                .schoolSubject("P")
                .numberOfStudents(1)
                .build();

        assertEquals(teacherDTO, TeacherMapper.INSTANCE.TeacherToTeacherDto(teacherEntity));


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

        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setName("I");
        teacherEntity.setSurname("N");
        teacherEntity.setEmail("e@e.e");
        teacherEntity.setAge(22);
        teacherEntity.setSchoolSubject("P");

        assertEquals(teacherEntity, TeacherMapper.INSTANCE.TeacherDtoToTeacher(teacherDTO));

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

        assertEquals(teacherDTOS, TeacherMapper.INSTANCE.TeachersToTeacherDtos(teacherEntities));
    }

}