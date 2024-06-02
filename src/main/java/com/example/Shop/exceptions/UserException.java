package com.example.Shop.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public static class UsernameNotFound extends UserException {
        public UsernameNotFound(String message) {
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    public static class UsernameAlreadyExists extends UserException {
        public UsernameAlreadyExists(String message) {
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public static class PasswordIncorrect extends UserException {
        public PasswordIncorrect(String message) {
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public static class UserUnauthorized extends UserException {
        public UserUnauthorized(String message) {
            super(message);
        }
    }
}

