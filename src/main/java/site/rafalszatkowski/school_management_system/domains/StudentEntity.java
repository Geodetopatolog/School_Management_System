package site.rafalszatkowski.school_management_system.domains;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class StudentEntity extends Person {


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
    private Set<TeacherEntity> teachers = new HashSet<>();



    @Override
    public String toString() {
        return "StudentEntity{" +
                "idStudent=" + idStudent +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", age='" + getAge() + '\'' +
                ", degreeCourse='" + degreeCourse + '\'' +
                ", teacherEntities=" + teachers +
                '}';
    }

    public void addTeacher(TeacherEntity teacherEntity) {
        teachers.add(teacherEntity);
    }

    public void removeTeacher(TeacherEntity teacherEntity) {
        this.teachers.remove(teacherEntity);
        teacherEntity.getStudents().remove(this);
    }

}
