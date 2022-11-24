package site.rafalszatkowski.school_management_system.components;

import org.junit.jupiter.api.Test;
import site.rafalszatkowski.school_management_system.validators.Validator;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    Validator instance = Validator.getInstance();

    @Test
    void validateName() {

        assertTrue(instance.validateName("Aaaaa"));
        assertTrue(instance.validateName("Aaa"));
        assertTrue(instance.validateName("Aaaaaaaaaa"));
        assertFalse(instance.validateName("A"));
        assertFalse(instance.validateName("Aa"));
        assertFalse(instance.validateName(""));
        assertFalse(instance.validateName("Aa11"));
        assertFalse(instance.validateName("1111"));
    }

    @Test
    void validateSurname() {

        assertTrue(instance.validateName("Aaaaa"));
        assertTrue(instance.validateName("Aaa"));
        assertTrue(instance.validateName("Aaaaaaaaa"));
        assertFalse(instance.validateName("A"));
        assertFalse(instance.validateName("Aa"));
        assertFalse(instance.validateName(""));
        assertFalse(instance.validateName("Aa11"));
        assertFalse(instance.validateName("1111"));
    }

    @Test
    void validateEmail() {

        assertTrue(instance.validateEmail("aaa@bbb.ccc"));
        assertTrue(instance.validateEmail("a@b.c"));
        assertFalse(instance.validateEmail("aaabbb.ccc"));
        assertFalse(instance.validateEmail("@bbb.ccc"));
        assertFalse(instance.validateEmail("aaa@"));
    }

    @Test
    void validateAge() {

        assertTrue(instance.validateAge(19));
        assertTrue(instance.validateAge(99));
        assertFalse(instance.validateAge(100));
        assertFalse(instance.validateAge(1));
    }

    @Test
    void validateAllData() {

        assertTrue(instance.validateAllData("Rafał", "Szatkowski", "rafalszatkowski1992@gmail.com", 29));
        assertFalse(instance.validateAllData("Ra", "Szatkowski", "rafalszatkowski1992@gmail.com", 29));
        assertFalse(instance.validateAllData("Rafał", "Sz1", "rafalszatkowski1992@gmail.com", 29));
        assertFalse(instance.validateAllData("Rafał", "Szatkowski", "rafalszatkowski1992gmail.com", 29));
        assertFalse(instance.validateAllData("Rafał", "Szatkowski", "rafalszatkowski1992gmail.com", 152));
    }

    @Test
    void getInstance() {
        assertEquals(instance, Validator.getInstance());
    }
}