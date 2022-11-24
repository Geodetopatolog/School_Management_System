package site.rafalszatkowski.school_management_system.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import site.rafalszatkowski.school_management_system.domain.TeacherEntity;
import site.rafalszatkowski.school_management_system.dto.TeacherCreationDTO;
import site.rafalszatkowski.school_management_system.dto.TeacherDTO;

import java.util.List;
@Mapper
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "idTeacher", ignore = true)
    TeacherEntity TeacherCreationDtoToTeacher (TeacherCreationDTO teacherCreationDTO);

    @Mapping(target = "numberOfStudents", expression = "java(teacherEntity.getStudents().size())")
    TeacherDTO TeacherToTeacherDto (TeacherEntity teacherEntity);

    @Mapping(target = "students", ignore = true)
    TeacherEntity TeacherDtoToTeacher (TeacherDTO teacherDTO);


    List<TeacherDTO> TeachersToTeacherDtos (List<TeacherEntity> teacherEntities);

}
