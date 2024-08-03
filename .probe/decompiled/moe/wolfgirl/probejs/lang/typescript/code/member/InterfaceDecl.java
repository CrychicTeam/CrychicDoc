package moe.wolfgirl.probejs.lang.typescript.code.member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.Code;
import moe.wolfgirl.probejs.lang.typescript.code.ts.MethodDeclaration;
import moe.wolfgirl.probejs.lang.typescript.code.ts.VariableDeclaration;
import moe.wolfgirl.probejs.lang.typescript.code.ts.Wrapped;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSVariableType;
import org.jetbrains.annotations.Nullable;

public class InterfaceDecl extends ClassDecl {

    private final Wrapped.Namespace namespace;

    public InterfaceDecl(String name, @Nullable BaseType superClass, List<BaseType> interfaces, List<TSVariableType> variableTypes) {
        super(name, superClass, interfaces, variableTypes);
        this.namespace = new Wrapped.Namespace(name);
    }

    @Override
    public List<String> formatRaw(Declaration declaration) {
        for (MethodDecl method : this.methods) {
            method.isInterface = true;
        }
        String head = "export interface %s".formatted(this.name);
        if (this.variableTypes.size() != 0) {
            String variables = (String) this.variableTypes.stream().map(type -> type.line(declaration, BaseType.FormatType.VARIABLE)).collect(Collectors.joining(", "));
            head = "%s<%s>".formatted(head, variables);
        }
        if (this.interfaces.size() != 0) {
            String formatted = (String) this.interfaces.stream().map(type -> type.line(declaration)).collect(Collectors.joining(", "));
            head = "%s extends %s".formatted(head, formatted);
        }
        head = "%s {".formatted(head);
        List<String> body = new ArrayList();
        for (FieldDecl field : this.fields) {
            this.namespace.addCode(new VariableDeclaration(field.name, field.type));
        }
        body.add("");
        for (MethodDecl method : this.methods) {
            if (method.isStatic) {
                this.namespace.addCode(new MethodDeclaration(method.name, method.variableTypes, method.params, method.returnType));
            } else {
                body.addAll(method.format(declaration));
            }
        }
        if (this.namespace.isEmpty()) {
            this.namespace.addCode(new Code() {

                @Override
                public Collection<ClassPath> getUsedClassPaths() {
                    return List.of();
                }

                @Override
                public List<String> format(Declaration declaration) {
                    return List.of("const probejs$$marker: never");
                }
            });
        }
        if (this.methods.stream().filter(methodx -> methodx.isAbstract).count() == 1L) {
            body.add("");
            MethodDecl methodx = (MethodDecl) this.methods.get(0);
            String hybridBody = ParamDecl.formatParams(methodx.params, declaration);
            String returnType = methodx.returnType.line(declaration);
            body.add("%s: %s".formatted(hybridBody, returnType));
        }
        List<String> formatted = new ArrayList();
        formatted.add(head);
        formatted.addAll(body);
        formatted.add("}\n");
        formatted.addAll(this.namespace.format(declaration));
        return formatted;
    }
}