package site.rafalszatkowski.school_management_system.datatransfer.dtos;


import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class StudentCreationDTO {

    private String name;
    private String surname;
    private Integer age;
    private String email;
    private String degreeCourse;

}
