package site.rafalszatkowski.school_management_system.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor
@Entity
public class Student extends Person {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id_student;

    @Column(name = "Kierunek")
    private String degreeCourse;

    @ManyToMany
    @JoinTable(
            name = "teachers_students_relations",
            joinColumns = @JoinColumn(name = "id_student"),
            inverseJoinColumns = @JoinColumn(name = "id_teacher")
    )
    private List<Teacher> teachers = new ArrayList<>();

    @Override
    public String toString() {
        return "Student{" +
                "id_student=" + id_student +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", age='" + getAge() + '\'' +
                ", degreeCourse='" + degreeCourse + '\'' +
                ", teachers=" + teachers +
                '}';
    }
}
