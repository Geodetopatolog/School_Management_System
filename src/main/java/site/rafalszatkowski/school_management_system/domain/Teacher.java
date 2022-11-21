package site.rafalszatkowski.school_management_system.domain;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor
@Entity
public class Teacher extends Person{



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id_teacher;

    @Column(name = "Przedmiot")
    private String schoolSubject;

    @ManyToMany (mappedBy = "teachers")
    private List<Student> students = new ArrayList<>();

    @Override
    public String toString() {
        return "Teacher{" +
                "id_student=" + id_teacher +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", age='" + getAge() + '\'' +
                ", degreeCourse='" + schoolSubject + '\'' +
                ", teachers=" + students +
                '}';
    }


}
