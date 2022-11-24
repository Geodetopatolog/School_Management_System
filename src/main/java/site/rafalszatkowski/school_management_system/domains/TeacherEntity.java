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
public class TeacherEntity extends Person{



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long idTeacher;

    @Column(name = "Przedmiot")
    private String schoolSubject;

    @ManyToMany (mappedBy = "teachers")
    private Set<StudentEntity> students = new HashSet<>();

    @Override
    public String toString() {
        return "TeacherEntity{" +
                "idTeacher=" + idTeacher +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", age='" + getAge() + '\'' +
                ", schoolSubject='" + schoolSubject + '\'' +
                ", studentEntities=" + students +
                '}';
    }


    public void addStudent(StudentEntity studentEntity) {
        students.add(studentEntity);
    }

    public void removeStudent(StudentEntity studentEntity) {
        this.students.remove(studentEntity);
        studentEntity.getTeachers().remove(this);
    }
}
