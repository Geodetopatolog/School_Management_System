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
public class Teacher extends Person{



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long idTeacher;

    @Column(name = "Przedmiot")
    private String schoolSubject;

    @ManyToMany (mappedBy = "teachers")
    private Set<Student> students = new HashSet<>();

    @Override
    public String toString() {
        return "Teacher{" +
                "idTeacher=" + idTeacher +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", age='" + getAge() + '\'' +
                ", schoolSubject='" + schoolSubject + '\'' +
                ", students=" + students +
                '}';
    }


    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getTeachers().remove(this);
    }
}
