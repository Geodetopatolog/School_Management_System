package site.rafalszatkowski.school_management_system.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import site.rafalszatkowski.school_management_system.domains.TeacherEntity;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends CrudRepository<TeacherEntity,Long>, PagingAndSortingRepository<TeacherEntity, Long> {

    @Query("select t from TeacherEntity t where t.name = :queryName and t.surname = :querySurname and t.email = :queryEmail and t.age = :queryAge")
    Optional<TeacherEntity> getTeacherByAllData(@Param("queryName") String name,
                                                @Param("querySurname") String surname,
                                                @Param("queryEmail") String email,
                                                @Param("queryAge") Integer age);

    @Query("select t from TeacherEntity t where cast(t.idTeacher as string) like :queryId and t.name like :queryName and t.surname like :querySurname and t.email like :queryEmail and cast(t.age as string) like :queryAge and t.schoolSubject like :querySchoolSubject")
    Optional<List<TeacherEntity>> getTeacherBySpecificData(@Param("queryId") String id,
                                                           @Param("queryName") String name,
                                                           @Param("querySurname") String surname,
                                                           @Param("queryEmail") String email,
                                                           @Param("queryAge") String age,
                                                           @Param("querySchoolSubject") String schoolSubject);


}