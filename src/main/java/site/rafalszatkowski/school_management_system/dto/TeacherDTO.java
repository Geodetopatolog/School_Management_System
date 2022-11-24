package site.rafalszatkowski.school_management_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherDTO {

    private Long idTeacher;
    private String name;
    private String surname;
    private Integer age;
    private String email;
    private String schoolSubject;
    private Integer numberOfStudents;

}
