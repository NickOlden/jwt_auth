package ru.akademit.jwtauth.service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;

public class SecretDataConfigurator implements ISecretDataConfigurator {

    @Override
    public Long getTokenLifetime() {
        return 1000L * 60 * 15;
    }

    @Override
    public SignatureAlgorithm getAlgorithm() {
        return SignatureAlgorithm.HS256;
    }

    @Override
    public Key getSecretKey() {
        return MacProvider.generateKey();
    }
}
