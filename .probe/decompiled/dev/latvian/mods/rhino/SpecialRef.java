package dev.latvian.mods.rhino;

class SpecialRef extends Ref {

    private static final int SPECIAL_NONE = 0;

    private static final int SPECIAL_PROTO = 1;

    private static final int SPECIAL_PARENT = 2;

    private final Scriptable target;

    private final int type;

    private final String name;

    static Ref createSpecial(Context cx, Scriptable scope, Object object, String name) {
        Scriptable target = ScriptRuntime.toObjectOrNull(cx, object, scope);
        if (target == null) {
            throw ScriptRuntime.undefReadError(cx, object, name);
        } else {
            int type;
            if (name.equals("__proto__")) {
                type = 1;
            } else {
                if (!name.equals("__parent__")) {
                    throw new IllegalArgumentException(name);
                }
                type = 2;
            }
            return new SpecialRef(target, type, name);
        }
    }

    private SpecialRef(Scriptable target, int type, String name) {
        this.target = target;
        this.type = type;
        this.name = name;
    }

    @Override
    public Object get(Context cx) {
        return switch(this.type) {
            case 0 ->
                ScriptRuntime.getObjectProp(cx, this.target, this.name);
            case 1 ->
                this.target.getPrototype(cx);
            case 2 ->
                this.target.getParentScope();
            default ->
                throw Kit.codeBug();
        };
    }

    @Deprecated
    @Override
    public Object set(Context cx, Object value) {
        throw new IllegalStateException();
    }

    @Override
    public Object set(Context cx, Scriptable scope, Object value) {
        switch(this.type) {
            case 0:
                return ScriptRuntime.setObjectProp(cx, this.target, this.name, value);
            case 1:
            case 2:
                Scriptable obj = ScriptRuntime.toObjectOrNull(cx, value, scope);
                if (obj != null) {
                    Scriptable search = obj;
                    do {
                        if (search == this.target) {
                            throw Context.reportRuntimeError1("msg.cyclic.value", this.name, cx);
                        }
                        if (this.type == 1) {
                            search = search.getPrototype(cx);
                        } else {
                            search = search.getParentScope();
                        }
                    } while (search != null);
                }
                if (this.type == 1) {
                    if (this.target instanceof ScriptableObject && !((ScriptableObject) this.target).isExtensible()) {
                        throw ScriptRuntime.typeError0(cx, "msg.not.extensible");
                    }
                    if (value != null && ScriptRuntime.typeof(cx, value) != MemberType.OBJECT || ScriptRuntime.typeof(cx, this.target) != MemberType.OBJECT) {
                        return Undefined.instance;
                    }
                    this.target.setPrototype(obj);
                } else {
                    this.target.setParentScope(obj);
                }
                return obj;
            default:
                throw Kit.codeBug();
        }
    }

    @Override
    public boolean has(Context cx) {
        return this.type == 0 ? ScriptRuntime.hasObjectElem(cx, this.target, this.name) : true;
    }

    @Override
    public boolean delete(Context cx) {
        return this.type == 0 ? ScriptRuntime.deleteObjectElem(cx, this.target, this.name) : false;
    }
}