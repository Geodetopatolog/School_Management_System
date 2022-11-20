package site.rafalszatkowski.school_management_system.datatransfer.dtos;


import lombok.Builder;
import lombok.Data;

import java.util.regex.Pattern;
@Data
@Builder
public class StudentCreationDTO {

    private String name;
    private String surname;
    private Integer age;
    private String email;
    private String degreeCourse;


    public boolean validateData() {
        return (name.length()>2
                && surname.length()>2
                && age > 18
                && Pattern.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", email)
        );
    }

    @Override
    public String toString() {
        return "StudentCreationDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", degreeCourse='" + degreeCourse + '\'' +
                '}';
    }
}
