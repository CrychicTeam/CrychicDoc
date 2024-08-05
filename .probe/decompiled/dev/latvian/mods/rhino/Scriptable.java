package dev.latvian.mods.rhino;

import java.util.function.Consumer;

public interface Scriptable extends IdEnumerationIterator {

    Object NOT_FOUND = UniqueTag.NOT_FOUND;

    String getClassName();

    Object get(Context var1, String var2, Scriptable var3);

    Object get(Context var1, int var2, Scriptable var3);

    boolean has(Context var1, String var2, Scriptable var3);

    boolean has(Context var1, int var2, Scriptable var3);

    void put(Context var1, String var2, Scriptable var3, Object var4);

    void put(Context var1, int var2, Scriptable var3, Object var4);

    void delete(Context var1, String var2);

    void delete(Context var1, int var2);

    Scriptable getPrototype(Context var1);

    void setPrototype(Scriptable var1);

    Scriptable getParentScope();

    void setParentScope(Scriptable var1);

    Object[] getIds(Context var1);

    default Object[] getAllIds(Context cx) {
        return this.getIds(cx);
    }

    Object getDefaultValue(Context var1, Class<?> var2);

    boolean hasInstance(Context var1, Scriptable var2);

    @Override
    default boolean enumerationIteratorHasNext(Context cx, Consumer<Object> currentId) {
        if (!(ScriptableObject.getProperty(this, "next", cx) instanceof Callable f)) {
            throw ScriptRuntime.notFunctionError(cx, this, "next");
        } else {
            Scriptable scope = this.getParentScope();
            Object r = f.call(cx, scope, this, ScriptRuntime.EMPTY_OBJECTS);
            Scriptable iteratorResult = ScriptRuntime.toObject(cx, scope, r);
            currentId.accept(ScriptableObject.getProperty(iteratorResult, "value", cx));
            Object done = ScriptableObject.getProperty(iteratorResult, "done", cx);
            return done == NOT_FOUND || !ScriptRuntime.toBoolean(cx, done);
        }
    }

    @Override
    default boolean enumerationIteratorNext(Context cx, Consumer<Object> currentId) throws JavaScriptException {
        if (ScriptableObject.getProperty(this, "next", cx) instanceof Callable f) {
            Scriptable scope = this.getParentScope();
            currentId.accept(f.call(cx, scope, this, ScriptRuntime.EMPTY_OBJECTS));
            return true;
        } else {
            return false;
        }
    }

    default MemberType getTypeOf() {
        return MemberType.OBJECT;
    }
}