package dev.latvian.mods.rhino;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class FunctionObject extends BaseFunction {

    public static final int JAVA_UNSUPPORTED_TYPE = 0;

    public static final int JAVA_STRING_TYPE = 1;

    public static final int JAVA_INT_TYPE = 2;

    public static final int JAVA_BOOLEAN_TYPE = 3;

    public static final int JAVA_DOUBLE_TYPE = 4;

    public static final int JAVA_SCRIPTABLE_TYPE = 5;

    public static final int JAVA_OBJECT_TYPE = 6;

    private static final short VARARGS_METHOD = -1;

    private static final short VARARGS_CTOR = -2;

    private static boolean sawSecurityException;

    private final String functionName;

    private final int parmsLength;

    private final boolean isStatic;

    MemberBox member;

    private transient byte[] typeTags;

    private transient boolean hasVoidReturn;

    private transient int returnTypeTag;

    public static int getTypeTag(Class<?> type) {
        if (type == ScriptRuntime.StringClass) {
            return 1;
        } else if (type == ScriptRuntime.IntegerClass || type == int.class) {
            return 2;
        } else if (type == ScriptRuntime.BooleanClass || type == boolean.class) {
            return 3;
        } else if (type == ScriptRuntime.DoubleClass || type == double.class) {
            return 4;
        } else if (ScriptRuntime.ScriptableClass.isAssignableFrom(type)) {
            return 5;
        } else {
            return type == ScriptRuntime.ObjectClass ? 6 : 0;
        }
    }

    public static Object convertArg(Context cx, Scriptable scope, Object arg, int typeTag) {
        switch(typeTag) {
            case 1:
                if (arg instanceof String) {
                    return arg;
                }
                return ScriptRuntime.toString(cx, arg);
            case 2:
                if (arg instanceof Integer) {
                    return arg;
                }
                return ScriptRuntime.toInt32(cx, arg);
            case 3:
                if (arg instanceof Boolean) {
                    return arg;
                }
                return ScriptRuntime.toBoolean(cx, arg) ? Boolean.TRUE : Boolean.FALSE;
            case 4:
                if (arg instanceof Double) {
                    return arg;
                }
                return ScriptRuntime.toNumber(cx, arg);
            case 5:
                return ScriptRuntime.toObjectOrNull(cx, arg, scope);
            case 6:
                return arg;
            default:
                throw new IllegalArgumentException();
        }
    }

    static Method findSingleMethod(Method[] methods, String name, Context cx) {
        Method found = null;
        int i = 0;
        for (int N = methods.length; i != N; i++) {
            Method method = methods[i];
            if (method != null && name.equals(method.getName())) {
                if (found != null) {
                    throw Context.reportRuntimeError2("msg.no.overload", name, method.getDeclaringClass().getName(), cx);
                }
                found = method;
            }
        }
        return found;
    }

    static Method[] getMethodList(Class<?> clazz) {
        Method[] methods = null;
        try {
            if (!sawSecurityException) {
                methods = clazz.getDeclaredMethods();
            }
        } catch (SecurityException var6) {
            sawSecurityException = true;
        }
        if (methods == null) {
            methods = clazz.getMethods();
        }
        int count = 0;
        for (int i = 0; i < methods.length; i++) {
            if (sawSecurityException ? methods[i].getDeclaringClass() == clazz : Modifier.isPublic(methods[i].getModifiers())) {
                count++;
            } else {
                methods[i] = null;
            }
        }
        Method[] result = new Method[count];
        int j = 0;
        for (int ix = 0; ix < methods.length; ix++) {
            if (methods[ix] != null) {
                result[j++] = methods[ix];
            }
        }
        return result;
    }

    public FunctionObject(String name, Member methodOrConstructor, Scriptable scope, Context cx) {
        if (methodOrConstructor instanceof Constructor) {
            this.member = new MemberBox((Constructor) methodOrConstructor);
            this.isStatic = true;
        } else {
            this.member = new MemberBox((Method) methodOrConstructor);
            this.isStatic = this.member.isStatic();
        }
        String methodName = this.member.getName();
        this.functionName = name;
        Class<?>[] types = this.member.argTypes;
        int arity = types.length;
        if (arity == 4 && (types[1].isArray() || types[2].isArray())) {
            if (types[1].isArray()) {
                if (!this.isStatic || types[0] != ScriptRuntime.ContextClass || types[1].getComponentType() != ScriptRuntime.ObjectClass || types[2] != ScriptRuntime.FunctionClass || types[3] != boolean.class) {
                    throw Context.reportRuntimeError1("msg.varargs.ctor", methodName, cx);
                }
                this.parmsLength = -2;
            } else {
                if (!this.isStatic || types[0] != ScriptRuntime.ContextClass || types[1] != ScriptRuntime.ScriptableClass || types[2].getComponentType() != ScriptRuntime.ObjectClass || types[3] != ScriptRuntime.FunctionClass) {
                    throw Context.reportRuntimeError1("msg.varargs.fun", methodName, cx);
                }
                this.parmsLength = -1;
            }
        } else {
            this.parmsLength = arity;
            if (arity > 0) {
                this.typeTags = new byte[arity];
                for (int i = 0; i != arity; i++) {
                    int tag = getTypeTag(types[i]);
                    if (tag == 0) {
                        throw Context.reportRuntimeError2("msg.bad.parms", types[i].getName(), methodName, cx);
                    }
                    this.typeTags[i] = (byte) tag;
                }
            }
        }
        if (this.member.isMethod()) {
            Class<?> returnType = this.member.getReturnType();
            if (returnType == void.class) {
                this.hasVoidReturn = true;
            } else {
                this.returnTypeTag = getTypeTag(returnType);
            }
        } else {
            Class<?> ctorType = this.member.getDeclaringClass();
            if (!ScriptRuntime.ScriptableClass.isAssignableFrom(ctorType)) {
                throw Context.reportRuntimeError1("msg.bad.ctor.return", ctorType.getName(), cx);
            }
        }
        ScriptRuntime.setFunctionProtoAndParent(cx, scope, this);
    }

    @Override
    public int getArity() {
        return this.parmsLength < 0 ? 1 : this.parmsLength;
    }

    @Override
    public int getLength() {
        return this.getArity();
    }

    @Override
    public String getFunctionName() {
        return this.functionName == null ? "" : this.functionName;
    }

    public void addAsConstructor(Scriptable scope, Scriptable prototype, Context cx) {
        this.initAsConstructor(scope, prototype, cx);
        defineProperty(scope, prototype.getClassName(), this, 2, cx);
    }

    void initAsConstructor(Scriptable scope, Scriptable prototype, Context cx) {
        ScriptRuntime.setFunctionProtoAndParent(cx, scope, this);
        this.setImmunePrototypeProperty(prototype);
        prototype.setParentScope(this);
        defineProperty(prototype, "constructor", this, 7, cx);
        this.setParentScope(scope);
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        boolean checkMethodResult = false;
        int argsLength = args.length;
        for (int i = 0; i < argsLength; i++) {
            if (args[i] instanceof ConsString) {
                args[i] = args[i].toString();
            }
        }
        Object result;
        if (this.parmsLength < 0) {
            if (this.parmsLength == -1) {
                Object[] invokeArgs = new Object[] { cx, thisObj, args, this };
                result = this.member.invoke(null, invokeArgs, cx, scope);
                checkMethodResult = true;
            } else {
                boolean inNewExpr = thisObj == null;
                Boolean b = inNewExpr ? Boolean.TRUE : Boolean.FALSE;
                Object[] invokeArgs = new Object[] { cx, args, this, b };
                result = this.member.isCtor() ? this.member.newInstance(invokeArgs, cx, scope) : this.member.invoke(null, invokeArgs, cx, scope);
            }
        } else {
            if (!this.isStatic) {
                Class<?> clazz = this.member.getDeclaringClass();
                if (!clazz.isInstance(thisObj)) {
                    boolean compatible = false;
                    if (thisObj == scope) {
                        Scriptable parentScope = this.getParentScope();
                        if (scope != parentScope) {
                            compatible = clazz.isInstance(parentScope);
                            if (compatible) {
                                thisObj = parentScope;
                            }
                        }
                    }
                    if (!compatible) {
                        throw ScriptRuntime.typeError1(cx, "msg.incompat.call", this.functionName);
                    }
                }
            }
            Object[] invokeArgs;
            if (this.parmsLength == argsLength) {
                invokeArgs = args;
                for (int ix = 0; ix != this.parmsLength; ix++) {
                    Object arg = args[ix];
                    Object converted = convertArg(cx, scope, arg, this.typeTags[ix]);
                    if (arg != converted) {
                        if (invokeArgs == args) {
                            invokeArgs = (Object[]) args.clone();
                        }
                        invokeArgs[ix] = converted;
                    }
                }
            } else if (this.parmsLength == 0) {
                invokeArgs = ScriptRuntime.EMPTY_OBJECTS;
            } else {
                invokeArgs = new Object[this.parmsLength];
                for (int ixx = 0; ixx != this.parmsLength; ixx++) {
                    Object arg = ixx < argsLength ? args[ixx] : Undefined.instance;
                    invokeArgs[ixx] = convertArg(cx, scope, arg, this.typeTags[ixx]);
                }
            }
            if (this.member.isMethod()) {
                result = this.member.invoke(thisObj, invokeArgs, cx, scope);
                checkMethodResult = true;
            } else {
                result = this.member.newInstance(invokeArgs, cx, scope);
            }
        }
        if (checkMethodResult) {
            if (this.hasVoidReturn) {
                result = Undefined.instance;
            } else if (this.returnTypeTag == 0) {
                result = cx.getWrapFactory().wrap(cx, scope, result, null);
            }
        }
        return result;
    }

    @Override
    public Scriptable createObject(Context cx, Scriptable scope) {
        if (!this.member.isCtor() && this.parmsLength != -2) {
            Scriptable result;
            try {
                result = (Scriptable) this.member.getDeclaringClass().newInstance();
            } catch (Exception var5) {
                throw Context.throwAsScriptRuntimeEx(var5, cx);
            }
            result.setPrototype(this.getClassPrototype(cx));
            result.setParentScope(this.getParentScope());
            return result;
        } else {
            return null;
        }
    }

    boolean isVarArgsMethod() {
        return this.parmsLength == -1;
    }

    boolean isVarArgsConstructor() {
        return this.parmsLength == -2;
    }
}