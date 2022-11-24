package site.rafalszatkowski.school_management_system.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import site.rafalszatkowski.school_management_system.dto.TeacherCreationDTO;
import site.rafalszatkowski.school_management_system.dto.TeacherDTO;
import site.rafalszatkowski.school_management_system.domain.Teacher;

import java.util.List;
@Mapper
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "idTeacher", ignore = true)
    Teacher TeacherCreationDtoToTeacher (TeacherCreationDTO teacherCreationDTO);

    @Mapping(target = "numberOfStudents", expression = "java(teacher.getStudents().size())")
    TeacherDTO TeacherToTeacherDto (Teacher teacher);

    @Mapping(target = "students", ignore = true)
    Teacher TeacherDtoToTeacher (TeacherDTO teacherDTO);


    List<TeacherDTO> TeachersToTeacherDtos (List<Teacher> teachers);

}
