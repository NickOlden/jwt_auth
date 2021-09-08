package ru.akademit.jwtauth.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IncomeData {

    /**
     * example:
     * [TOKEN]
     * Bearer [TOKEN]
     * etc.
     */
    String authenticationString;

    /**
     * Optional
     * If you needed check the prefix in {@link IncomeData#authenticationString}
     */
    String authenticationPrefix;
}
