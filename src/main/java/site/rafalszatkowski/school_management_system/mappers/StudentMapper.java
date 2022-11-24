package site.rafalszatkowski.school_management_system.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import site.rafalszatkowski.school_management_system.dto.StudentCreationDTO;
import site.rafalszatkowski.school_management_system.dto.StudentDTO;
import site.rafalszatkowski.school_management_system.domain.Student;

import java.util.List;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "idStudent", ignore = true)
    Student StudentCreationDtoToStudent (StudentCreationDTO studentCreationDTO);

    @Mapping(target = "numberOfTeachers", expression = "java(student.getTeachers().size())")
    StudentDTO StudentToStudentDto (Student student);

    @Mapping(target = "teachers", ignore = true)
    Student StudentDtoToStudent (StudentDTO studentDTO);


    List<StudentDTO> StudentsToStudentDtos (List<Student> students);



}
