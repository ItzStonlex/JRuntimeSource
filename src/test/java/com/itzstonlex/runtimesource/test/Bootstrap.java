package com.itzstonlex.runtimesource.test;

import com.itzstonlex.runtimesource.RuntimeSourceFactory;
import com.itzstonlex.runtimesource.SourceCodeBuilder;
import com.itzstonlex.runtimesource.compile.RuntimeCompiler;
import com.itzstonlex.runtimesource.data.AccessID;
import com.itzstonlex.runtimesource.data.MethodParam;
import com.itzstonlex.runtimesource.data.MethodSignature;

import java.io.Serializable;

public class Bootstrap {

    public static void main(String[] args)
    throws Exception {

        SourceCodeBuilder sourceCodeBuilder = RuntimeSourceFactory.create("User", null, new Class[]{Serializable.class})
                .setPackage("com.itzstonlex.users")

                .makeField(AccessID.PRIVATE, String.class, "name")
                .makeConstructor(AccessID.PUBLIC, MethodSignature.with(MethodParam.create(String.class, "name")))
                .beginBody()
                    .makeSetThisField("name")
                .endpointBody()

                .makeGetter(String.class, "name")
                .makeSetter(String.class, "name")

                .makeOverrideMethod(AccessID.PUBLIC, String.class, "toString", MethodSignature.empty())
                .beginBody()
                    .makeReturn("getName()")
                .endpointBody();

        RuntimeCompiler runtimeCompiler = new RuntimeCompiler();
        runtimeCompiler.addClass(sourceCodeBuilder);

        runtimeCompiler.compile();

        Class<?> compiledClass = runtimeCompiler.getCompiledClass("com.itzstonlex.users.User");

        System.out.println(sourceCodeBuilder.build());
        System.out.println(compiledClass.getConstructor(String.class).newInstance("itzstonlex"));
    }

}
