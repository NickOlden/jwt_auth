package ru.akademit.jwtauth.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.akademit.jwtauth.annotations.NotForEncrypt;
import ru.akademit.jwtauth.annotations.TokenSubject;
import ru.akademit.jwtauth.exceptions.GenerateTokenException;
import ru.akademit.jwtauth.model.SecretData;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetToken<T> {

    SecretData s;

    /**
     *
     * @param o - The object based on which the token will be generated
     * @return Token
     */
    public String generateToken(T o) {
        if (Objects.isNull(o)) {
            throw new GenerateTokenException("The object based on which the token will be generated can not be NULL");
        }

        Class<?> clazz = o.getClass();
        Map<String, Object> claims = new HashMap<>();
        String subject = null;
        for (Field f : clazz.getFields()) {
            try {
                f.setAccessible(true);
                if (f.isAnnotationPresent(TokenSubject.class)) {
                        subject = f.get(clazz).toString();
                }
                if (!f.isAnnotationPresent(NotForEncrypt.class) && !f.isAnnotationPresent(TokenSubject.class)) {
                    claims.put(f.getName(), f.get(clazz));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new GenerateTokenException("Need access to field");
            }
        }

        String jwts;
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + this.s.getLiveTime());
        if (Objects.nonNull(subject)) {
            jwts = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(issuedDate)
                    .setExpiration(expiredDate)
                    .signWith(Keys.secretKeyFor(this.s.getSignatureAlgorithm()), this.s.getSignatureAlgorithm())
                    .compact();
        } else {
            jwts = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(issuedDate)
                    .setExpiration(expiredDate)
                    .signWith(Keys.secretKeyFor(this.s.getSignatureAlgorithm()), this.s.getSignatureAlgorithm())
                    .compact();
        }

        return jwts;
    }
}
