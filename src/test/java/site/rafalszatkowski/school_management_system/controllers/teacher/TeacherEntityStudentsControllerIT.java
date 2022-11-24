package site.rafalszatkowski.school_management_system.controllers.teacher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import site.rafalszatkowski.school_management_system.dtos.Student;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class TeacherEntityStudentsControllerIT {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    private URI createServerAddress(String suffix) throws URISyntaxException {
        return new URI("http://localhost:" + serverPort + suffix);
    }

    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    @Sql(statements =
                    "insert into teacher_entity (id, imię, nazwisko, email, wiek, przedmiot) values (1, 'ImieA', 'NazwiskoA', 'aaa@bbb.com', 22, '1');" +
                    "insert into student_entity (id, imię, nazwisko, email, wiek, kierunek) values (2, 'ImieB', 'NazwiskoB', 'aaa@bbb.com', 11, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturn2xxWhenAddStudentToTeacherSuccessfully() throws URISyntaxException {
        //when
        RequestEntity<Void> request = RequestEntity
                .patch(createServerAddress("/teacher/student?idTeacher=1&idStudent=2"))
                .build();

        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("/teacher/student?idTeacher=1"))
                .build();

        ResponseEntity<?> response = restTemplate.exchange(request, ResponseEntity.class);
        ResponseEntity<List<Student>> response2 = restTemplate.exchange(request2, new ParameterizedTypeReference<>() {});

        //then
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response2.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(1, response2.getBody().size());
    }

    @Test
    @Sql(statements =
                    "insert into teacher_entity (id, imię, nazwisko, email, wiek, przedmiot) values (9, 'ImieA', 'NazwiskoA', 'aaa@bbb.com', 22, '1');" +
                    "insert into student_entity (id, imię, nazwisko, email, wiek, kierunek) values (10, 'ImieB', 'NazwiskoB', 'aaa@bbb.com', 11, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturn4xxWhenAddStudentToTeacherUnsuccesfully() throws URISyntaxException {
        //when
        RequestEntity<Void> wrongStudent = RequestEntity
                .patch(createServerAddress("/teacher/student?idTeacher=9&idStudent=1000"))
                .build();

        RequestEntity<Void> wrongTeacher = RequestEntity
                .patch(createServerAddress("/teacher/student?idTeacher=1000&idStudent=10"))
                .build();

        RequestEntity<Void> bothWrong = RequestEntity
                .patch(createServerAddress("/teacher/student?idTeacher=1000&idStudent=1000"))
                .build();

        RequestEntity<Void> noStudentID = RequestEntity
                .patch(createServerAddress("/teacher/student?idTeacher=1000"))
                .build();

        RequestEntity<Void> noTeacherID = RequestEntity
                .patch(createServerAddress("/teacher/student?idStudent=1000"))
                .build();

        RequestEntity<Void> noIDs = RequestEntity
                .patch(createServerAddress("/teacher/student"))
                .build();

        ResponseEntity<String> response1 = restTemplate.exchange(wrongStudent, String.class);
        ResponseEntity<String> response2 = restTemplate.exchange(wrongTeacher, String.class);
        ResponseEntity<String> response3 = restTemplate.exchange(bothWrong, String.class);
        ResponseEntity<String> response4 = restTemplate.exchange(noStudentID, String.class);
        ResponseEntity<String> response5 = restTemplate.exchange(noTeacherID, String.class);
        ResponseEntity<String> response6 = restTemplate.exchange(noIDs, String.class);

        //then
        assertTrue(response1.getStatusCode().is4xxClientError());
        assertTrue(response2.getStatusCode().is4xxClientError());
        assertTrue(response3.getStatusCode().is4xxClientError());
        assertTrue(response4.getStatusCode().is4xxClientError());
        assertTrue(response5.getStatusCode().is4xxClientError());
        assertTrue(response6.getStatusCode().is4xxClientError());
        assertEquals("W bazie danych nie istnieje podany nauczyciel lub student", response1.getBody());
        assertEquals("W bazie danych nie istnieje podany nauczyciel lub student", response2.getBody());
        assertEquals("W bazie danych nie istnieje podany nauczyciel lub student", response3.getBody());
        assertEquals("Niekompletne dane", response4.getBody());
        assertEquals("Niekompletne dane", response5.getBody());
        assertEquals("Niekompletne dane", response6.getBody());

    }

    @Test
    @Sql(statements =
                    "insert into teacher_entity (id, imię, nazwisko, email, wiek, przedmiot) values (3, 'ImieA', 'NazwiskoA', 'aaa@bbb.com', 22, '1');" +
                    "insert into student_entity (id, imię, nazwisko, email, wiek, kierunek) values (4, 'ImieB', 'NazwiskoB', 'aaa@bbb.com', 11, '1');" +
                    "insert into student_entity (id, imię, nazwisko, email, wiek, kierunek) values (12, 'ImieC', 'NazwiskoC', 'aaa@bbb.com', 11, '1');" +
                    "insert into teachers_students_relations (id_student, id_teacher) values (4, 3);" +
                    "insert into teachers_students_relations (id_student, id_teacher) values (12, 3);"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturn2xxAndListOfTeacherStudentsWhenGetDataSuccessfully() throws URISyntaxException {
        //when
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("/teacher/student?idTeacher=3"))
                .build();

        ResponseEntity<List<Student>> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {});

        //then
        assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturn4xxWhenGetDataUnsuccessfully() throws URISyntaxException {
        //when
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("/teacher/student?idTeacher=1000"))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Nauczyciel nie występuje w bazie", response.getBody());
    }

    @Test
    @Sql(statements =
                    "insert into teacher_entity (id, imię, nazwisko, email, wiek, przedmiot) values (5, 'ImieA', 'NazwiskoA', 'aaa@bbb.com', 22, '1');" +
                    "insert into student_entity (id, imię, nazwisko, email, wiek, kierunek) values (6, 'ImieB', 'NazwiskoB', 'aaa@bbb.com', 11, '1');" +
                    "insert into teachers_students_relations (id_student, id_teacher) values (6, 5);"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturn2xxWhenRemoveTeachersStudentSuccessfully() throws URISyntaxException {
        //when
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("/teacher/student?idTeacher=5&idStudent=6"))
                .build();

        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("/teacher/student?idTeacher=5"))
                .build();

        ResponseEntity<?> response = restTemplate.exchange(request, ResponseEntity.class);
        ResponseEntity<List<Student>> response2 = restTemplate.exchange(request2, new ParameterizedTypeReference<>() {});

        //then
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response2.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(0, response2.getBody().size());

    }

    @Test
    @Sql(statements =
                    "insert into teacher_entity (id, imię, nazwisko, email, wiek, przedmiot) values (7, 'ImieA', 'NazwiskoA', 'aaa@bbb.com', 22, '1');" +
                    "insert into student_entity (id, imię, nazwisko, email, wiek, kierunek) values (8, 'ImieB', 'NazwiskoB', 'aaa@bbb.com', 11, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturn4xxWhenRemoveStudentFromTeacherUnsuccesfully() throws URISyntaxException {
        //when
        RequestEntity<Void> wrongStudent = RequestEntity
                .delete(createServerAddress("/teacher/student?idTeacher=7&idStudent=1000"))
                .build();

        RequestEntity<Void> wrongTeacher = RequestEntity
                .delete(createServerAddress("/teacher/student?idTeacher=1000&idStudent=8"))
                .build();

        RequestEntity<Void> bothWrong = RequestEntity
                .delete(createServerAddress("/teacher/student?idTeacher=1000&idStudent=1000"))
                .build();

        RequestEntity<Void> noStudentID = RequestEntity
                .delete(createServerAddress("/teacher/student?idTeacher=1000"))
                .build();

        RequestEntity<Void> noTeacherID = RequestEntity
                .delete(createServerAddress("/teacher/student?idStudent=1000"))
                .build();

        RequestEntity<Void> noIDs = RequestEntity
                .delete(createServerAddress("/teacher/student"))
                .build();

        ResponseEntity<String> response1 = restTemplate.exchange(wrongStudent, String.class);
        ResponseEntity<String> response2 = restTemplate.exchange(wrongTeacher, String.class);
        ResponseEntity<String> response3 = restTemplate.exchange(bothWrong, String.class);
        ResponseEntity<String> response4 = restTemplate.exchange(noStudentID, String.class);
        ResponseEntity<String> response5 = restTemplate.exchange(noTeacherID, String.class);
        ResponseEntity<String> response6 = restTemplate.exchange(noIDs, String.class);

        //then
        assertTrue(response1.getStatusCode().is4xxClientError());
        assertTrue(response2.getStatusCode().is4xxClientError());
        assertTrue(response3.getStatusCode().is4xxClientError());
        assertTrue(response4.getStatusCode().is4xxClientError());
        assertTrue(response5.getStatusCode().is4xxClientError());
        assertTrue(response6.getStatusCode().is4xxClientError());
        assertEquals("W bazie danych nie istnieje podany nauczyciel lub student", response1.getBody());
        assertEquals("W bazie danych nie istnieje podany nauczyciel lub student", response2.getBody());
        assertEquals("W bazie danych nie istnieje podany nauczyciel lub student", response3.getBody());
        assertEquals("Niekompletne dane", response4.getBody());
        assertEquals("Niekompletne dane", response5.getBody());
        assertEquals("Niekompletne dane", response6.getBody());
    }






}