package moe.wolfgirl.probejs.lang.typescript.code.member.clazz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import moe.wolfgirl.probejs.lang.typescript.code.member.ConstructorDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.ParamDecl;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSVariableType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;

public class ConstructorBuilder {

    public final List<TSVariableType> variableTypes = new ArrayList();

    public final List<ParamDecl> params = new ArrayList();

    public ConstructorBuilder typeVariables(String... symbols) {
        for (String symbol : symbols) {
            this.typeVariables(Types.generic(symbol));
        }
        return this;
    }

    public ConstructorBuilder typeVariables(TSVariableType... variableTypes) {
        this.variableTypes.addAll(Arrays.asList(variableTypes));
        return this;
    }

    public ConstructorBuilder param(String symbol, BaseType type) {
        return this.param(symbol, type, false);
    }

    public ConstructorBuilder param(String symbol, BaseType type, boolean isOptional) {
        return this.param(symbol, type, isOptional, false);
    }

    public ConstructorBuilder param(String symbol, BaseType type, boolean isOptional, boolean isVarArg) {
        this.params.add(new ParamDecl(symbol, type, isVarArg, isOptional));
        return this;
    }

    public final ConstructorDecl buildAsConstructor() {
        return new ConstructorDecl(this.variableTypes, this.params);
    }
}