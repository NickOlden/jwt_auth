package ru.akademit.jwtauth.service;

import ru.akademit.jwtauth.exceptions.GenerateTokenException;
import ru.akademit.jwtauth.model.IncomeDataToken;

public class TokenUtils {
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
}
