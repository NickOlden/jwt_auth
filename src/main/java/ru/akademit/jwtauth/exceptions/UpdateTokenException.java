package ru.akademit.jwtauth.exceptions;

public class UpdateTokenException extends RuntimeException {
    public UpdateTokenException(String errorMessage) {
        super(errorMessage);
    }
}
