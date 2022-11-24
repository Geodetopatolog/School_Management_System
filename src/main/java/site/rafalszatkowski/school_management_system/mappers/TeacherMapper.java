package site.rafalszatkowski.school_management_system.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import site.rafalszatkowski.school_management_system.domains.TeacherEntity;
import site.rafalszatkowski.school_management_system.dtos.TeacherCreation;
import site.rafalszatkowski.school_management_system.dtos.Teacher;

import java.util.List;
@Mapper
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "idTeacher", ignore = true)
    TeacherEntity TeacherCreationDtoToTeacher (TeacherCreation teacherCreation);

    @Mapping(target = "numberOfStudents", expression = "java(teacherEntity.getStudents().size())")
    Teacher TeacherToTeacherDto (TeacherEntity teacherEntity);

    @Mapping(target = "students", ignore = true)
    TeacherEntity TeacherDtoToTeacher (Teacher teacher);


    List<Teacher> TeachersToTeacherDtos (List<TeacherEntity> teacherEntities);

}
