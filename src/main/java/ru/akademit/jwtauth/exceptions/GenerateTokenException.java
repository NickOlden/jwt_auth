package ru.akademit.jwtauth.exceptions;

public class GenerateTokenException extends RuntimeException {
    public GenerateTokenException(String errorMessage) {
        super(errorMessage);
    }
}
