package com.itzstonlex.runtimesource.compile;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ClassDataOutputStream extends OutputStream {

    private Map<String, byte[]> classData;

    private String className;

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    @Override
    public void write(int b) {
        byteArrayOutputStream.write(b);
    }

    @Override
    public void close() throws IOException {
        classData.put(className, byteArrayOutputStream.toByteArray());

        super.close();
    }
}