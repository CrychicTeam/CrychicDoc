package dev.latvian.mods.rhino;

public class JavaScriptException extends RhinoException {

    private static final long serialVersionUID = -7666130513694669293L;

    private final Context localContext;

    private final Object value;

    public JavaScriptException(Context cx, Object value, String sourceName, int lineNumber) {
        super(cx);
        this.localContext = cx;
        this.recordErrorOrigin(sourceName, lineNumber, null, 0);
        this.value = value;
    }

    @Override
    public String details() {
        if (this.value == null) {
            return "null";
        } else if (this.value instanceof NativeError) {
            return this.value.toString();
        } else {
            try {
                return ScriptRuntime.toString(this.localContext, this.value);
            } catch (RuntimeException var2) {
                return this.value instanceof Scriptable ? ScriptRuntime.defaultObjectToString((Scriptable) this.value) : this.value.toString();
            }
        }
    }

    public Object getValue() {
        return this.value;
    }
}