package moe.wolfgirl.probejs.lang.typescript.code.member.clazz;

import moe.wolfgirl.probejs.lang.typescript.code.member.MethodDecl;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;

public class MethodBuilder extends ConstructorBuilder {

    public final String name;

    public BaseType returnType = Types.VOID;

    public boolean isAbstract = false;

    public boolean isStatic = false;

    public MethodBuilder(String name) {
        this.name = name;
    }

    public MethodBuilder returnType(BaseType type) {
        this.returnType = type;
        return this;
    }

    public MethodBuilder abstractMethod() {
        this.isAbstract = true;
        return this;
    }

    public MethodBuilder staticMethod() {
        this.isStatic = true;
        return this;
    }

    public MethodDecl buildAsMethod() {
        MethodDecl decl = new MethodDecl(this.name, this.variableTypes, this.params, this.returnType);
        decl.isAbstract = this.isAbstract;
        decl.isStatic = this.isStatic;
        return decl;
    }
}