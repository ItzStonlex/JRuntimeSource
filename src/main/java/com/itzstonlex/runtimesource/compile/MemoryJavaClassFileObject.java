package com.itzstonlex.runtimesource.compile;

import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import javax.tools.SimpleJavaFileObject;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

@FieldDefaults(makeFinal = true)
public class MemoryJavaClassFileObject extends SimpleJavaFileObject {

    private Map<String, byte[]> classData;

    private String className;

    public MemoryJavaClassFileObject(@NonNull Map<String, byte[]> classData, @NonNull String className) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.CLASS.extension), Kind.CLASS);

        this.classData = classData;
        this.className = className;
    }

    @Override
    public OutputStream openOutputStream() {
        return new ClassDataOutputStream(classData, className);
    }
}