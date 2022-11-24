package site.rafalszatkowski.school_management_system.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Student {

    private Long idStudent;
    private String name;
    private String surname;
    private Integer age;
    private String email;
    private String degreeCourse;
    private Integer numberOfTeachers;


}
