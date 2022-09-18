package com.itzstonlex.runtimesource.compile;

import com.itzstonlex.runtimesource.SourceCodeBuilder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.nio.charset.Charset;
import java.util.*;

@FieldDefaults(makeFinal = true)
public final class RuntimeCompiler {

    private static final JavaCompiler JAVA_COMPILER = ToolProvider.getSystemJavaCompiler();

    private ClassDataFileManager classDataFileManager;

    private MapClassLoader mapClassLoader;

    private List<JavaFileObject> compilationUnits = new ArrayList<>();

    private Map<String, byte[]> classData = new LinkedHashMap<>();

    public RuntimeCompiler() {
        classDataFileManager = new ClassDataFileManager(classData,
                JAVA_COMPILER.getStandardFileManager(null, Locale.getDefault(), Charset.defaultCharset()));

        mapClassLoader = new MapClassLoader(classData);
    }

    public void addClass(@NonNull SourceCodeBuilder sourceCodeBuilder) {
        addClass(sourceCodeBuilder.getClassName(), sourceCodeBuilder.build());
    }

    public void addClass(@NonNull String className, @NonNull String code) {
        compilationUnits.add(new MemoryJavaSourceFileObject(className, code));
    }

    public void addClass(@NonNull Class<?> cls) {
        compilationUnits.add(new MemoryJavaClassFileObject(classData, cls.getName()));
    }

    public void compile() {
        JavaCompiler.CompilationTask compilationTask = JAVA_COMPILER.getTask(null, classDataFileManager,
                new DiagnosticCollector<>(), null, null, compilationUnits);

        compilationTask.call();
        compilationUnits.clear();
    }

    public Class<?> getCompiledClass(@NonNull String className) {
        return mapClassLoader.findClass(className);
    }

}