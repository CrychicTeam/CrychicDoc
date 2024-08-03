package dev.latvian.mods.rhino;

public final class NativeStringIterator extends ES6Iterator {

    private static final String ITERATOR_TAG = "StringIterator";

    private String string;

    private int index;

    static void init(ScriptableObject scope, boolean sealed, Context cx) {
        init(scope, sealed, new NativeStringIterator(), "StringIterator", cx);
    }

    private NativeStringIterator() {
    }

    NativeStringIterator(Context cx, Scriptable scope, Object stringLike) {
        super(scope, "StringIterator", cx);
        this.index = 0;
        this.string = ScriptRuntime.toString(cx, stringLike);
    }

    @Override
    public String getClassName() {
        return "String Iterator";
    }

    @Override
    protected boolean isDone(Context cx, Scriptable scope) {
        return this.index >= this.string.length();
    }

    @Override
    protected Object nextValue(Context cx, Scriptable scope) {
        int newIndex = this.string.offsetByCodePoints(this.index, 1);
        Object value = this.string.substring(this.index, newIndex);
        this.index = newIndex;
        return value;
    }

    @Override
    protected String getTag() {
        return "StringIterator";
    }
}