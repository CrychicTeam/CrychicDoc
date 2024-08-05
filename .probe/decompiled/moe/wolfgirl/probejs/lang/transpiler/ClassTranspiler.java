package moe.wolfgirl.probejs.lang.transpiler;

import java.util.ArrayList;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.clazz.Clazz;
import moe.wolfgirl.probejs.lang.java.clazz.members.ConstructorInfo;
import moe.wolfgirl.probejs.lang.java.clazz.members.FieldInfo;
import moe.wolfgirl.probejs.lang.java.clazz.members.MethodInfo;
import moe.wolfgirl.probejs.lang.java.type.impl.VariableType;
import moe.wolfgirl.probejs.lang.transpiler.members.Constructor;
import moe.wolfgirl.probejs.lang.transpiler.members.Converter;
import moe.wolfgirl.probejs.lang.transpiler.members.Field;
import moe.wolfgirl.probejs.lang.transpiler.members.Method;
import moe.wolfgirl.probejs.lang.transpiler.transformation.ClassTransformer;
import moe.wolfgirl.probejs.lang.typescript.code.member.ClassDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.ConstructorDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.FieldDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.InterfaceDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.MethodDecl;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSVariableType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;

public class ClassTranspiler extends Converter<Clazz, ClassDecl> {

    private final Method method;

    private final Field field;

    private final Constructor constructor;

    public ClassTranspiler(TypeConverter converter) {
        super(converter);
        this.method = new Method(converter);
        this.field = new Field(converter);
        this.constructor = new Constructor(converter);
    }

    public ClassDecl transpile(Clazz input) {
        List<TSVariableType> variableTypes = new ArrayList();
        for (VariableType variableType : input.variableTypes) {
            variableTypes.add((TSVariableType) this.converter.convertType(variableType));
        }
        BaseType superClass = input.superClass == null ? null : this.converter.convertType(input.superClass);
        ClassDecl decl = (ClassDecl) (input.attribute.isInterface ? new InterfaceDecl(input.classPath.getName(), superClass == Types.ANY ? null : superClass, input.interfaces.stream().map(this.converter::convertType).filter(t -> t != Types.ANY).toList(), variableTypes) : new ClassDecl(input.classPath.getName(), superClass == Types.ANY ? null : superClass, input.interfaces.stream().map(this.converter::convertType).filter(t -> t != Types.ANY).toList(), variableTypes));
        for (FieldInfo fieldInfo : input.fields) {
            FieldDecl fieldDecl = this.field.transpile(fieldInfo);
            ClassTransformer.transformFields(fieldInfo, fieldDecl);
            decl.fields.add(fieldDecl);
        }
        for (MethodInfo methodInfo : input.methods) {
            MethodDecl methodDecl = this.method.transpile(methodInfo);
            ClassTransformer.transformMethods(methodInfo, methodDecl);
            decl.methods.add(methodDecl);
        }
        for (ConstructorInfo constructorInfo : input.constructors) {
            ConstructorDecl constructorDecl = this.constructor.transpile(constructorInfo);
            ClassTransformer.transformConstructors(constructorInfo, constructorDecl);
            decl.constructors.add(constructorDecl);
        }
        return decl;
    }
}