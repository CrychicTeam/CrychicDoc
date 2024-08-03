package moe.wolfgirl.probejs.lang.typescript.code.member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.Code;
import moe.wolfgirl.probejs.lang.typescript.code.member.clazz.ConstructorBuilder;
import moe.wolfgirl.probejs.lang.typescript.code.member.clazz.MethodBuilder;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSVariableType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import org.jetbrains.annotations.Nullable;

public class ClassDecl extends CommentableCode {

    public final String name;

    @Nullable
    public BaseType superClass;

    public final List<BaseType> interfaces;

    public final List<TSVariableType> variableTypes;

    public boolean isAbstract = false;

    public boolean isNative = true;

    public final List<FieldDecl> fields = new ArrayList();

    public final List<ConstructorDecl> constructors = new ArrayList();

    public final List<MethodDecl> methods = new ArrayList();

    public final List<Code> bodyCode = new ArrayList();

    public ClassDecl(String name, @Nullable BaseType superClass, List<BaseType> interfaces, List<TSVariableType> variableTypes) {
        this.name = name;
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.variableTypes = variableTypes;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        Set<ClassPath> paths = new HashSet();
        for (FieldDecl field : this.fields) {
            paths.addAll(field.getUsedClassPaths());
        }
        for (ConstructorDecl constructor : this.constructors) {
            paths.addAll(constructor.getUsedClassPaths());
        }
        for (MethodDecl method : this.methods) {
            paths.addAll(method.getUsedClassPaths());
        }
        for (BaseType anInterface : this.interfaces) {
            paths.addAll(anInterface.getUsedClassPaths());
        }
        for (TSVariableType variableType : this.variableTypes) {
            paths.addAll(variableType.getUsedClassPaths());
        }
        for (Code code : this.bodyCode) {
            paths.addAll(code.getUsedClassPaths());
        }
        if (this.superClass != null) {
            paths.addAll(this.superClass.getUsedClassPaths());
        }
        return paths;
    }

    @Override
    public List<String> formatRaw(Declaration declaration) {
        List<String> modifiers = new ArrayList();
        modifiers.add("export");
        if (this.isAbstract) {
            modifiers.add("abstract");
        }
        modifiers.add("class");
        String head = "%s %s".formatted(String.join(" ", modifiers), this.name);
        if (this.variableTypes.size() != 0) {
            String variables = (String) this.variableTypes.stream().map(type -> type.line(declaration, BaseType.FormatType.VARIABLE)).collect(Collectors.joining(", "));
            head = "%s<%s>".formatted(head, variables);
        }
        if (this.superClass != null) {
            head = "%s extends %s".formatted(head, this.superClass.line(declaration));
        }
        if (this.interfaces.size() != 0) {
            String formatted = (String) this.interfaces.stream().map(type -> type.line(declaration)).collect(Collectors.joining(", "));
            head = "%s implements %s".formatted(head, formatted);
        }
        head = "%s {".formatted(head);
        List<String> body = new ArrayList();
        for (FieldDecl field : this.fields) {
            body.addAll(field.format(declaration));
        }
        body.add("");
        for (ConstructorDecl constructor : this.constructors) {
            body.addAll(constructor.format(declaration));
        }
        body.add("");
        for (MethodDecl method : this.methods) {
            body.addAll(method.format(declaration));
        }
        List<String> tail = new ArrayList();
        for (Code code : this.bodyCode) {
            tail.addAll(code.format(declaration));
        }
        tail.add("}");
        List<String> formatted = new ArrayList();
        formatted.add(head);
        formatted.addAll(body);
        formatted.addAll(tail);
        return formatted;
    }

    public static class Builder {

        public final String name;

        @Nullable
        public BaseType superClass = null;

        public final List<BaseType> interfaces = new ArrayList();

        public final List<TSVariableType> variableTypes = new ArrayList();

        public boolean isAbstract = false;

        public boolean isInterface = false;

        public final List<FieldDecl> fields = new ArrayList();

        public final List<ConstructorDecl> constructors = new ArrayList();

        public final List<MethodDecl> methods = new ArrayList();

        public Builder(String name) {
            this.name = name;
        }

        public ClassDecl.Builder abstractClass() {
            this.isAbstract = true;
            return this;
        }

        public ClassDecl.Builder interfaceClass() {
            this.isInterface = true;
            return this;
        }

        public ClassDecl.Builder field(String symbol, BaseType baseType) {
            return this.field(symbol, baseType, false);
        }

        public ClassDecl.Builder field(String symbol, BaseType baseType, boolean isStatic) {
            return this.field(symbol, baseType, isStatic, false);
        }

        public ClassDecl.Builder field(String symbol, BaseType baseType, boolean isStatic, boolean isFinal) {
            FieldDecl field = new FieldDecl(symbol, baseType);
            field.isStatic = isStatic;
            field.isFinal = isFinal;
            this.fields.add(field);
            return this;
        }

        public ClassDecl.Builder superClass(BaseType superClass) {
            this.superClass = superClass;
            return this;
        }

        public ClassDecl.Builder interfaces(BaseType... interfaces) {
            this.interfaces.addAll(Arrays.asList(interfaces));
            return this;
        }

        public ClassDecl.Builder typeVariables(String... symbols) {
            for (String symbol : symbols) {
                this.typeVariables(Types.generic(symbol));
            }
            return this;
        }

        public ClassDecl.Builder typeVariables(TSVariableType... variableTypes) {
            this.variableTypes.addAll(Arrays.asList(variableTypes));
            return this;
        }

        public ClassDecl.Builder method(String name, Consumer<MethodBuilder> method) {
            MethodBuilder builder = new MethodBuilder(name);
            method.accept(builder);
            this.methods.add(builder.buildAsMethod());
            return this;
        }

        public ClassDecl.Builder ctor(Consumer<ConstructorBuilder> constructor) {
            ConstructorBuilder builder = new ConstructorBuilder();
            constructor.accept(builder);
            this.constructors.add(builder.buildAsConstructor());
            return this;
        }

        public ClassDecl build() {
            ClassDecl decl = (ClassDecl) (this.isInterface ? new InterfaceDecl(this.name, this.superClass, this.interfaces, this.variableTypes) : new ClassDecl(this.name, this.superClass, this.interfaces, this.variableTypes));
            decl.isAbstract = this.isAbstract;
            decl.methods.addAll(this.methods);
            decl.fields.addAll(this.fields);
            decl.constructors.addAll(this.constructors);
            decl.isNative = false;
            decl.addComment(new String[] { "This is a class generated by ProbeJS, you shall not load/require this class for your usages\nbecause it doesn't exist in the JVM. The class exist only for type hinting purpose.\nLoading the class will not throw an error, but instead the class loaded will be undefined.\n" });
            return decl;
        }
    }
}