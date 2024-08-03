package dev.latvian.mods.rhino;

import java.util.EnumMap;

public class TopLevel extends IdScriptableObject {

    private EnumMap<TopLevel.Builtins, BaseFunction> ctors;

    private EnumMap<TopLevel.NativeErrors, BaseFunction> errors;

    public static Function getBuiltinCtor(Context cx, Scriptable scope, TopLevel.Builtins type) {
        assert scope.getParentScope() == null;
        if (scope instanceof TopLevel) {
            Function result = ((TopLevel) scope).getBuiltinCtor(type);
            if (result != null) {
                return result;
            }
        }
        String typeName;
        if (type == TopLevel.Builtins.GeneratorFunction) {
            typeName = "__GeneratorFunction";
        } else {
            typeName = type.name();
        }
        return ScriptRuntime.getExistingCtor(cx, scope, typeName);
    }

    static Function getNativeErrorCtor(Context cx, Scriptable scope, TopLevel.NativeErrors type) {
        assert scope.getParentScope() == null;
        if (scope instanceof TopLevel) {
            Function result = ((TopLevel) scope).getNativeErrorCtor(type);
            if (result != null) {
                return result;
            }
        }
        return ScriptRuntime.getExistingCtor(cx, scope, type.name());
    }

    public static Scriptable getBuiltinPrototype(Scriptable scope, TopLevel.Builtins type, Context cx) {
        assert scope.getParentScope() == null;
        if (scope instanceof TopLevel) {
            Scriptable result = ((TopLevel) scope).getBuiltinPrototype(cx, type);
            if (result != null) {
                return result;
            }
        }
        String typeName;
        if (type == TopLevel.Builtins.GeneratorFunction) {
            typeName = "__GeneratorFunction";
        } else {
            typeName = type.name();
        }
        return getClassPrototype(scope, typeName, cx);
    }

    @Override
    public String getClassName() {
        return "global";
    }

    public void cacheBuiltins(Scriptable scope, boolean sealed, Context cx) {
        this.ctors = new EnumMap(TopLevel.Builtins.class);
        for (TopLevel.Builtins builtin : TopLevel.Builtins.values()) {
            Object value = getProperty(this, builtin.name(), cx);
            if (value instanceof BaseFunction) {
                this.ctors.put(builtin, (BaseFunction) value);
            } else if (builtin == TopLevel.Builtins.GeneratorFunction) {
                this.ctors.put(builtin, (BaseFunction) BaseFunction.initAsGeneratorFunction(scope, sealed, cx));
            }
        }
        this.errors = new EnumMap(TopLevel.NativeErrors.class);
        for (TopLevel.NativeErrors error : TopLevel.NativeErrors.values()) {
            Object value = getProperty(this, error.name(), cx);
            if (value instanceof BaseFunction) {
                this.errors.put(error, (BaseFunction) value);
            }
        }
    }

    public BaseFunction getBuiltinCtor(TopLevel.Builtins type) {
        return this.ctors != null ? (BaseFunction) this.ctors.get(type) : null;
    }

    BaseFunction getNativeErrorCtor(TopLevel.NativeErrors type) {
        return this.errors != null ? (BaseFunction) this.errors.get(type) : null;
    }

    public Scriptable getBuiltinPrototype(Context cx, TopLevel.Builtins type) {
        BaseFunction func = this.getBuiltinCtor(type);
        Object proto = func != null ? func.getPrototypeProperty(cx) : null;
        return proto instanceof Scriptable ? (Scriptable) proto : null;
    }

    public static enum Builtins {

        Object,
        Array,
        Function,
        String,
        Number,
        Boolean,
        RegExp,
        Error,
        Symbol,
        GeneratorFunction
    }

    static enum NativeErrors {

        Error,
        EvalError,
        RangeError,
        ReferenceError,
        SyntaxError,
        TypeError,
        URIError,
        InternalError,
        JavaException
    }
}