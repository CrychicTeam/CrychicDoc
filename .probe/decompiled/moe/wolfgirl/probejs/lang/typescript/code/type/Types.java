package moe.wolfgirl.probejs.lang.typescript.code.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiFunction;
import moe.wolfgirl.probejs.ProbeJS;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSArrayType;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSJoinedType;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSLambdaType;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSObjectType;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSPrimitiveType;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSTypeOfType;

public interface Types {

    JSPrimitiveType ANY = new JSPrimitiveType("any");

    JSPrimitiveType BOOLEAN = new JSPrimitiveType("boolean");

    JSPrimitiveType NUMBER = new JSPrimitiveType("number");

    JSPrimitiveType STRING = new JSPrimitiveType("string");

    JSPrimitiveType NEVER = new JSPrimitiveType("never");

    JSPrimitiveType UNKNOWN = new JSPrimitiveType("unknown");

    JSPrimitiveType VOID = new JSPrimitiveType("void");

    JSPrimitiveType THIS = new JSPrimitiveType("this");

    JSPrimitiveType OBJECT = new JSPrimitiveType("object");

    JSPrimitiveType NULL = new JSPrimitiveType("null");

    static JSPrimitiveType literal(Object content) {
        return !(content instanceof String) && !(content instanceof Number) && !(content instanceof Boolean) ? ANY : new JSPrimitiveType(ProbeJS.GSON.toJson(content));
    }

    static JSPrimitiveType primitive(String type) {
        return new JSPrimitiveType(type);
    }

    static JSArrayType arrayOf(BaseType... types) {
        return new JSArrayType(Arrays.stream(types).toList());
    }

    static JSJoinedType.Intersection and(BaseType... types) {
        return new JSJoinedType.Intersection(Arrays.stream(types).toList());
    }

    static BaseType or(BaseType... types) {
        return (BaseType) (types.length == 0 ? NEVER : new JSJoinedType.Union(Arrays.stream(types).toList()));
    }

    static TSParamType parameterized(BaseType base, BaseType... params) {
        return new TSParamType(base, Arrays.stream(params).toList());
    }

    static TSVariableType generic(String symbol) {
        return generic(symbol, ANY);
    }

    static TSVariableType generic(String symbol, BaseType extendOn) {
        return new TSVariableType(symbol, extendOn);
    }

    static BaseType typeMaybeGeneric(Class<?> clazz) {
        if (clazz.getTypeParameters().length == 0) {
            return type(clazz);
        } else {
            BaseType[] params = (BaseType[]) Collections.nCopies(clazz.getTypeParameters().length, ANY).toArray(BaseType[]::new);
            return parameterized(type(clazz), params);
        }
    }

    static TSClassType type(Class<?> clazz) {
        return type(new ClassPath(clazz));
    }

    static TSClassType type(ClassPath classPath) {
        return new TSClassType(classPath);
    }

    static JSTypeOfType typeOf(Class<?> clazz) {
        return typeOf(new ClassPath(clazz));
    }

    static JSTypeOfType typeOf(ClassPath classPath) {
        return typeOf(new TSClassType(classPath));
    }

    static JSTypeOfType typeOf(BaseType classType) {
        return new JSTypeOfType(classType);
    }

    static BaseType ignoreContext(BaseType type, BaseType.FormatType formatType) {
        return new ContextShield(type, formatType);
    }

    static BaseType custom(BiFunction<Declaration, BaseType.FormatType, String> formatter, ClassPath... imports) {
        return new CustomType(formatter, imports);
    }

    static JSLambdaType.Builder lambda() {
        return new JSLambdaType.Builder();
    }

    static JSObjectType.Builder object() {
        return new JSObjectType.Builder();
    }
}