package ru.akademit.jwtauth.service;

import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;

public interface ISecretDataConfigurator {

    Long getTokenLifetime();
    SignatureAlgorithm getAlgorithm();
    Key getSecretKey();
}
