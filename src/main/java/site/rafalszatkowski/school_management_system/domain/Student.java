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
    private Long id_student;

    @Column(name = "Kierunek")
    private String degreeCourse;

    @ManyToMany
    @JoinTable(
            name = "teachers_students_relations",
            joinColumns = @JoinColumn(name = "id_student"),
            inverseJoinColumns = @JoinColumn(name = "id_teacher")
    )
    private Set<Teacher> teachers = new HashSet<>();



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

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public void removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getStudents().remove(this);
    }

}
