package moe.wolfgirl.probejs.lang.typescript.code.ts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.member.CommentableCode;
import moe.wolfgirl.probejs.lang.typescript.code.member.ParamDecl;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSVariableType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;

public class MethodDeclaration extends CommentableCode {

    public String name;

    public final List<TSVariableType> variableTypes;

    public final List<ParamDecl> params;

    public BaseType returnType;

    public MethodDeclaration(String name, List<TSVariableType> variableTypes, List<ParamDecl> params, BaseType returnType) {
        this.name = name;
        this.variableTypes = variableTypes;
        this.params = params;
        this.returnType = returnType;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        Set<ClassPath> paths = new HashSet(this.returnType.getUsedClassPaths());
        for (TSVariableType variableType : this.variableTypes) {
            paths.addAll(variableType.getUsedClassPaths());
        }
        for (ParamDecl param : this.params) {
            paths.addAll(param.type.getUsedClassPaths());
        }
        return paths;
    }

    @Override
    public List<String> formatRaw(Declaration declaration) {
        String head = "function %s".formatted(this.name);
        if (this.variableTypes.size() != 0) {
            String variables = (String) this.variableTypes.stream().map(type -> type.line(declaration)).collect(Collectors.joining(", "));
            head = "%s<%s>".formatted(head, variables);
        }
        String body = ParamDecl.formatParams(this.params, declaration);
        String tail = ": %s".formatted(this.returnType.line(declaration, BaseType.FormatType.RETURN));
        return List.of("%s%s%s".formatted(head, body, tail));
    }

    public static class Builder {

        public final String name;

        public final List<TSVariableType> variableTypes = new ArrayList();

        public final List<ParamDecl> params = new ArrayList();

        public BaseType returnType = Types.VOID;

        public Builder(String name) {
            this.name = name;
        }

        public MethodDeclaration.Builder variable(String... symbols) {
            for (String symbol : symbols) {
                this.variable(Types.generic(symbol));
            }
            return this;
        }

        public MethodDeclaration.Builder variable(TSVariableType... variableType) {
            this.variableTypes.addAll(Arrays.asList(variableType));
            return this;
        }

        public MethodDeclaration.Builder returnType(BaseType type) {
            this.returnType = type;
            return this;
        }

        public MethodDeclaration.Builder param(String symbol, BaseType type) {
            return this.param(symbol, type, false);
        }

        public MethodDeclaration.Builder param(String symbol, BaseType type, boolean isOptional) {
            return this.param(symbol, type, isOptional, false);
        }

        public MethodDeclaration.Builder param(String symbol, BaseType type, boolean isOptional, boolean isVarArg) {
            this.params.add(new ParamDecl(symbol, type, isVarArg, isOptional));
            return this;
        }

        public MethodDeclaration build() {
            return new MethodDeclaration(this.name, this.variableTypes, this.params, this.returnType);
        }
    }
}