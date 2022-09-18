package com.itzstonlex.runtimesource;

import com.itzstonlex.runtimesource.data.AccessID;
import com.itzstonlex.runtimesource.data.MethodParam;
import com.itzstonlex.runtimesource.data.MethodSignature;
import com.itzstonlex.runtimesource.data.SourceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.Arrays;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class SourceCodeBuilder {

    @Getter
    String className;

    @Getter
    StringBuilder sourceCode;

    @NonFinal
    int outbound;

    SourceCodeBuilder(boolean isFinal, SourceType sourceType, String className, Class<?> extension, Class<?>... implementations) {
        this.className = className;
        this.sourceCode = new StringBuilder("\npublic ")
                .append(isFinal ? " final " : "")
                .append(sourceType.getName())
                .append(className);

        if (extension != null) {
            importType(extension);

            sourceCode.append(" extends ").append(extension.getSimpleName());
        }

        if (implementations.length > 0) {
            sourceCode.append(" implements ");

            for (Class<?> implementType : implementations) {
                importType(implementType);
            }

            sourceCode.append(Arrays.stream(implementations).map(Class::getSimpleName).collect(Collectors.joining(", ")));
        }

        beginBody();
    }

    private String getClassName(Class<?> cls) {
        if (cls.isPrimitive()) {
            return cls.getName();
        }
        return cls.getPackage().getName().startsWith("java.lang") ? cls.getSimpleName() : cls.getName();
    }

    private String getOutbound() {
        StringBuilder outboundString = new StringBuilder();

        for (int i = 0; i < outbound; i++) {
            outboundString.append("    ");
        }

        return outboundString.toString();
    }

    public SourceCodeBuilder setPackage(String name) {
        if (sourceCode.toString().startsWith("package ")) {
            sourceCode.delete(0, sourceCode.indexOf(";\n\n") + ";\n\n".length());
        }

        sourceCode.insert(0, "package " + name + ";\n\n");
        return this;
    }

    public SourceCodeBuilder importType(Class<?> cls) {
        sourceCode.insert(0, "import " + cls.getName() + ";\n");
        return this;
    }

    public SourceCodeBuilder beginBody() {
        sourceCode.append(" {");
        outbound++;
        return this;
    }

    public SourceCodeBuilder endpointBody() {
        if (outbound - 1 < 0) {
            throw new UnsupportedOperationException("BEGIN body is not found for set ENDPOINT");
        }

        outbound--;
        sourceCode.append("\n").append(getOutbound()).append("}");
        return this;
    }

    private SourceCodeBuilder makeField(boolean isStatic, boolean isFinal, AccessID accessID, Class<?> type, String name, String codeValue) {
        sourceCode.append("\n\n")
                .append(getOutbound())
                .append(accessID.getName())
                .append(isStatic ? "static " : "")
                .append(isFinal ? "final " : "")
                .append(getClassName(type))
                .append(" ")
                .append(name);

        if (codeValue != null) {
            sourceCode.append(" = ").append(codeValue);
        }

        sourceCode.append(";\n");
        return this;
    }

    public SourceCodeBuilder makeConstant(AccessID accessID, Class<?> type, String name, String codeValue) {
        return makeField(true, true, accessID, type, name, codeValue);
    }

    public SourceCodeBuilder makeConstant(Class<?> type, String name, String codeValue) {
        return makeField(true, true, AccessID.PUBLIC, type, name, codeValue);
    }

    public SourceCodeBuilder makeStaticField(AccessID accessID, Class<?> type, String name, String codeValue) {
        return makeField(true, false, accessID, type, name, codeValue);
    }

    public SourceCodeBuilder makeStaticField(AccessID accessID, Class<?> type, String name) {
        return makeField(true, false, accessID, type, name, null);
    }

    public SourceCodeBuilder makeStaticField(Class<?> type, String name, String codeValue) {
        return makeField(true, false, AccessID.PUBLIC, type, name, codeValue);
    }

    public SourceCodeBuilder makeStaticField(Class<?> type, String name) {
        return makeField(true, false, AccessID.PUBLIC, type, name, null);
    }

    public SourceCodeBuilder makeFinalizedField(AccessID accessID, Class<?> type, String name, String codeValue) {
        return makeField(false, true, accessID, type, name, codeValue);
    }

    public SourceCodeBuilder makeFinalizedField(AccessID accessID, Class<?> type, String name) {
        return makeField(false, true, accessID, type, name, null);
    }

    public SourceCodeBuilder makeField(AccessID accessID, Class<?> type, String name, String codeValue) {
        return makeField(false, false, accessID, type, name, codeValue);
    }

    public SourceCodeBuilder makeField(AccessID accessID, Class<?> type, String name) {
        return makeField(false, false, accessID, type, name, null);
    }

    public SourceCodeBuilder makeFinalizedField(Class<?> type, String name, String codeValue) {
        return makeField(false, true, AccessID.PUBLIC, type, name, codeValue);
    }

    public SourceCodeBuilder makeFinalizedField(Class<?> type, String name) {
        return makeField(false, true, AccessID.PUBLIC, type, name, null);
    }

    public SourceCodeBuilder makeField(Class<?> type, String name, String codeValue) {
        return makeField(false, false, AccessID.PUBLIC, type, name, codeValue);
    }

    public SourceCodeBuilder makeField(Class<?> type, String name) {
        return makeField(false, false, AccessID.PUBLIC, type, name, null);
    }

    public SourceCodeBuilder makeLocalFinalizedField(Class<?> type, String name, String codeValue) {
        return makeField(false, true, AccessID.PACKAGE, type, name, codeValue);
    }

    public SourceCodeBuilder makeLocalFinalizedField(Class<?> type, String name) {
        return makeField(false, true, AccessID.PACKAGE, type, name, null);
    }

    public SourceCodeBuilder makeLocalField(Class<?> type, String name, String codeValue) {
        return makeField(false, false, AccessID.PACKAGE, type, name, codeValue);
    }

    public SourceCodeBuilder makeLocalField(Class<?> type, String name) {
        return makeField(false, false, AccessID.PACKAGE, type, name, null);
    }

    public SourceCodeBuilder makeConstructor(@NonNull AccessID accessID, @NonNull MethodSignature signature) {
        sourceCode.append("\n").append(getOutbound()).append(accessID.getName()).append(className)
                .append("(").append(signature).append(")");

        return this;
    }

    public SourceCodeBuilder makeEmptyConstructor(@NonNull AccessID accessID) {
        return makeConstructor(accessID, MethodSignature.empty()).beginBody().endpointBody();
    }

    private SourceCodeBuilder makeMethod(boolean isFinal, boolean override, AccessID accessID, Class<?> returnType, String name, MethodSignature signature) {
        sourceCode.append("\n\n")
                .append(override ? getOutbound() + "@Override\n" : "")
                .append(getOutbound())
                .append(isFinal ? "final " : "")
                .append(accessID.getName())
                .append(returnType == null ? "void" : getClassName(returnType))
                .append(" ")
                .append(name).append("(");

        sourceCode.append(signature);
        sourceCode.append(")");
        return this;
    }

    public SourceCodeBuilder makeOverrideMethod(AccessID accessID, Class<?> returnType, String name, MethodSignature signature) {
        return makeMethod(false, true, accessID, returnType, name, signature);
    }

    public SourceCodeBuilder makeFinalizedMethod(AccessID accessID, Class<?> returnType, String name, MethodSignature signature) {
        return makeMethod(true, false, accessID, returnType, name, signature);
    }

    public SourceCodeBuilder makeMethod(AccessID accessID, Class<?> returnType, String name, MethodSignature signature) {
        return makeMethod(false, false, accessID, returnType, name, signature);
    }

    public SourceCodeBuilder makeOverrideMethod(Class<?> returnType, String name, MethodSignature signature) {
        return makeMethod(false, true, AccessID.PUBLIC, returnType, name, signature);
    }

    public SourceCodeBuilder makeFinalizedMethod(Class<?> returnType, String name, MethodSignature signature) {
        return makeMethod(true, false, AccessID.PUBLIC, returnType, name, signature);
    }

    public SourceCodeBuilder makeMethod(Class<?> returnType, String name, MethodSignature signature) {
        return makeMethod(false, false, AccessID.PUBLIC, returnType, name, signature);
    }

    public SourceCodeBuilder makeGetter(AccessID accessID, Class<?> returnType, String field) {
        String methodName = ("get") + Character.toUpperCase(field.charAt(0)) + field.substring(1);
        return makeMethod(accessID, returnType, methodName, MethodSignature.empty())
                .beginBody()
                .makeReturn(field)
                .endpointBody();
    }

    public SourceCodeBuilder makeGetter(Class<?> returnType, String field) {
        return makeGetter(AccessID.PUBLIC, returnType, field);
    }

    public SourceCodeBuilder makeSetter(AccessID accessID, Class<?> returnType, String field) {
        String methodName = ("set") + Character.toUpperCase(field.charAt(0)) + field.substring(1);
        return makeMethod(accessID, void.class, methodName, MethodSignature.with(MethodParam.create(returnType, field)))
                .beginBody()
                .makeSetThisField(field)
                .endpointBody();
    }

    public SourceCodeBuilder makeSetter(Class<?> returnType, String field) {
        return makeSetter(AccessID.PUBLIC, returnType, field);
    }

    public SourceCodeBuilder makeLine(String line) {
        sourceCode.append("\n").append(getOutbound()).append(line).append(";");
        return this;
    }

    public SourceCodeBuilder makeReturn(String value) {
        sourceCode.append("\n").append(getOutbound()).append("return ").append(value).append(";");
        return this;
    }

    public SourceCodeBuilder makeReturnString(String value) {
        sourceCode.append("\n").append(getOutbound()).append("return \"").append(value).append("\";");
        return this;
    }

    public SourceCodeBuilder makeReturnChar(String value) {
        sourceCode.append("\n").append(getOutbound()).append("return '").append(value).append("';");
        return this;
    }

    private SourceCodeBuilder makeSetField(boolean isThis, String field, String codeValue) {
        return makeLine((isThis ? "this." : "") + field + " = " + codeValue);
    }

    public SourceCodeBuilder makeSetThisField(String field, String codeValue) {
        return makeSetField(true, field, codeValue);
    }

    public SourceCodeBuilder makeSetThisField(String field) {
        return makeSetField(true, field, field);
    }

    public SourceCodeBuilder makeSetField(String field, String codeValue) {
        return makeSetField(false, field, codeValue);
    }

    @NonFinal
    private boolean alreadyBuild;

    public String build() {
        if (alreadyBuild) {
            return sourceCode.toString();
        }

        alreadyBuild = true;
        return endpointBody().sourceCode.toString();
    }
}