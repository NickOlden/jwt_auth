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
import java.util.*;
import java.util.stream.Collectors;

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
        List<Field> subjects = Arrays.stream(clazz.getFields())
                .filter(f -> f.isAnnotationPresent(TokenSubject.class))
                .collect(Collectors.toUnmodifiableList());

        if (subjects.size() == 0) {
            throw new NullPointerException("At least one field must be marked with the TokenSubject annotation");
        }
        if (subjects.size() != 1) {
            throw new GenerateTokenException("There should be only one field marked with the TokenSubject annotation");
        }

        String subject = null;

        for (Field f : clazz.getFields()) {
            try {
                f.setAccessible(true);
                if (f.isAnnotationPresent(TokenSubject.class)) {
                    Class<?> permittedType = String.class;
                    if (f.getType().isAssignableFrom(permittedType)) {
                        subject = f.get(clazz).toString();
                    } else {
                        throw new GenerateTokenException("The field marked with the TokenSubject must be a String");
                    }
                }
                if (!f.isAnnotationPresent(NotForEncrypt.class) && !f.isAnnotationPresent(TokenSubject.class)) {
                    claims.put(f.getName(), f.get(clazz));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new GenerateTokenException("Need access to field");
            }
        }

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + this.s.getLiveTime());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(Keys.secretKeyFor(this.s.getSignatureAlgorithm()), this.s.getSignatureAlgorithm())
                .compact();
    }
}
