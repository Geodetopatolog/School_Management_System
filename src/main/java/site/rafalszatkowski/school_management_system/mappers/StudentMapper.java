package site.rafalszatkowski.school_management_system.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import site.rafalszatkowski.school_management_system.domains.StudentEntity;
import site.rafalszatkowski.school_management_system.dtos.Student;
import site.rafalszatkowski.school_management_system.dtos.StudentCreation;

import java.util.List;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "idStudent", ignore = true)
    StudentEntity StudentCreationDtoToStudent (StudentCreation studentCreation);

    @Mapping(target = "numberOfTeachers", expression = "java(studentEntity.getTeachers().size())")
    Student StudentToStudentDto (StudentEntity studentEntity);

    @Mapping(target = "teachers", ignore = true)
    StudentEntity StudentDtoToStudent (Student student);


    List<Student> StudentsToStudentDtos (List<StudentEntity> studentEntities);



}
