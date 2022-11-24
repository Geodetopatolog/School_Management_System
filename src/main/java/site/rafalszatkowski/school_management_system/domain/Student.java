package site.rafalszatkowski.school_management_system.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Student extends Person {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long idStudent;

    @Column(name = "Kierunek")
    private String degreeCourse;

    @ManyToMany
    @JoinTable(
            name = "teachers_students_relations",
            joinColumns = @JoinColumn(name = "idStudent"),
            inverseJoinColumns = @JoinColumn(name = "idTeacher")
    )
    private Set<Teacher> teachers = new HashSet<>();



    @Override
    public String toString() {
        return "Student{" +
                "idStudent=" + idStudent +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", age='" + getAge() + '\'' +
                ", degreeCourse='" + degreeCourse + '\'' +
                ", teachers=" + teachers +
                '}';
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public void removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getStudents().remove(this);
    }

}
