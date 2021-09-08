package ru.akademit.jwtauth.model;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecretData {

    /**
     * Phrase to encrypt and decrypt token
     */
    String secretKey;

    /**
     * Token lifetime in ms
     * example: 60L * 60L * 1000L (one hour) or 900000L (fifteen minutes)
     */
    Long liveTime;

    /**
     * The complexity of the encryption algorithm
     */
    SignatureAlgorithm signatureAlgorithm;
}
