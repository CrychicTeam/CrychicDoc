package dev.latvian.mods.rhino;

public final class NativeArrayIterator extends ES6Iterator {

    private static final String ITERATOR_TAG = "ArrayIterator";

    private NativeArrayIterator.ArrayIteratorType type;

    private Scriptable arrayLike;

    private int index;

    static void init(ScriptableObject scope, boolean sealed, Context cx) {
        init(scope, sealed, new NativeArrayIterator(), "ArrayIterator", cx);
    }

    private NativeArrayIterator() {
    }

    public NativeArrayIterator(Context cx, Scriptable scope, Scriptable arrayLike, NativeArrayIterator.ArrayIteratorType type) {
        super(scope, "ArrayIterator", cx);
        this.index = 0;
        this.arrayLike = arrayLike;
        this.type = type;
    }

    @Override
    public String getClassName() {
        return "Array Iterator";
    }

    @Override
    protected boolean isDone(Context cx, Scriptable scope) {
        return (long) this.index >= NativeArray.getLengthProperty(cx, this.arrayLike, false);
    }

    @Override
    protected Object nextValue(Context cx, Scriptable scope) {
        if (this.type == NativeArrayIterator.ArrayIteratorType.KEYS) {
            return this.index++;
        } else {
            Object value = this.arrayLike.get(cx, this.index, this.arrayLike);
            if (value == NOT_FOUND) {
                value = Undefined.instance;
            }
            if (this.type == NativeArrayIterator.ArrayIteratorType.ENTRIES) {
                value = cx.newArray(scope, new Object[] { this.index, value });
            }
            this.index++;
            return value;
        }
    }

    @Override
    protected String getTag() {
        return "ArrayIterator";
    }

    public static enum ArrayIteratorType {

        ENTRIES, KEYS, VALUES
    }
}