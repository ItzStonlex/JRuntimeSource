package com.itzstonlex.runtimesource;

import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

@FieldDefaults(makeFinal = true)
public class MemoryJavaSourceFileObject extends SimpleJavaFileObject {

    private String sourceCode;

    public MemoryJavaSourceFileObject(@NonNull String fileName,
                                      @NonNull String sourceCode) {

        super(URI.create("string:///" + fileName.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.sourceCode = sourceCode;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return sourceCode;
    }
}