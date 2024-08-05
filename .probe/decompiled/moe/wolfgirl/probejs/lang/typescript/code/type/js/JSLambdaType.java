package moe.wolfgirl.probejs.lang.typescript.code.type.js;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.member.MethodDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.ParamDecl;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;

public class JSLambdaType extends BaseType {

    public final List<ParamDecl> params;

    public final BaseType returnType;

    public JSLambdaType(List<ParamDecl> params, BaseType returnType) {
        this.params = params;
        this.returnType = returnType;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        Set<ClassPath> classPaths = new HashSet(this.returnType.getUsedClassPaths());
        for (ParamDecl param : this.params) {
            classPaths.addAll(param.type.getUsedClassPaths());
        }
        return classPaths;
    }

    @Override
    public List<String> format(Declaration declaration, BaseType.FormatType input) {
        return List.of("%s => %s".formatted(ParamDecl.formatParams(this.params, declaration), this.returnType.line(declaration, BaseType.FormatType.RETURN)));
    }

    public String formatWithName(String name, Declaration declaration, BaseType.FormatType input) {
        return "%s%s: %s".formatted(name, ParamDecl.formatParams(this.params, declaration), this.returnType.line(declaration, BaseType.FormatType.RETURN));
    }

    public MethodDecl asMethod(String methodName) {
        return new MethodDecl(methodName, List.of(), this.params, this.returnType);
    }

    public static class Builder {

        public final List<ParamDecl> params = new ArrayList();

        public BaseType returnType = Types.VOID;

        public boolean arrowFunction = true;

        public JSLambdaType.Builder returnType(BaseType type) {
            this.returnType = Types.ignoreContext(type, this.arrowFunction ? BaseType.FormatType.INPUT : BaseType.FormatType.RETURN);
            return this;
        }

        public JSLambdaType.Builder param(String symbol, BaseType type) {
            return this.param(symbol, type, false);
        }

        public JSLambdaType.Builder param(String symbol, BaseType type, boolean isOptional) {
            return this.param(symbol, type, isOptional, false);
        }

        public JSLambdaType.Builder param(String symbol, BaseType type, boolean isOptional, boolean isVarArg) {
            this.params.add(new ParamDecl(symbol, Types.ignoreContext(type, this.arrowFunction ? BaseType.FormatType.RETURN : BaseType.FormatType.INPUT), isVarArg, isOptional));
            return this;
        }

        public JSLambdaType.Builder method() {
            this.arrowFunction = false;
            return this;
        }

        public JSLambdaType build() {
            return new JSLambdaType(this.params, this.returnType);
        }
    }
}