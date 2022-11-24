package site.rafalszatkowski.school_management_system.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import site.rafalszatkowski.school_management_system.domain.StudentEntity;
import site.rafalszatkowski.school_management_system.dto.StudentCreationDTO;
import site.rafalszatkowski.school_management_system.dto.StudentDTO;

import java.util.List;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "idStudent", ignore = true)
    StudentEntity StudentCreationDtoToStudent (StudentCreationDTO studentCreationDTO);

    @Mapping(target = "numberOfTeachers", expression = "java(studentEntity.getTeachers().size())")
    StudentDTO StudentToStudentDto (StudentEntity studentEntity);

    @Mapping(target = "teachers", ignore = true)
    StudentEntity StudentDtoToStudent (StudentDTO studentDTO);


    List<StudentDTO> StudentsToStudentDtos (List<StudentEntity> studentEntities);



}
