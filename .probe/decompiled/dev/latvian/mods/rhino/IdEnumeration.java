package dev.latvian.mods.rhino;

import java.util.function.Consumer;

public class IdEnumeration implements Consumer<Object> {

    public Object tempResult;

    Scriptable obj;

    Object[] ids;

    ObjToIntMap used;

    Object currentId;

    int index;

    int enumType;

    boolean enumNumbers;

    IdEnumerationIterator iterator;

    public Boolean next(Context cx) {
        if (this.iterator != null) {
            if (this.enumType == 6) {
                if (this.iterator.enumerationIteratorHasNext(cx, this)) {
                    this.currentId = this.tempResult;
                    this.tempResult = null;
                    return Boolean.TRUE;
                } else {
                    this.tempResult = null;
                    return Boolean.FALSE;
                }
            } else {
                Boolean var10;
                try {
                    if (this.iterator.enumerationIteratorNext(cx, this)) {
                        this.currentId = this.tempResult;
                    }
                    return Boolean.TRUE;
                } catch (JavaScriptException var7) {
                    if (!(var7.getValue() instanceof NativeIterator.StopIteration)) {
                        throw var7;
                    }
                    var10 = Boolean.FALSE;
                } finally {
                    this.tempResult = null;
                }
                return var10;
            }
        } else {
            while (true) {
                if (this.obj == null) {
                    return Boolean.FALSE;
                }
                if (this.index != this.ids.length) {
                    Object id = this.ids[this.index++];
                    if ((this.used == null || !this.used.has(id)) && !(id instanceof Symbol)) {
                        if (id instanceof String strId) {
                            if (this.obj.has(cx, strId, this.obj)) {
                                this.currentId = strId;
                                break;
                            }
                        } else {
                            int intId = ((Number) id).intValue();
                            if (this.obj.has(cx, intId, this.obj)) {
                                this.currentId = this.enumNumbers ? intId : String.valueOf(intId);
                                break;
                            }
                        }
                    }
                } else {
                    this.obj = this.obj.getPrototype(cx);
                    this.changeObject(cx);
                }
            }
            return Boolean.TRUE;
        }
    }

    public void changeObject(Context cx) {
        Object[] nids = null;
        while (this.obj != null) {
            nids = this.obj.getIds(cx);
            if (nids.length != 0) {
                break;
            }
            this.obj = this.obj.getPrototype(cx);
        }
        if (this.obj != null && this.ids != null) {
            Object[] previous = this.ids;
            int L = previous.length;
            if (this.used == null) {
                this.used = new ObjToIntMap(L);
            }
            for (int i = 0; i != L; i++) {
                this.used.intern(previous[i]);
            }
        }
        this.ids = nids;
        this.index = 0;
    }

    public Object getId(Context cx) {
        if (this.iterator != null) {
            return this.currentId;
        } else {
            switch(this.enumType) {
                case 0:
                case 3:
                    return this.currentId;
                case 1:
                case 4:
                    return this.getValue(cx);
                case 2:
                case 5:
                    Object[] elements = new Object[] { this.currentId, this.getValue(cx) };
                    return cx.newArray(ScriptableObject.getTopLevelScope(this.obj), elements);
                default:
                    throw Kit.codeBug();
            }
        }
    }

    public Object getValue(Context cx) {
        Object result;
        if (ScriptRuntime.isSymbol(this.currentId)) {
            SymbolScriptable so = ScriptableObject.ensureSymbolScriptable(this.obj, cx);
            result = so.get(cx, (Symbol) this.currentId, this.obj);
        } else {
            ScriptRuntime.StringIdOrIndex s = ScriptRuntime.toStringIdOrIndex(cx, this.currentId);
            if (s.stringId == null) {
                result = this.obj.get(cx, s.index, this.obj);
            } else {
                result = this.obj.get(cx, s.stringId, this.obj);
            }
        }
        return result;
    }

    public Object nextExec(Context cx, Scriptable scope) {
        Boolean b = this.next(cx);
        if (!b) {
            throw new JavaScriptException(cx, NativeIterator.getStopIterationObject(scope, cx), null, 0);
        } else {
            return this.getId(cx);
        }
    }

    public void accept(Object o) {
        this.tempResult = o;
    }
}