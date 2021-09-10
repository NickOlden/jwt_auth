package ru.akademit.jwtauth.service;


import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.akademit.jwtauth.annotations.NotForEncrypt;
import ru.akademit.jwtauth.annotations.TokenSubject;
import ru.akademit.jwtauth.exceptions.GenerateTokenException;
import ru.akademit.jwtauth.exceptions.UpdateTokenException;
import ru.akademit.jwtauth.model.IncomeDataToken;
import ru.akademit.jwtauth.model.SecretDataToken;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainTokenService<T> {

    static Class<?> permittedType = String.class;

    SecretDataToken s;

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
        String subject = getSubject(o);

        for (Field f : clazz.getDeclaredFields()) {
            try {
                if (!f.isAnnotationPresent(NotForEncrypt.class) && !f.isAnnotationPresent(TokenSubject.class)) {
                    f.setAccessible(true);
                    claims.put(f.getName(), f.get(o));
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
                .signWith(s.getSecretKey(), s.getSignatureAlgorithm())
                .compact();
    }

    public String updateToken(IncomeDataToken i, T o) {
        if (Objects.isNull(o)) {
            throw new UpdateTokenException("The object based on which the token will be generated can not be NULL");
        }
        if (Objects.isNull(i)) {
            throw new UpdateTokenException("Token data can not be NULL");
        }

        Map<String, Object> tokenData = getDataFromToken(i);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + this.s.getLiveTime());
        String sub = tokenData.get("sub").toString();

        tokenData.put("iat", "");
        tokenData.put("exp", "");
        tokenData.put("sub", "");

        return Jwts.builder()
                .setClaims(tokenData)
                .setSubject(sub)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(s.getSecretKey(), s.getSignatureAlgorithm())
                .compact();
    }

    public boolean validate(Map<String, Object> tokenData, T o) {
        if (!tokenData.isEmpty()) {
            if (Objects.isNull(tokenData.get("exp")) || tokenData.get("exp").toString().isEmpty() ||
                    new Date(Long.parseLong(tokenData.get("exp").toString()) * 1000L)
                            .before(new Date(System.currentTimeMillis()))) {
                return false;
            }

            if (tokenData.get("sub") == getSubject(o)) {
                Class<?> clazz = o.getClass();
                for (Field f : clazz.getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        if (!tokenData.get(f.getName()).equals(f.get(o))) {
                            return false;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public Map<String, Object> getDataFromToken(IncomeDataToken i) {
        if (Objects.isNull(i)) {
            throw new GenerateTokenException("Token data can not be NULL");
        }
        return TokenUtils.getClaimsMap(i, s.getSecretKey());
    }

    private String getSubject(T o) {
        String subject;
        Class<?> clazz = o.getClass();
        List<Field> subjects = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(TokenSubject.class))
                .collect(Collectors.toUnmodifiableList());

        if (subjects.size() == 0) {
            throw new GenerateTokenException("At least one field must be marked with the TokenSubject annotation");
        }
        if (subjects.size() != 1) {
            throw new GenerateTokenException("There should be only one field marked with the TokenSubject annotation");
        }

        Field f = subjects.get(0);
        try {
            if (f.getType().isAssignableFrom(permittedType)) {
                f.setAccessible(true);
                subject = f.get(o).toString();
            } else {
                throw new GenerateTokenException("The field marked with the TokenSubject must be a String");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new GenerateTokenException("Need access to field");
        }

        return subject;
    }
}
