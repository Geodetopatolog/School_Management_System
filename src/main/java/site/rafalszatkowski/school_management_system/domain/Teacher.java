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

    private String schoolSubject;

    @ManyToMany (mappedBy = "teachers")
    private List<Student> students = new ArrayList<>();




}
