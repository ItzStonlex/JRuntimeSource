package com.itzstonlex.runtimesource;

import com.itzstonlex.runtimesource.data.SourceType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RuntimeSourceFactory {

    public SourceCodeBuilder createFinalized(String className, Class<?> extension, Class<?>[] implementations) {
        return new SourceCodeBuilder(true, SourceType.CLASS, className, extension, implementations);
    }

    public SourceCodeBuilder createFinalized(String className, Class<?> extension) {
        return new SourceCodeBuilder(true, SourceType.CLASS, className, extension);
    }

    public SourceCodeBuilder createFinalized(String className) {
        return new SourceCodeBuilder(true, SourceType.CLASS, className, null);
    }

    public SourceCodeBuilder createFinalized(String className, Class<?>[] implementations) {
        return new SourceCodeBuilder(true, SourceType.CLASS, className, null, implementations);
    }

    public SourceCodeBuilder create(String className, Class<?> extension, Class<?>[] implementations) {
        return new SourceCodeBuilder(false, SourceType.CLASS, className, extension, implementations);
    }

    public SourceCodeBuilder create(String className, Class<?> extension) {
        return new SourceCodeBuilder(false, SourceType.CLASS, className, extension);
    }

    public SourceCodeBuilder create(String className) {
        return new SourceCodeBuilder(false, SourceType.CLASS, className, null);
    }

    public SourceCodeBuilder create(String className, Class<?>[] implementations) {
        return new SourceCodeBuilder(false, SourceType.CLASS, className, null, implementations);
    }

    public SourceCodeBuilder create(SourceType sourceType, String className, Class<?> extension, Class<?>[] implementations) {
        return new SourceCodeBuilder(false, sourceType, className, extension, implementations);
    }

    public SourceCodeBuilder create(SourceType sourceType, String className, Class<?> extension) {
        return new SourceCodeBuilder(false, sourceType, className, extension);
    }

    public SourceCodeBuilder create(SourceType sourceType, String className) {
        return new SourceCodeBuilder(false, sourceType, className, null);
    }

    public SourceCodeBuilder create(SourceType sourceType, String className, Class<?>[] implementations) {
        return new SourceCodeBuilder(false, sourceType, className, null, implementations);
    }

}
