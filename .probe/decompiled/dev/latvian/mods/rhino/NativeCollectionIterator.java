package dev.latvian.mods.rhino;

import java.util.Collections;
import java.util.Iterator;

public class NativeCollectionIterator extends ES6Iterator {

    private final String className;

    private final NativeCollectionIterator.Type type;

    private transient Iterator<Hashtable.Entry> iterator = Collections.emptyIterator();

    static void init(ScriptableObject scope, String tag, boolean sealed, Context cx) {
        init(scope, sealed, new NativeCollectionIterator(tag), tag, cx);
    }

    public NativeCollectionIterator(String tag) {
        this.className = tag;
        this.iterator = Collections.emptyIterator();
        this.type = NativeCollectionIterator.Type.BOTH;
    }

    public NativeCollectionIterator(Scriptable scope, String className, NativeCollectionIterator.Type type, Iterator<Hashtable.Entry> iterator, Context cx) {
        super(scope, className, cx);
        this.className = className;
        this.iterator = iterator;
        this.type = type;
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    protected boolean isDone(Context cx, Scriptable scope) {
        return !this.iterator.hasNext();
    }

    @Override
    protected Object nextValue(Context cx, Scriptable scope) {
        Hashtable.Entry e = (Hashtable.Entry) this.iterator.next();
        return switch(this.type) {
            case KEYS ->
                e.key;
            case VALUES ->
                e.value;
            case BOTH ->
                cx.newArray(scope, new Object[] { e.key, e.value });
        };
    }

    static enum Type {

        KEYS, VALUES, BOTH
    }
}