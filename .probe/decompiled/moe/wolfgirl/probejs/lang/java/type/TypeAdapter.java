package moe.wolfgirl.probejs.lang.java.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import moe.wolfgirl.probejs.lang.java.type.impl.ArrayType;
import moe.wolfgirl.probejs.lang.java.type.impl.ClassType;
import moe.wolfgirl.probejs.lang.java.type.impl.ParamType;
import moe.wolfgirl.probejs.lang.java.type.impl.VariableType;
import moe.wolfgirl.probejs.lang.java.type.impl.WildcardType;

public class TypeAdapter {

    public static TypeDescriptor getTypeDescription(AnnotatedType type) {
        return getTypeDescription(type, true);
    }

    public static TypeDescriptor getTypeDescription(AnnotatedType type, boolean recursive) {
        if (type == null) {
            return null;
        } else if (type instanceof AnnotatedArrayType arrayType) {
            return new ArrayType(arrayType);
        } else if (type instanceof AnnotatedParameterizedType paramType) {
            return new ParamType(paramType);
        } else if (type instanceof AnnotatedTypeVariable typeVariable) {
            return new VariableType(typeVariable, recursive);
        } else if (type instanceof AnnotatedWildcardType wildcardType) {
            return new WildcardType(wildcardType, recursive);
        } else if (type.getType() instanceof Class<?> clazz) {
            TypeVariable<?>[] interfaces = clazz.getTypeParameters();
            return (TypeDescriptor) (recursive && interfaces.length != 0 ? new ParamType(type.getAnnotations(), new ClassType(clazz), Collections.nCopies(interfaces.length, new ClassType(Object.class))) : new ClassType(type));
        } else {
            throw new RuntimeException("Unknown type to be resolved");
        }
    }

    public static TypeDescriptor getTypeDescription(Type type) {
        return getTypeDescription(type, true);
    }

    public static TypeDescriptor getTypeDescription(Type type, boolean recursive) {
        if (type == null) {
            return null;
        } else if (type instanceof GenericArrayType arrayType) {
            return new ArrayType(arrayType);
        } else if (type instanceof ParameterizedType parameterizedType) {
            return new ParamType(parameterizedType);
        } else if (type instanceof TypeVariable<?> typeVariable) {
            return new VariableType(typeVariable, recursive);
        } else if (type instanceof java.lang.reflect.WildcardType wildcardType) {
            return new WildcardType(wildcardType, recursive);
        } else if (type instanceof Class<?> clazz) {
            TypeVariable<?>[] interfaces = clazz.getTypeParameters();
            return (TypeDescriptor) (recursive && interfaces.length != 0 ? new ParamType(new Annotation[0], new ClassType(clazz), Collections.nCopies(interfaces.length, new ClassType(Object.class))) : new ClassType(clazz));
        } else {
            throw new RuntimeException("Unknown type to be resolved");
        }
    }

    public static TypeDescriptor consolidateType(TypeDescriptor in, String symbol, TypeDescriptor replacement) {
        if (in instanceof VariableType variableType && variableType.symbol.equals(symbol)) {
            return replacement;
        }
        if (in instanceof ArrayType arrayType) {
            return new ArrayType(consolidateType(arrayType.component, symbol, replacement));
        } else {
            return (TypeDescriptor) (in instanceof ParamType paramType ? new ParamType(new Annotation[0], consolidateType(paramType.base, symbol, replacement), paramType.params.stream().map(t -> consolidateType(t, symbol, replacement)).toList()) : in);
        }
    }
}