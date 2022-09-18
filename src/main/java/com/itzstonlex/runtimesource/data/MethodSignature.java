package com.itzstonlex.runtimesource.data;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodSignature {

    public static MethodSignature empty() {
        return with();
    }

    public static MethodSignature with(MethodParam... params) {
        return new MethodSignature(params);
    }

    private MethodParam[] params;

    private String getClassName(Class<?> cls) {
        return cls.getPackage().getName().startsWith("java.lang") ? cls.getSimpleName() : cls.getName();
    }

    @Override
    public String toString() {
        return Arrays.stream(params).map(parameter -> getClassName(parameter.getType()) + " " + parameter.getName())
                .collect(Collectors.joining(", "));
    }
}
