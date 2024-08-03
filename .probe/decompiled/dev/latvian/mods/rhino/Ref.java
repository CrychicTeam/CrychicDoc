package dev.latvian.mods.rhino;

public abstract class Ref {

    public boolean has(Context cx) {
        return true;
    }

    public abstract Object get(Context var1);

    @Deprecated
    public abstract Object set(Context var1, Object var2);

    public Object set(Context cx, Scriptable scope, Object value) {
        return this.set(cx, value);
    }

    public boolean delete(Context cx) {
        return false;
    }
}