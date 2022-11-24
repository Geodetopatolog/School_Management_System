package site.rafalszatkowski.school_management_system.validators;

import java.util.regex.Pattern;

public final class Validator {

    public static volatile Validator instance = null;

    public boolean validateName(String name) {
        return name.length()>2 && Pattern.matches("^[^0-9]+$", name);
    }

    public boolean  validateSurname(String surname) {
        return surname.length()>2  && Pattern.matches("^[^0-9]+$", surname);
    }

    public boolean validateEmail(String email) {
        return Pattern.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", email);
    }

    public boolean validateAge(Integer age) {
        return age>18  && age < 100;
    }

    public boolean validateAllData(String name, String surname, String email, Integer age) {
        return validateName(name) &&
                validateSurname(surname) &&
                validateEmail(email) &&
                validateAge(age);
    }


    private Validator() {
        if (instance != null) {
            throw new RuntimeException("Tak nie robimy, użyj sobie getInstance()");
        }
    }


    public static Validator getInstance() {
        if(instance == null) {
            synchronized (Validator.class) {
                if(instance == null) {
                    instance = new Validator();
                }
            }
        }
        return instance;
    }

}
