package site.rafalszatkowski.school_management_system.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import site.rafalszatkowski.school_management_system.datatransfer.dtos.StudentCreationDTO;

import java.net.URI;
import java.net.URISyntaxException;

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

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldReturn2xxWhenAddStudentSuccessfully() throws URISyntaxException {
        //given
        StudentCreationDTO studentCreationDTO = StudentCreationDTO.builder()
                .name("Imie1")
                .surname("Nazwisko1")
                .email("aaa@bbb.com")
                .age(22)
                .degreeCourse("kierunek1")
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
}