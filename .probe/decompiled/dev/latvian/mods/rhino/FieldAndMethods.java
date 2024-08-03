package dev.latvian.mods.rhino;

import java.lang.reflect.Field;

public class FieldAndMethods extends NativeJavaMethod {

    public transient Field field;

    public transient Object javaObject;

    FieldAndMethods(Scriptable scope, MemberBox[] methods, Field field, Context cx) {
        super(methods);
        this.field = field;
        this.setParentScope(scope);
        this.setPrototype(getFunctionPrototype(scope, cx));
    }

    @Override
    public Object getDefaultValue(Context cx, Class<?> hint) {
        if (hint == ScriptRuntime.FunctionClass) {
            return this;
        } else {
            Object rval;
            Class<?> type;
            try {
                rval = this.field.get(this.javaObject);
                type = this.field.getType();
            } catch (IllegalAccessException var6) {
                throw Context.reportRuntimeError1("msg.java.internal.private", this.field.getName(), cx);
            }
            rval = cx.getWrapFactory().wrap(cx, this, rval, type);
            if (rval instanceof Scriptable) {
                rval = ((Scriptable) rval).getDefaultValue(cx, hint);
            }
            return rval;
        }
    }
}