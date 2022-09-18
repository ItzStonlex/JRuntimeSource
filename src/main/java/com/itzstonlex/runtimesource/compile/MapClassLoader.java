package com.itzstonlex.runtimesource;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class MapClassLoader extends ClassLoader {

    private Map<String, byte[]> classData;

    @Override
    public Class<?> findClass(@NonNull String name) {
        return defineClass(name, classData.get(name), 0, classData.get(name).length);
    }
}