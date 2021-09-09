package ru.akademit.jwtauth.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IncomeDataToken {

    /**
     * example:
     * [TOKEN]
     * Bearer [TOKEN]
     * etc...
     */
    String authenticationString;

    /**
     * If you needed check the prefix in {@link IncomeDataToken#authenticationString}
     *
     * Optional
     *
     * example:
     * Bearer
     * Basic
     * etc...
     */
    String authenticationPrefix;
}
