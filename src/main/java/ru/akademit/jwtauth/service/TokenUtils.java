package ru.akademit.jwtauth.service;

import io.jsonwebtoken.Jwts;
import ru.akademit.jwtauth.exceptions.GenerateTokenException;
import ru.akademit.jwtauth.model.IncomeDataToken;

import java.security.Key;
import java.util.Map;
import java.util.Objects;

class TokenUtils {
    protected static String getToken(IncomeDataToken i) {
        if (i == null || (
                i.getAuthenticationPrefix() != null && !i.getAuthenticationPrefix().isEmpty() &&
                        !i.getAuthenticationString().startsWith(i.getAuthenticationPrefix()))) {
            throw new GenerateTokenException("Bad Token");
        }

        return i.getAuthenticationPrefix() != null && !i.getAuthenticationPrefix().isEmpty()?
                i.getAuthenticationString().replace(i.getAuthenticationPrefix() + " ","") :
                i.getAuthenticationString();
    }

    protected static Map<String, Object> getClaimsMap(IncomeDataToken i, Key k) {
        if (Objects.isNull(i)) {
            throw new GenerateTokenException("Token data can not be NULL");
        }
        return Jwts.parserBuilder()
                .setSigningKey(k)
                .build()
                .parseClaimsJws(TokenUtils.getToken(i))
                .getBody();
    }
}
