package ru.akademit.jwtauth.model;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.security.Key;

@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecretDataToken {

    /**
     * Key to encrypt and decrypt token
     */
    Key secretKey;

    /**
     * Token lifetime in ms
     * example: 60 * 60 * 1000L (one hour) or 900000L (fifteen minutes)
     */
    Long liveTime;

    /**
     * The complexity of the encryption algorithm
     */
    SignatureAlgorithm signatureAlgorithm;
}
