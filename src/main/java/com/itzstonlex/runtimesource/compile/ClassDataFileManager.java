package com.itzstonlex.runtimesource;

import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.util.Map;

@FieldDefaults(makeFinal = true)
public class ClassDataFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private Map<String, byte[]> classData;

    public ClassDataFileManager(@NonNull Map<String, byte[]> classData, @NonNull StandardJavaFileManager standardJavaFileManager) {
        super(standardJavaFileManager);
        this.classData = classData;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        return new MemoryJavaClassFileObject(classData, className);
    }
}