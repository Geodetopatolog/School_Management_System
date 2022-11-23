package site.rafalszatkowski.school_management_system.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
//@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public class Person {



    @Column (name = "Imię")
    private String name;

    @Column (name = "Nazwisko")
    private String surname;

    @Column (name = "Wiek")
    private Integer age;

    @Column (name = "Email")
    private String email;

}
