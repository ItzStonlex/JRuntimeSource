package com.itzstonlex.runtimesource.data;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@ToString
@Getter
@Value(staticConstructor = "create")
public class MethodParam {

    Class<?> type;

    String name;
}
