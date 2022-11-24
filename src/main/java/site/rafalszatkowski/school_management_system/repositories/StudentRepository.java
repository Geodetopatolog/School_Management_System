package site.rafalszatkowski.school_management_system.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import site.rafalszatkowski.school_management_system.domain.StudentEntity;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends CrudRepository<StudentEntity,Long>, PagingAndSortingRepository<StudentEntity, Long> {

    @Query("select s from StudentEntity s where s.name = :queryName and s.surname = :querySurname and s.email = :queryEmail and s.age = :queryAge")
    Optional<StudentEntity> getStudentByAllData(@Param("queryName") String name,
                                                @Param("querySurname") String surname,
                                                @Param("queryEmail") String email,
                                                @Param("queryAge") Integer age);

    @Query("select s from StudentEntity s where cast(s.idStudent as string) like :queryId and s.name like :queryName and s.surname like :querySurname and s.email like :queryEmail and cast(s.age as string) like :queryAge and s.degreeCourse like :queryDegreeCourse")
    Optional<List<StudentEntity>> getStudentsBySpecificData(@Param("queryId") String id,
                                                            @Param("queryName") String name,
                                                            @Param("querySurname") String surname,
                                                            @Param("queryEmail") String email,
                                                            @Param("queryAge") String age,
                                                            @Param("queryDegreeCourse") String degreeCourse);


}
