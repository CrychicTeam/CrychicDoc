package moe.wolfgirl.probejs.lang.transpiler.members;

import java.util.ArrayList;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.clazz.members.MethodInfo;
import moe.wolfgirl.probejs.lang.java.type.impl.VariableType;
import moe.wolfgirl.probejs.lang.transpiler.TypeConverter;
import moe.wolfgirl.probejs.lang.typescript.code.member.MethodDecl;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSVariableType;

public class Method extends Converter<MethodInfo, MethodDecl> {

    private final Param param;

    public Method(TypeConverter converter) {
        super(converter);
        this.param = new Param(converter);
    }

    public MethodDecl transpile(MethodInfo input) {
        List<TSVariableType> variableTypes = new ArrayList();
        for (VariableType variableType : input.variableTypes) {
            variableTypes.add((TSVariableType) this.converter.convertType(variableType));
        }
        MethodDecl decl = new MethodDecl(input.name, variableTypes, input.params.stream().map(this.param::transpile).toList(), this.converter.convertType(input.returnType));
        decl.isAbstract = input.attributes.isAbstract;
        decl.isStatic = input.attributes.isStatic;
        return decl;
    }
}