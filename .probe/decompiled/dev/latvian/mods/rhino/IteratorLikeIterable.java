package dev.latvian.mods.rhino;

import java.io.Closeable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorLikeIterable implements Iterable<Object>, Closeable {

    private final Context cx;

    private final Scriptable scope;

    private final Callable next;

    private final Callable returnFunc;

    private final Scriptable iterator;

    private boolean closed;

    public IteratorLikeIterable(Context cx, Scriptable scope, Object target) {
        this.cx = cx;
        this.scope = scope;
        this.next = ScriptRuntime.getPropFunctionAndThis(cx, scope, target, "next");
        this.iterator = cx.lastStoredScriptable();
        Object retObj = ScriptRuntime.getObjectPropNoWarn(cx, scope, target, "return");
        if (retObj != null && !Undefined.isUndefined(retObj)) {
            if (!(retObj instanceof Callable)) {
                throw ScriptRuntime.notFunctionError(cx, target, retObj, "return");
            }
            this.returnFunc = (Callable) retObj;
        } else {
            this.returnFunc = null;
        }
    }

    public void close() {
        if (!this.closed) {
            this.closed = true;
            if (this.returnFunc != null) {
                this.returnFunc.call(this.cx, this.scope, this.iterator, ScriptRuntime.EMPTY_OBJECTS);
            }
        }
    }

    public IteratorLikeIterable.Itr iterator() {
        return new IteratorLikeIterable.Itr();
    }

    public final class Itr implements Iterator<Object> {

        private Object nextVal;

        private boolean isDone;

        public boolean hasNext() {
            Object val = IteratorLikeIterable.this.next.call(IteratorLikeIterable.this.cx, IteratorLikeIterable.this.scope, IteratorLikeIterable.this.iterator, ScriptRuntime.EMPTY_OBJECTS);
            Object doneval = ScriptableObject.getProperty(ScriptableObject.ensureScriptable(val, IteratorLikeIterable.this.cx), "done", IteratorLikeIterable.this.cx);
            if (doneval == Scriptable.NOT_FOUND) {
                doneval = Undefined.instance;
            }
            if (ScriptRuntime.toBoolean(IteratorLikeIterable.this.cx, doneval)) {
                this.isDone = true;
                return false;
            } else {
                this.nextVal = ScriptRuntime.getObjectPropNoWarn(IteratorLikeIterable.this.cx, IteratorLikeIterable.this.scope, val, "value");
                return true;
            }
        }

        public Object next() {
            if (this.isDone) {
                throw new NoSuchElementException();
            } else {
                return this.nextVal;
            }
        }
    }
}