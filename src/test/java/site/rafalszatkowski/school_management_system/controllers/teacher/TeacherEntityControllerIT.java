package site.rafalszatkowski.school_management_system.controllers.teacher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.jdbc.Sql;
import site.rafalszatkowski.school_management_system.dtos.Teacher;
import site.rafalszatkowski.school_management_system.dtos.TeacherCreation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeacherEntityControllerIT {

    @AfterEach
    void tearDown() {
    }
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
                    "DELETE FROM teacher_entity WHERE przedmiot = 'poprawny';"
                    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn2xxWhenAddTeacherSuccessfully() throws URISyntaxException {

        //given
        TeacherCreation teacherCreation = TeacherCreation.builder()
                .name("ImieA")
                .surname("NazwiskoA")
                .email("aaa@bbb.com")
                .age(22)
                .schoolSubject("poprawny")
                .build();

        //when
        RequestEntity<TeacherCreation> request = RequestEntity
                .post(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(teacherCreation);

        ResponseEntity<?> response = restTemplate.exchange(request, ResponseEntity.class);

        //then
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void shouldReturn4xxWhenDataNotValidated() throws URISyntaxException {

        //given
        TeacherCreation wrongName = TeacherCreation.builder()
                .name("I")
                .surname("NazwiskoA")
                .email("aaa@bbb.com")
                .age(22)
                .schoolSubject("kierunekA")
                .build();

        TeacherCreation wrongSurname = TeacherCreation.builder()
                .name("ImieA")
                .surname("N")
                .email("aaa@bbb.com")
                .age(22)
                .schoolSubject("kierunekA")
                .build();

        TeacherCreation wrongEmail = TeacherCreation.builder()
                .name("ImieA")
                .surname("NazwiskoA")
                .email("aaabbb.com")
                .age(22)
                .schoolSubject("kierunekA")
                .build();

        TeacherCreation wrongAge = TeacherCreation.builder()
                .name("ImieA")
                .surname("NazwiskoA")
                .email("aaa@bbb.com")
                .age(11)
                .schoolSubject("kierunekA")
                .build();

        //when
        RequestEntity<TeacherCreation> name = RequestEntity
                .post(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(wrongName);

        RequestEntity<TeacherCreation> surname = RequestEntity
                .post(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(wrongSurname);

        RequestEntity<TeacherCreation> email = RequestEntity
                .post(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(wrongEmail);

        RequestEntity<TeacherCreation> age = RequestEntity
                .post(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(wrongAge);

        ResponseEntity<String> response1 = restTemplate.exchange(name, String.class);
        ResponseEntity<String> response2 = restTemplate.exchange(surname, String.class);
        ResponseEntity<String> response3 = restTemplate.exchange(email, String.class);
        ResponseEntity<String> response4 = restTemplate.exchange(age, String.class);

        //then
        assertTrue(response1.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imi?? i Nazwisko kr??tsze ni?? 2 litery, wiek < 18 lat lub  niepoprawny adres email", response1.getBody());
        assertTrue(response2.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imi?? i Nazwisko kr??tsze ni?? 2 litery, wiek < 18 lat lub  niepoprawny adres email", response2.getBody());
        assertTrue(response3.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imi?? i Nazwisko kr??tsze ni?? 2 litery, wiek < 18 lat lub  niepoprawny adres email", response3.getBody());
        assertTrue(response4.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imi?? i Nazwisko kr??tsze ni?? 2 litery, wiek < 18 lat lub  niepoprawny adres email", response4.getBody());
    }

    @Test
    @Sql (statements =
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (1, 'ImieA', 'NazwiskoA', 'aaa@bbb.com', 22, 'powtorka')"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql (statements =
                    "DELETE FROM teacher_entity WHERE przedmiot = 'powtorka';"
                    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn4xxWhenTeacherAlreadyExists() throws URISyntaxException {

        //given
        TeacherCreation teacherCreation = TeacherCreation.builder()
                .name("ImieA")
                .surname("NazwiskoA")
                .email("aaa@bbb.com")
                .age(22)
                .schoolSubject("powtorka")
                .build();

        //when
        RequestEntity<TeacherCreation> request = RequestEntity
                .post(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(teacherCreation);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Nauczyciel ju?? istnieje w bazie", response.getBody());
    }

    @Test
    @Sql (statements =
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (1, 'ImieA', 'NazwiskoA', '111@bbb.com', 22, '1');" +
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (2, 'ImieA', 'NazwiskoA', '222@bbb.com', 22, '2');" +
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (3, 'ImieB', 'NazwiskoA', '111@bbb.com', 22, '3');" +
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (4, 'ImieC', 'NazwiskoB', '222@bbb.com', 22, '1');" +
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (5, 'ImieD', 'NazwiskoC', '111@bbb.com', 32, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql (statements =
                    "DELETE FROM teacher_entity WHERE id = '1';" +
                    "DELETE FROM teacher_entity WHERE id = '2';" +
                    "DELETE FROM teacher_entity WHERE id = '3';" +
                    "DELETE FROM teacher_entity WHERE id = '4';" +
                    "DELETE FROM teacher_entity WHERE id = '5';"
                    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn2xxAndAllMatchedTeachersWhenQuerySuccessful() throws URISyntaxException {

        //when
        RequestEntity<Void> request1 = RequestEntity
                .get(createServerAddress("/teacher/query?id=4"))
                .build();
        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("/teacher/query?name=ImieA"))
                .build();
        RequestEntity<Void> request3 = RequestEntity
                .get(createServerAddress("/teacher/query?surname=NazwiskoA"))
                .build();
        RequestEntity<Void> request4 = RequestEntity
                .get(createServerAddress("/teacher/query?email=111@bbb.com"))
                .build();
        RequestEntity<Void> request5 = RequestEntity
                .get(createServerAddress("/teacher/query?age=22"))
                .build();
        RequestEntity<Void> request6 = RequestEntity
                .get(createServerAddress("/teacher/query?schoolSubject=1"))
                .build();
        RequestEntity<Void> request7 = RequestEntity
                .get(createServerAddress("/teacher/query?name=ImieA&surname=NazwiskoA"))
                .build();
        RequestEntity<Void> request8 = RequestEntity
                .get(createServerAddress("/teacher/query?age=22&email=222@bbb.com"))
                .build();

        ResponseEntity<List<Teacher>> response1 = restTemplate.exchange(request1, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<Teacher>> response2 = restTemplate.exchange(request2, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<Teacher>> response3 = restTemplate.exchange(request3, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<Teacher>> response4 = restTemplate.exchange(request4, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<Teacher>> response5 = restTemplate.exchange(request5, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<Teacher>> response6 = restTemplate.exchange(request6, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<Teacher>> response7 = restTemplate.exchange(request7, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<Teacher>> response8 = restTemplate.exchange(request8, new ParameterizedTypeReference<>(){});

        //then:
        Assertions.assertTrue(response1.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(1, response1.getBody().size());
        Assertions.assertTrue(response2.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(2, response2.getBody().size());
        Assertions.assertTrue(response3.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(3, response3.getBody().size());
        Assertions.assertTrue(response4.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(3, response4.getBody().size());
        Assertions.assertTrue(response5.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(4, response5.getBody().size());
        Assertions.assertTrue(response6.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(3, response6.getBody().size());
        Assertions.assertTrue(response7.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(2, response7.getBody().size());
        Assertions.assertTrue(response8.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(2, response8.getBody().size());
    }

    @Test
    void shouldReturn4xxWhenTeacherNotFound() throws URISyntaxException {

        //given
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("/teacher/query?id=1000"))
                .build();

        //when
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Nie znaleziono wpis??w odpowiadaj??cych wyszukiwaniu", response.getBody());
    }

    @Test
    void shouldReturn4xxWhenQueryParametersNotProvided() throws URISyntaxException {

        //given
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("/teacher/query"))
                .build();

        //when
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Nie wprowadzono parametr??w wyszukiwania", response.getBody());
    }

    @Test
    @Sql (statements =
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (1, 'ImieA', 'NazwiskoA', '111@bbb.com', 22, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql (statements =
                    "DELETE FROM teacher_entity WHERE id = 1;"
                    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn2xxWhenUpdateTeacherSuccessfully() throws URISyntaxException {

        //given
        Teacher teacher = Teacher.builder()
                .idTeacher(1L)
                .name("ImieB")
                .surname("NazwiskoB")
                .email("aaa@bbb.com")
                .age(22)
                .schoolSubject("1")
                .numberOfStudents(0)
                .build();

        System.out.println(teacher);

        //when
        RequestEntity<Teacher> request1 = RequestEntity
                .patch(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(teacher);

        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("/teacher/query?id=1"))
                .build();

        ResponseEntity<?> response1 = restTemplate.exchange(request1, ResponseEntity.class);
        ResponseEntity<List<Teacher>> response2 = restTemplate.exchange(request2, new ParameterizedTypeReference<>(){});

        //then
        assertTrue(response1.getStatusCode().is2xxSuccessful());
        assertTrue(response2.getStatusCode().is2xxSuccessful());
        assertEquals(1, response2.getBody().size());
        assertEquals(response2.getBody().get(0), teacher);
    }

    @Test
    void shouldReturn4xxWhenUpdateTeacherNotExists() throws URISyntaxException {

        //given
        Teacher teacher = Teacher.builder()
                .idTeacher(1L)
                .name("ImieB")
                .surname("NazwiskoB")
                .email("aaa@bbb.com")
                .age(22)
                .schoolSubject("1")
                .build();

        //when
        RequestEntity<Teacher> request = RequestEntity
                .patch(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(teacher);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Nauczyciel, kt??rego dane pr??bowano uaktualni??, nie wyst??puje w bazie danych", response.getBody());
    }

    @Test
    void shouldReturn4xxWhenUpdateTeacherWithUnvalidatedData() throws URISyntaxException {

        //given
        Teacher name = Teacher.builder()
                .idTeacher(1L)
                .name("I")
                .surname("NazwiskoA")
                .email("aaa@bbb.com")
                .age(22)
                .schoolSubject("1")
                .build();

        Teacher surname = Teacher.builder()
                .idTeacher(1L)
                .name("ImieA")
                .surname("N")
                .email("aaa@bbb.com")
                .age(22)
                .surname("1")
                .build();

        Teacher email = Teacher.builder()
                .idTeacher(1L)
                .name("ImieA")
                .surname("NazwiskoB")
                .email("aaabbb.com")
                .age(22)
                .schoolSubject("1")
                .build();

        Teacher age = Teacher.builder()
                .idTeacher(1L)
                .name("ImieA")
                .surname("NazwiskoB")
                .email("aaa@bbb.com")
                .age(11)
                .schoolSubject("1")
                .build();

        //when
        RequestEntity<Teacher> request1 = RequestEntity
                .patch(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(name);

        RequestEntity<Teacher> request2 = RequestEntity
                .patch(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(surname);

        RequestEntity<Teacher> request3 = RequestEntity
                .patch(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(email);

        RequestEntity<Teacher> request4 = RequestEntity
                .patch(createServerAddress("/teacher"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(age);

        ResponseEntity<String> response1 = restTemplate.exchange(request1, String.class);
        ResponseEntity<String> response2 = restTemplate.exchange(request2, String.class);
        ResponseEntity<String> response3 = restTemplate.exchange(request3, String.class);
        ResponseEntity<String> response4 = restTemplate.exchange(request4, String.class);

        //then
        assertTrue(response1.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imi?? i Nazwisko kr??tsze ni?? 2 litery, wiek < 18 lat lub  niepoprawny adres email", response1.getBody());
        assertTrue(response2.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imi?? i Nazwisko kr??tsze ni?? 2 litery, wiek < 18 lat lub  niepoprawny adres email", response2.getBody());
        assertTrue(response3.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imi?? i Nazwisko kr??tsze ni?? 2 litery, wiek < 18 lat lub  niepoprawny adres email", response3.getBody());
        assertTrue(response4.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imi?? i Nazwisko kr??tsze ni?? 2 litery, wiek < 18 lat lub  niepoprawny adres email", response4.getBody());
    }

    @Test
    @Sql (statements =
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (1, 'ImieA', 'NazwiskoA', '111@bbb.com', 22, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturn2xxWhenDeleteTeacherSuccessfully() throws URISyntaxException {

        //when
        RequestEntity<Void> request1 = RequestEntity
                .delete(createServerAddress("/teacher?id=1"))
                .build();

        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("/teacher/query?id=1"))
                .build();

        ResponseEntity<?> response1 = restTemplate.exchange(request1, ResponseEntity.class);
        ResponseEntity<String> response2 = restTemplate.exchange(request2, String.class);

        //then
        assertTrue(response1.getStatusCode().is2xxSuccessful());
        assertTrue(response2.getStatusCode().is4xxClientError());
        assertEquals("Nie znaleziono wpis??w odpowiadaj??cych wyszukiwaniu", response2.getBody());
    }

    @Test
    void shouldReturn4xxWhenDeleteTeacherNotExists() throws URISyntaxException {

        //when
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("/teacher?id=1"))
                .build();


        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Nauczyciel, kt??rego dane pr??bowano usun????, nie wyst??puje w bazie danych", response.getBody());
    }

    @Test
    @Sql (statements =
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (1, 'ImieC', 'NazwiskoC', 'aaa@bbb.com', 22, '1');" +
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (2, 'ImieB', 'NazwiskoB', 'aaa@bbb.com', 21, '2');" +
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (3, 'ImieA', 'NazwiskoA', 'aaa@bbb.com', 23, '2');" +
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (4, 'ImieD', 'NazwiskoD', 'aaa@bbb.com', 25, '2');" +
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (5, 'ImieE', 'NazwiskoE', 'aaa@bbb.com', 27, '2');" +
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (6, 'ImieF', 'NazwiskoF', 'aaa@bbb.com', 24, '2');" +
                    "insert into teacher_entity (id, imi??, nazwisko, email, wiek, przedmiot) values (7, 'ImieG', 'NazwiskoG', 'aaa@bbb.com', 26, '3');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql (statements =
                    "DELETE FROM teacher_entity WHERE id = 1;" +
                    "DELETE FROM teacher_entity WHERE id = 2;" +
                    "DELETE FROM teacher_entity WHERE id = 3;" +
                    "DELETE FROM teacher_entity WHERE id = 4;" +
                    "DELETE FROM teacher_entity WHERE id = 5;" +
                    "DELETE FROM teacher_entity WHERE id = 6;" +
                    "DELETE FROM teacher_entity WHERE id = 7;"
                    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnPageOfTeachers() throws URISyntaxException {

        //when
        RequestEntity<Void> request1 = RequestEntity
                .get(createServerAddress("/teacher/all?page=0"))
                .build();

        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("/teacher/all?page=0&sortBy=name"))
                .build();

        RequestEntity<Void> request3 = RequestEntity
                .get(createServerAddress("/teacher/all?page=0&sortBy=name&descending=true"))
                .build();

        RequestEntity<Void> request4 = RequestEntity
                .get(createServerAddress("/teacher/all?sortBy=age"))
                .build();

        RequestEntity<Void> request5 = RequestEntity
                .get(createServerAddress("/teacher/all?sortBy=age&descending=true"))
                .build();

        ResponseEntity<List<Teacher>> response1 = restTemplate.exchange(request1, new ParameterizedTypeReference<>() {});
        ResponseEntity<List<Teacher>> response2 = restTemplate.exchange(request2, new ParameterizedTypeReference<>() {});
        ResponseEntity<List<Teacher>> response3 = restTemplate.exchange(request3, new ParameterizedTypeReference<>() {});
        ResponseEntity<List<Teacher>> response4 = restTemplate.exchange(request4, new ParameterizedTypeReference<>() {});
        ResponseEntity<List<Teacher>> response5 = restTemplate.exchange(request5, new ParameterizedTypeReference<>() {});

        System.out.println(response1.getBody());

        //then
        Assertions.assertTrue(response1.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(5, response1.getBody().size());

        Assertions.assertTrue(response2.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(5, response2.getBody().size());
        assertEquals("ImieA", response2.getBody().get(0).getName());

        Assertions.assertTrue(response3.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(5, response3.getBody().size());
        assertEquals("ImieG", response3.getBody().get(0).getName());

        Assertions.assertTrue(response4.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(7, response4.getBody().size());
        assertEquals(21, response4.getBody().get(0).getAge());

        Assertions.assertTrue(response5.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(7, response5.getBody().size());
        assertEquals(27, response5.getBody().get(0).getAge());
    }





}