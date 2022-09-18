package com.itzstonlex.runtimesource.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public enum AccessID {

    PUBLIC("public "),

    PRIVATE("private "),

    PROTECTED("protected "),

    PACKAGE(""),
    ;

    private String name;
}
