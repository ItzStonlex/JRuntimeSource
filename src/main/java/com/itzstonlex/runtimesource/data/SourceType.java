package com.itzstonlex.runtimesource.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public enum SourceType {

    CLASS("class "),

    ENUM("enum "),

    INTERFACE("interface "),

    ABSTRACT_CLASS("abstract class "),
    ;

    private String name;
}
