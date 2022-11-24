package site.rafalszatkowski.school_management_system.controllers.student;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.jdbc.Sql;
import site.rafalszatkowski.school_management_system.dto.StudentCreationDTO;
import site.rafalszatkowski.school_management_system.dto.StudentDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerIT {

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
    @Sql (statements = "DELETE FROM student WHERE kierunek = 'poprawny';"
                        , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn2xxWhenAddStudentSuccessfully() throws URISyntaxException {
        //given
        StudentCreationDTO studentCreationDTO = StudentCreationDTO.builder()
                .name("ImieA")
                .surname("NazwiskoA")
                .email("aaa@bbb.com")
                .age(22)
                .degreeCourse("poprawny")
                .build();

        //when
        RequestEntity<StudentCreationDTO> request = RequestEntity
                .post(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentCreationDTO);

        ResponseEntity<?> response = restTemplate.exchange(request, ResponseEntity.class);

        //then
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void shouldReturn4xxWhenDataNotValidated() throws URISyntaxException {

        //given
        StudentCreationDTO wrongName = StudentCreationDTO.builder()
                .name("I")
                .surname("NazwiskoA")
                .email("aaa@bbb.com")
                .age(22)
                .degreeCourse("kierunek1")
                .build();

        StudentCreationDTO wrongSurname = StudentCreationDTO.builder()
                .name("ImieA")
                .surname("N")
                .email("aaa@bbb.com")
                .age(22)
                .degreeCourse("kierunekA")
                .build();

        StudentCreationDTO wrongEmail = StudentCreationDTO.builder()
                .name("ImieA")
                .surname("NazwiskoA")
                .email("aaabb.com")
                .age(22)
                .degreeCourse("kierunekA")
                .build();

        StudentCreationDTO tooYoung = StudentCreationDTO.builder()
                .name("ImieA")
                .surname("NazwiskoA")
                .email("aaa@bbb.com")
                .age(15)
                .degreeCourse("kierunekA")
                .build();

        //when
        RequestEntity<StudentCreationDTO> name = RequestEntity
                .post(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(wrongName);

        RequestEntity<StudentCreationDTO> surname = RequestEntity
                .post(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(wrongSurname);

        RequestEntity<StudentCreationDTO> email = RequestEntity
                .post(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(wrongEmail);

        RequestEntity<StudentCreationDTO> age = RequestEntity
                .post(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(tooYoung);


        ResponseEntity<String> response1 = restTemplate.exchange(name, String.class);
        ResponseEntity<String> response2 = restTemplate.exchange(surname, String.class);
        ResponseEntity<String> response3 = restTemplate.exchange(email, String.class);
        ResponseEntity<String> response4 = restTemplate.exchange(age, String.class);

        //then
        assertTrue(response1.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, wiek < 18 lat lub  niepoprawny adres email", response1.getBody());
        assertTrue(response2.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, wiek < 18 lat lub  niepoprawny adres email", response2.getBody());
        assertTrue(response3.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, wiek < 18 lat lub  niepoprawny adres email", response3.getBody());
        assertTrue(response4.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, wiek < 18 lat lub  niepoprawny adres email", response4.getBody());
    }

    @Test
    @Sql (statements = "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (1, 'ImieA', 'NazwiskoA', 'aaa@bbb.com', 22, 'powtorka')"
                        , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql (statements = "DELETE FROM student WHERE kierunek = 'powtorka';"
                        , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn4xxWhenStudentAlreadyExists() throws URISyntaxException {
        //given
        StudentCreationDTO studentCreationDTO = StudentCreationDTO.builder()
                .name("ImieA")
                .surname("NazwiskoA")
                .email("aaa@bbb.com")
                .age(22)
                .degreeCourse("powtorka")
                .build();

        //when
        RequestEntity<StudentCreationDTO> request = RequestEntity
                .post(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentCreationDTO);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    @Sql (statements =
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (1, 'ImieA', 'NazwiskoA', '111@bbb.com', 22, '1');" +
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (2, 'ImieA', 'NazwiskoA', '222@bbb.com', 22, '2');" +
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (3, 'ImieB', 'NazwiskoA', '111@bbb.com', 22, '3');" +
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (4, 'ImieC', 'NazwiskoB', '222@bbb.com', 22, '1');" +
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (5, 'ImieD', 'NazwiskoC', '111@bbb.com', 32, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql (statements =
                    "DELETE FROM student WHERE id = '1';" +
                    "DELETE FROM student WHERE id = '2';" +
                    "DELETE FROM student WHERE id = '3';" +
                    "DELETE FROM student WHERE id = '4';" +
                    "DELETE FROM student WHERE id = '5';"
                    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn2xxAndAllMatchedStudentsWhenQuerySuccessful() throws URISyntaxException {

        //when
        RequestEntity<Void> request1 = RequestEntity
                .get(createServerAddress("/student/query?id=4"))
                .build();
        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("/student/query?name=ImieA"))
                .build();
        RequestEntity<Void> request3 = RequestEntity
                .get(createServerAddress("/student/query?surname=NazwiskoA"))
                .build();
        RequestEntity<Void> request4 = RequestEntity
                .get(createServerAddress("/student/query?email=111@bbb.com"))
                .build();
        RequestEntity<Void> request5 = RequestEntity
                .get(createServerAddress("/student/query?age=22"))
                .build();
        RequestEntity<Void> request6 = RequestEntity
                .get(createServerAddress("/student/query?degreeCourse=1"))
                .build();
        RequestEntity<Void> request7 = RequestEntity
                .get(createServerAddress("/student/query?name=ImieA&surname=NazwiskoA"))
                .build();
        RequestEntity<Void> request8 = RequestEntity
                .get(createServerAddress("/student/query?age=22&email=222@bbb.com"))
                .build();

        ResponseEntity<List<StudentDTO>> response1 = restTemplate.exchange(request1, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<StudentDTO>> response2 = restTemplate.exchange(request2, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<StudentDTO>> response3 = restTemplate.exchange(request3, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<StudentDTO>> response4 = restTemplate.exchange(request4, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<StudentDTO>> response5 = restTemplate.exchange(request5, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<StudentDTO>> response6 = restTemplate.exchange(request6, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<StudentDTO>> response7 = restTemplate.exchange(request7, new ParameterizedTypeReference<>(){});
        ResponseEntity<List<StudentDTO>> response8 = restTemplate.exchange(request8, new ParameterizedTypeReference<>(){});

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
    @Sql (statements =
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (1, 'ImieA', 'NazwiskoA', '111@bbb.com', 22, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql (statements =
                    "DELETE FROM student WHERE id = '1';"
                    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn4xxWhenStudentNotFound() throws URISyntaxException {
        //when
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("/student/query?id=1000"))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then

        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Nie znaleziono wpisów odpowiadających wyszukiwaniu", response.getBody());
    }

    @Test
    @Sql (statements =
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (1, 'ImieA', 'NazwiskoA', '111@bbb.com', 22, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql (statements =
                    "DELETE FROM student WHERE id = '1';"
                    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn4xxWhenQueryParametersNotProvided() throws URISyntaxException {

        //when
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("/student/query"))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Nie wprowadzono parametrów wyszukiwania", response.getBody());
    }

    @Test
    @Sql (statements =
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (1, 'ImieA', 'NazwiskoA', '111@bbb.com', 22, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql (statements =
                    "DELETE FROM student WHERE kierunek = '1';"
                    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn2xxWhenUpdateStudentSuccessfully() throws URISyntaxException {

        //given
        StudentDTO studentDTO = StudentDTO.builder()
                .idStudent(1L)
                .name("ImieB")
                .surname("NazwiskoB")
                .email("aaa@bbb.com")
                .age(22)
                .degreeCourse("1")
                .numberOfTeachers(0)
                .build();

        //when
        RequestEntity<StudentDTO> request1 = RequestEntity
                .patch(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentDTO);

        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("/student/query?id=1"))
                .build();

        ResponseEntity<?> response1 = restTemplate.exchange(request1, ResponseEntity.class);
        ResponseEntity<List<StudentDTO>> response2 = restTemplate.exchange(request2, new ParameterizedTypeReference<>(){});

        //then
        assertTrue(response1.getStatusCode().is2xxSuccessful());
        assertTrue(response2.getStatusCode().is2xxSuccessful());
        assertEquals(1, response2.getBody().size());
        assertEquals(response2.getBody().get(0), studentDTO);
    }

    @Test
    void shouldReturn4xxWhenUpdateStudentNotExists() throws URISyntaxException {

        //given
        StudentDTO studentDTO = StudentDTO.builder()
                .idStudent(1L)
                .name("ImieB")
                .surname("NazwiskoB")
                .email("aaa@bbb.com")
                .age(22)
                .degreeCourse("1")
                .build();

        //when
        RequestEntity<StudentDTO> request = RequestEntity
                .patch(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentDTO);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Student, którego dane próbowano uaktualnić, nie występuje w bazie danych", response.getBody());
    }

    @Test
    void shouldReturn4xxWhenUpdateStudentWithUnvalidatedData() throws URISyntaxException {

        //given
        StudentDTO name = StudentDTO.builder()
                .idStudent(1L)
                .name("I")
                .surname("NazwiskoB")
                .email("aaa@bbb.com")
                .age(22)
                .degreeCourse("1")
                .build();

        StudentDTO surname = StudentDTO.builder()
                .idStudent(1L)
                .name("ImieA")
                .surname("N")
                .email("aaa@bbb.com")
                .age(22)
                .degreeCourse("1")
                .build();

        StudentDTO email = StudentDTO.builder()
                .idStudent(1L)
                .name("ImieA")
                .surname("NazwiskoB")
                .email("aaabbb.com")
                .age(22)
                .degreeCourse("1")
                .build();

        StudentDTO age = StudentDTO.builder()
                .idStudent(1L)
                .name("ImieA")
                .surname("NazwiskoB")
                .email("aaa@bbb.com")
                .age(11)
                .degreeCourse("1")
                .build();

        //when
        RequestEntity<StudentDTO> request1 = RequestEntity
                .patch(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(name);

        RequestEntity<StudentDTO> request2 = RequestEntity
                .patch(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(surname);

        RequestEntity<StudentDTO> request3 = RequestEntity
                .patch(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(email);

        RequestEntity<StudentDTO> request4 = RequestEntity
                .patch(createServerAddress("/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(age);

        ResponseEntity<String> response1 = restTemplate.exchange(request1, String.class);
        ResponseEntity<String> response2 = restTemplate.exchange(request2, String.class);
        ResponseEntity<String> response3 = restTemplate.exchange(request3, String.class);
        ResponseEntity<String> response4 = restTemplate.exchange(request4, String.class);

        //then
        assertTrue(response1.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, wiek < 18 lat lub  niepoprawny adres email", response1.getBody());
        assertTrue(response2.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, wiek < 18 lat lub  niepoprawny adres email", response2.getBody());
        assertTrue(response3.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, wiek < 18 lat lub  niepoprawny adres email", response3.getBody());
        assertTrue(response4.getStatusCode().is4xxClientError());
        assertEquals("Niepoprawne dane: Imię i Nazwisko krótsze niż 2 litery, wiek < 18 lat lub  niepoprawny adres email", response4.getBody());
    }

    @Test
    @Sql (statements =
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (1, 'ImieA', 'NazwiskoA', '111@bbb.com', 22, '1');"
                    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturn2xxWhenDeleteStudentSuccessfully() throws URISyntaxException {

        //when
        RequestEntity<Void> request1 = RequestEntity
                .delete(createServerAddress("/student?id=1"))
                .build();

        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("/student/query?id=1"))
                .build();

        ResponseEntity<?> response1 = restTemplate.exchange(request1, ResponseEntity.class);
        ResponseEntity<String> response2 = restTemplate.exchange(request2, String.class);

        //then
        assertTrue(response1.getStatusCode().is2xxSuccessful());

        assertTrue(response2.getStatusCode().is4xxClientError());
        assertEquals("Nie znaleziono wpisów odpowiadających wyszukiwaniu", response2.getBody());
    }

    @Test
    void shouldReturn4xxWhenDeleteStudentNotExists() throws URISyntaxException {

        //when
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("/student?id=1"))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Student, którego dane próbowano usunąć, nie występuje w bazie danych", response.getBody());
    }

    @Test
    @Sql (statements =
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (1, 'ImieB', 'NazwiskoB', 'aaa@bbb.com', 21, '1');" +
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (2, 'ImieA', 'NazwiskoA', 'aaa@bbb.com', 22, '2');" +
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (3, 'ImieC', 'NazwiskoC', 'aaa@bbb.com', 23, '2');" +
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (4, 'ImieD', 'NazwiskoD', 'aaa@bbb.com', 24, '2');" +
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (5, 'ImieE', 'NazwiskoE', 'aaa@bbb.com', 25, '2');" +
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (6, 'ImieF', 'NazwiskoF', 'aaa@bbb.com', 26, '2');" +
                    "insert into student (id, imię, nazwisko, email, wiek, kierunek) values (7, 'ImieG', 'NazwiskoG', 'aaa@bbb.com', 27, '3');"
            , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql (statements =
                    "DELETE FROM student WHERE id = 1;" +
                    "DELETE FROM student WHERE id = 2;" +
                    "DELETE FROM student WHERE id = 3;" +
                    "DELETE FROM student WHERE id = 4;" +
                    "DELETE FROM student WHERE id = 5;" +
                    "DELETE FROM student WHERE id = 6;" +
                    "DELETE FROM student WHERE id = 7;"
            , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturn2xxPageOfStudents() throws URISyntaxException {

        //when
        RequestEntity<Void> request1 = RequestEntity
                .get(createServerAddress("/student/all?page=0"))
                .build();

        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("/student/all?page=0&sortBy=name"))
                .build();

        RequestEntity<Void> request3 = RequestEntity
                .get(createServerAddress("/student/all?page=0&sortBy=name&descending=true"))
                .build();

        RequestEntity<Void> request4 = RequestEntity
                .get(createServerAddress("/student/all?sortBy=age"))
                .build();

        RequestEntity<Void> request5 = RequestEntity
                .get(createServerAddress("/student/all?sortBy=age&descending=true"))
                .build();

        ResponseEntity<List<StudentDTO>> response1 = restTemplate.exchange(request1, new ParameterizedTypeReference<>() {});
        ResponseEntity<List<StudentDTO>> response2 = restTemplate.exchange(request2, new ParameterizedTypeReference<>() {});
        ResponseEntity<List<StudentDTO>> response3 = restTemplate.exchange(request3, new ParameterizedTypeReference<>() {});
        ResponseEntity<List<StudentDTO>> response4 = restTemplate.exchange(request4, new ParameterizedTypeReference<>() {});
        ResponseEntity<List<StudentDTO>> response5 = restTemplate.exchange(request5, new ParameterizedTypeReference<>() {});

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