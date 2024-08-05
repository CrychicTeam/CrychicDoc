package dev.latvian.mods.rhino;

public class CustomFunction extends BaseFunction {

    public static final Class<?>[] NO_ARGS = new Class[0];

    private final String functionName;

    private final CustomFunction.Func func;

    private final Class<?>[] argTypes;

    public CustomFunction(String functionName, CustomFunction.Func func, Class<?>[] argTypes) {
        this.functionName = functionName;
        this.func = func;
        this.argTypes = argTypes;
    }

    @Override
    public String getFunctionName() {
        return this.functionName;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        Object[] origArgs = args;
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Object coerced = Context.jsToJava(cx, arg, this.argTypes[i]);
            if (coerced != arg) {
                if (origArgs == args) {
                    args = (Object[]) args.clone();
                }
                args[i] = coerced;
            }
        }
        Object retval = this.func.call(cx, args);
        if (retval == null) {
            return Undefined.instance;
        } else {
            Object wrapped = cx.getWrapFactory().wrap(cx, scope, retval, retval.getClass());
            if (wrapped == null) {
                wrapped = Undefined.instance;
            }
            return wrapped;
        }
    }

    @FunctionalInterface
    public interface Func {

        Object call(Context var1, Object[] var2);
    }

    @FunctionalInterface
    public interface NoArgFunc extends CustomFunction.Func {

        Object call(Context var1);

        @Override
        default Object call(Context cx, Object[] args) {
            return this.call(cx);
        }
    }
}