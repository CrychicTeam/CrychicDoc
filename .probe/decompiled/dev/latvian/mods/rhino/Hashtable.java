package dev.latvian.mods.rhino;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Hashtable implements Iterable<Hashtable.Entry> {

    private final Context localContext;

    private final HashMap<Object, Hashtable.Entry> map = new HashMap();

    private Hashtable.Entry first = null;

    private Hashtable.Entry last = null;

    private static Hashtable.Entry makeDummy(Context cx) {
        Hashtable.Entry d = new Hashtable.Entry(cx);
        d.clear();
        return d;
    }

    public Hashtable(Context cx) {
        this.localContext = cx;
    }

    public int size() {
        return this.map.size();
    }

    public void put(Context cx, Object key, Object value) {
        Hashtable.Entry nv = new Hashtable.Entry(cx, key, value);
        Hashtable.Entry ev = (Hashtable.Entry) this.map.putIfAbsent(nv, nv);
        if (ev == null) {
            if (this.first == null) {
                this.first = this.last = nv;
            } else {
                this.last.next = nv;
                nv.prev = this.last;
                this.last = nv;
            }
        } else {
            ev.value = value;
        }
    }

    public Object get(Context cx, Object key) {
        Hashtable.Entry e = new Hashtable.Entry(cx, key, null);
        Hashtable.Entry v = (Hashtable.Entry) this.map.get(e);
        return v == null ? null : v.value;
    }

    public boolean has(Context cx, Object key) {
        Hashtable.Entry e = new Hashtable.Entry(cx, key, null);
        return this.map.containsKey(e);
    }

    public Object delete(Context cx, Object key) {
        Hashtable.Entry e = new Hashtable.Entry(cx, key, null);
        Hashtable.Entry v = (Hashtable.Entry) this.map.remove(e);
        if (v == null) {
            return null;
        } else {
            if (v == this.first) {
                if (v == this.last) {
                    v.clear();
                    v.prev = null;
                } else {
                    this.first = v.next;
                    this.first.prev = null;
                    if (this.first.next != null) {
                        this.first.next.prev = this.first;
                    }
                }
            } else {
                Hashtable.Entry prev = v.prev;
                prev.next = v.next;
                v.prev = null;
                if (v.next != null) {
                    v.next.prev = prev;
                } else {
                    assert v == this.last;
                    this.last = prev;
                }
            }
            return v.clear();
        }
    }

    public void clear(Context cx) {
        Iterator<Hashtable.Entry> it = this.iterator();
        it.forEachRemaining(Hashtable.Entry::clear);
        if (this.first != null) {
            Hashtable.Entry dummy = makeDummy(cx);
            this.last.next = dummy;
            this.first = this.last = dummy;
        }
        this.map.clear();
    }

    public Iterator<Hashtable.Entry> iterator() {
        return new Hashtable.Iter(this.localContext, this.first);
    }

    public static final class Entry {

        private final Context localContext;

        private final int hashCode;

        Object key;

        Object value;

        private boolean deleted;

        private Hashtable.Entry next;

        private Hashtable.Entry prev;

        Entry(Context cx) {
            this.localContext = cx;
            this.hashCode = 0;
        }

        Entry(Context cx, Object k, Object value) {
            this.localContext = cx;
            if (k instanceof Number && !(k instanceof Double)) {
                this.key = ((Number) k).doubleValue();
            } else if (k instanceof ConsString) {
                this.key = k.toString();
            } else {
                this.key = k;
            }
            if (this.key == null) {
                this.hashCode = 0;
            } else if (k.equals(ScriptRuntime.negativeZeroObj)) {
                this.hashCode = 0;
            } else {
                this.hashCode = this.key.hashCode();
            }
            this.value = value;
        }

        public Object key() {
            return this.key;
        }

        public Object value() {
            return this.value;
        }

        Object clear() {
            Object ret = this.value;
            this.key = Undefined.instance;
            this.value = Undefined.instance;
            this.deleted = true;
            return ret;
        }

        public int hashCode() {
            return this.hashCode;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            } else {
                try {
                    return ScriptRuntime.sameZero(this.localContext, this.key, ((Hashtable.Entry) o).key);
                } catch (ClassCastException var3) {
                    return false;
                }
            }
        }
    }

    private static final class Iter implements Iterator<Hashtable.Entry> {

        private Hashtable.Entry pos;

        Iter(Context cx, Hashtable.Entry start) {
            Hashtable.Entry dummy = Hashtable.makeDummy(cx);
            dummy.next = start;
            this.pos = dummy;
        }

        private void skipDeleted() {
            while (this.pos.next != null && this.pos.next.deleted) {
                this.pos = this.pos.next;
            }
        }

        public boolean hasNext() {
            this.skipDeleted();
            return this.pos != null && this.pos.next != null;
        }

        public Hashtable.Entry next() {
            this.skipDeleted();
            if (this.pos != null && this.pos.next != null) {
                Hashtable.Entry e = this.pos.next;
                this.pos = this.pos.next;
                return e;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}