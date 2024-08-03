package dev.latvian.mods.rhino;

public abstract class IdScriptableObject extends ScriptableObject implements IdFunctionCall {

    private transient IdScriptableObject.PrototypeValues prototypeValues;

    protected static int instanceIdInfo(int attributes, int id) {
        return attributes << 16 | id;
    }

    protected static EcmaError incompatibleCallError(IdFunctionObject f, Context cx) {
        throw ScriptRuntime.typeError1(cx, "msg.incompat.call", f.getFunctionName());
    }

    public IdScriptableObject() {
    }

    public IdScriptableObject(Scriptable scope, Scriptable prototype) {
        super(scope, prototype);
    }

    protected final boolean defaultHas(Context cx, String name) {
        return super.has(cx, name, this);
    }

    protected final Object defaultGet(Context cx, String name) {
        return super.get(cx, name, this);
    }

    protected final void defaultPut(Context cx, String name, Object value) {
        super.put(cx, name, this, value);
    }

    @Override
    public boolean has(Context cx, String name, Scriptable start) {
        int info = this.findInstanceIdInfo(name, cx);
        if (info != 0) {
            int attr = info >>> 16;
            if ((attr & 4) != 0) {
                return true;
            } else {
                int id = info & 65535;
                return NOT_FOUND != this.getInstanceIdValue(id, cx);
            }
        } else {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    return this.prototypeValues.has(id);
                }
            }
            return super.has(cx, name, start);
        }
    }

    @Override
    public boolean has(Context cx, Symbol key, Scriptable start) {
        int info = this.findInstanceIdInfo(key);
        if (info != 0) {
            int attr = info >>> 16;
            if ((attr & 4) != 0) {
                return true;
            } else {
                int id = info & 65535;
                return NOT_FOUND != this.getInstanceIdValue(id, cx);
            }
        } else {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(key);
                if (id != 0) {
                    return this.prototypeValues.has(id);
                }
            }
            return super.has(cx, key, start);
        }
    }

    @Override
    public Object get(Context cx, String name, Scriptable start) {
        Object value = super.get(cx, name, start);
        if (value != NOT_FOUND) {
            return value;
        } else {
            int info = this.findInstanceIdInfo(name, cx);
            if (info != 0) {
                int id = info & 65535;
                value = this.getInstanceIdValue(id, cx);
                if (value != NOT_FOUND) {
                    return value;
                }
            }
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    return this.prototypeValues.get(id, cx);
                }
            }
            return NOT_FOUND;
        }
    }

    @Override
    public Object get(Context cx, Symbol key, Scriptable start) {
        Object value = super.get(cx, key, start);
        if (value != NOT_FOUND) {
            return value;
        } else {
            int info = this.findInstanceIdInfo(key);
            if (info != 0) {
                int id = info & 65535;
                value = this.getInstanceIdValue(id, cx);
                if (value != NOT_FOUND) {
                    return value;
                }
            }
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(key);
                if (id != 0) {
                    return this.prototypeValues.get(id, cx);
                }
            }
            return NOT_FOUND;
        }
    }

    @Override
    public void put(Context cx, String name, Scriptable start, Object value) {
        int info = this.findInstanceIdInfo(name, cx);
        if (info != 0) {
            if (start == this && this.isSealed(cx)) {
                throw Context.reportRuntimeError1("msg.modify.sealed", name, cx);
            } else {
                int attr = info >>> 16;
                if ((attr & 1) == 0) {
                    if (start == this) {
                        int id = info & 65535;
                        this.setInstanceIdValue(id, value, cx);
                    } else {
                        start.put(cx, name, start, value);
                    }
                }
            }
        } else {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    if (start == this && this.isSealed(cx)) {
                        throw Context.reportRuntimeError1("msg.modify.sealed", name, cx);
                    }
                    this.prototypeValues.set(id, start, value, cx);
                    return;
                }
            }
            super.put(cx, name, start, value);
        }
    }

    @Override
    public void put(Context cx, Symbol key, Scriptable start, Object value) {
        int info = this.findInstanceIdInfo(key);
        if (info != 0) {
            if (start == this && this.isSealed(cx)) {
                throw Context.reportRuntimeError0("msg.modify.sealed", cx);
            } else {
                int attr = info >>> 16;
                if ((attr & 1) == 0) {
                    if (start == this) {
                        int id = info & 65535;
                        this.setInstanceIdValue(id, value, cx);
                    } else {
                        ensureSymbolScriptable(start, cx).put(cx, key, start, value);
                    }
                }
            }
        } else {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(key);
                if (id != 0) {
                    if (start == this && this.isSealed(cx)) {
                        throw Context.reportRuntimeError0("msg.modify.sealed", cx);
                    }
                    this.prototypeValues.set(id, start, value, cx);
                    return;
                }
            }
            super.put(cx, key, start, value);
        }
    }

    @Override
    public void delete(Context cx, String name) {
        int info = this.findInstanceIdInfo(name, cx);
        if (info != 0 && !this.isSealed(cx)) {
            int attr = info >>> 16;
            if ((attr & 4) != 0) {
                if (cx.isStrictMode()) {
                    throw ScriptRuntime.typeError1(cx, "msg.delete.property.with.configurable.false", name);
                }
            } else {
                int id = info & 65535;
                this.setInstanceIdValue(id, NOT_FOUND, cx);
            }
        } else {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    if (!this.isSealed(cx)) {
                        this.prototypeValues.delete(id, cx);
                    }
                    return;
                }
            }
            super.delete(cx, name);
        }
    }

    @Override
    public void delete(Context cx, Symbol key) {
        int info = this.findInstanceIdInfo(key);
        if (info != 0 && !this.isSealed(cx)) {
            int attr = info >>> 16;
            if ((attr & 4) != 0) {
                if (cx.isStrictMode()) {
                    throw ScriptRuntime.typeError0(cx, "msg.delete.property.with.configurable.false");
                }
            } else {
                int id = info & 65535;
                this.setInstanceIdValue(id, NOT_FOUND, cx);
            }
        } else {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(key);
                if (id != 0) {
                    if (!this.isSealed(cx)) {
                        this.prototypeValues.delete(id, cx);
                    }
                    return;
                }
            }
            super.delete(cx, key);
        }
    }

    @Override
    public int getAttributes(Context cx, String name) {
        int info = this.findInstanceIdInfo(name, cx);
        if (info != 0) {
            return info >>> 16;
        } else {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    return this.prototypeValues.getAttributes(id, cx);
                }
            }
            return super.getAttributes(cx, name);
        }
    }

    @Override
    public int getAttributes(Context cx, Symbol key) {
        int info = this.findInstanceIdInfo(key);
        if (info != 0) {
            return info >>> 16;
        } else {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(key);
                if (id != 0) {
                    return this.prototypeValues.getAttributes(id, cx);
                }
            }
            return super.getAttributes(cx, key);
        }
    }

    @Override
    public void setAttributes(Context cx, String name, int attributes) {
        ScriptableObject.checkValidAttributes(attributes);
        int info = this.findInstanceIdInfo(name, cx);
        if (info != 0) {
            int id = info & 65535;
            int currentAttributes = info >>> 16;
            if (attributes != currentAttributes) {
                this.setInstanceIdAttributes(id, attributes, cx);
            }
        } else {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    this.prototypeValues.setAttributes(id, attributes, cx);
                    return;
                }
            }
            super.setAttributes(cx, name, attributes);
        }
    }

    @Override
    Object[] getIds(Context cx, boolean getNonEnumerable, boolean getSymbols) {
        Object[] result = super.getIds(cx, getNonEnumerable, getSymbols);
        if (this.prototypeValues != null) {
            result = this.prototypeValues.getNames(getNonEnumerable, getSymbols, result, cx);
        }
        int maxInstanceId = this.getMaxInstanceId();
        if (maxInstanceId != 0) {
            Object[] ids = null;
            int count = 0;
            for (int id = maxInstanceId; id != 0; id--) {
                String name = this.getInstanceIdName(id);
                int info = this.findInstanceIdInfo(name, cx);
                if (info != 0) {
                    int attr = info >>> 16;
                    if (((attr & 4) != 0 || NOT_FOUND != this.getInstanceIdValue(id, cx)) && (getNonEnumerable || (attr & 2) == 0)) {
                        if (count == 0) {
                            ids = new Object[id];
                        }
                        ids[count++] = name;
                    }
                }
            }
            if (count != 0) {
                if (result.length == 0 && ids.length == count) {
                    result = ids;
                } else {
                    Object[] tmp = new Object[result.length + count];
                    System.arraycopy(result, 0, tmp, 0, result.length);
                    System.arraycopy(ids, 0, tmp, result.length, count);
                    result = tmp;
                }
            }
        }
        return result;
    }

    protected int getMaxInstanceId() {
        return 0;
    }

    protected int findInstanceIdInfo(String name, Context cx) {
        return 0;
    }

    protected int findInstanceIdInfo(Symbol key) {
        return 0;
    }

    protected String getInstanceIdName(int id) {
        throw new IllegalArgumentException(String.valueOf(id));
    }

    protected Object getInstanceIdValue(int id, Context cx) {
        throw new IllegalStateException(String.valueOf(id));
    }

    protected void setInstanceIdValue(int id, Object value, Context cx) {
        throw new IllegalStateException(String.valueOf(id));
    }

    protected void setInstanceIdAttributes(int id, int attr, Context cx) {
        throw ScriptRuntime.constructError(cx, "InternalError", "Changing attributes not supported for " + this.getClassName() + " " + this.getInstanceIdName(id) + " property");
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        throw f.unknown();
    }

    public final IdFunctionObject exportAsJSClass(int maxPrototypeId, Scriptable scope, boolean sealed, Context cx) {
        if (scope != this && scope != null) {
            this.setParentScope(scope);
            this.setPrototype(getObjectPrototype(scope, cx));
        }
        this.activatePrototypeMap(maxPrototypeId);
        IdFunctionObject ctor = this.prototypeValues.createPrecachedConstructor(cx);
        if (sealed) {
            this.sealObject(cx);
        }
        this.fillConstructorProperties(ctor, cx);
        if (sealed) {
            ctor.sealObject(cx);
        }
        ctor.exportAsScopeProperty(cx);
        return ctor;
    }

    public final boolean hasPrototypeMap() {
        return this.prototypeValues != null;
    }

    public final void activatePrototypeMap(int maxPrototypeId) {
        IdScriptableObject.PrototypeValues values = new IdScriptableObject.PrototypeValues(this, maxPrototypeId);
        synchronized (this) {
            if (this.prototypeValues != null) {
                throw new IllegalStateException();
            } else {
                this.prototypeValues = values;
            }
        }
    }

    public final IdFunctionObject initPrototypeMethod(Object tag, int id, String name, int arity, Context cx) {
        return this.initPrototypeMethod(tag, id, name, name, arity, cx);
    }

    public final IdFunctionObject initPrototypeMethod(Object tag, int id, String propertyName, String functionName, int arity, Context cx) {
        Scriptable scope = ScriptableObject.getTopLevelScope(this);
        IdFunctionObject function = this.newIdFunction(tag, id, functionName != null ? functionName : propertyName, arity, scope, cx);
        this.prototypeValues.initValue(id, propertyName, function, 2);
        return function;
    }

    public final IdFunctionObject initPrototypeMethod(Object tag, int id, Symbol key, String functionName, int arity, Context cx) {
        Scriptable scope = ScriptableObject.getTopLevelScope(this);
        IdFunctionObject function = this.newIdFunction(tag, id, functionName, arity, scope, cx);
        this.prototypeValues.initValue(id, key, function, 2);
        return function;
    }

    public final void initPrototypeConstructor(IdFunctionObject f, Context cx) {
        int id = this.prototypeValues.constructorId;
        if (id == 0) {
            throw new IllegalStateException();
        } else if (f.methodId() != id) {
            throw new IllegalArgumentException();
        } else {
            if (this.isSealed(cx)) {
                f.sealObject(cx);
            }
            this.prototypeValues.initValue(id, "constructor", f, 2);
        }
    }

    public final void initPrototypeValue(int id, String name, Object value, int attributes) {
        this.prototypeValues.initValue(id, name, value, attributes);
    }

    public final void initPrototypeValue(int id, Symbol key, Object value, int attributes) {
        this.prototypeValues.initValue(id, key, value, attributes);
    }

    protected void initPrototypeId(int id, Context cx) {
        throw new IllegalArgumentException(String.valueOf(id));
    }

    protected int findPrototypeId(String name) {
        throw new IllegalArgumentException(name);
    }

    protected int findPrototypeId(Symbol key) {
        return 0;
    }

    protected void fillConstructorProperties(IdFunctionObject ctor, Context cx) {
    }

    protected void addIdFunctionProperty(Scriptable obj, Object tag, int id, String name, int arity, Context cx) {
        Scriptable scope = ScriptableObject.getTopLevelScope(obj);
        IdFunctionObject f = this.newIdFunction(tag, id, name, arity, scope, cx);
        f.addAsProperty(obj, cx);
    }

    private IdFunctionObject newIdFunction(Object tag, int id, String name, int arity, Scriptable scope, Context cx) {
        IdFunctionObject function = new IdFunctionObjectES6(this, tag, id, name, arity, scope);
        if (this.isSealed(cx)) {
            function.sealObject(cx);
        }
        return function;
    }

    @Override
    public void defineOwnProperty(Context cx, Object key, ScriptableObject desc) {
        if (key instanceof String name) {
            int info = this.findInstanceIdInfo(name, cx);
            if (info != 0) {
                int id = info & 65535;
                if (!this.isAccessorDescriptor(cx, desc)) {
                    this.checkPropertyDefinition(cx, desc);
                    ScriptableObject current = this.getOwnPropertyDescriptor(cx, key);
                    this.checkPropertyChange(cx, name, current, desc);
                    int attr = info >>> 16;
                    Object value = getProperty(desc, "value", cx);
                    if (value != NOT_FOUND && (attr & 1) == 0) {
                        Object currentValue = this.getInstanceIdValue(id, cx);
                        if (!this.sameValue(cx, value, currentValue)) {
                            this.setInstanceIdValue(id, value, cx);
                        }
                    }
                    this.setAttributes(cx, name, this.applyDescriptorToAttributeBitset(cx, attr, desc));
                    return;
                }
                this.delete(cx, id);
            }
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    if (!this.isAccessorDescriptor(cx, desc)) {
                        this.checkPropertyDefinition(cx, desc);
                        ScriptableObject current = this.getOwnPropertyDescriptor(cx, key);
                        this.checkPropertyChange(cx, name, current, desc);
                        int attr = this.prototypeValues.getAttributes(id, cx);
                        Object value = getProperty(desc, "value", cx);
                        if (value != NOT_FOUND && (attr & 1) == 0) {
                            Object currentValue = this.prototypeValues.get(id, cx);
                            if (!this.sameValue(cx, value, currentValue)) {
                                this.prototypeValues.set(id, this, value, cx);
                            }
                        }
                        this.prototypeValues.setAttributes(id, this.applyDescriptorToAttributeBitset(cx, attr, desc), cx);
                        if (super.has(cx, name, this)) {
                            super.delete(cx, name);
                        }
                        return;
                    }
                    this.prototypeValues.delete(id, cx);
                }
            }
        }
        super.defineOwnProperty(cx, key, desc);
    }

    @Override
    protected ScriptableObject getOwnPropertyDescriptor(Context cx, Object id) {
        ScriptableObject desc = super.getOwnPropertyDescriptor(cx, id);
        if (desc == null) {
            if (id instanceof String) {
                desc = this.getBuiltInDescriptor((String) id, cx);
            } else if (ScriptRuntime.isSymbol(id)) {
                desc = this.getBuiltInDescriptor(((NativeSymbol) id).getKey(), cx);
            }
        }
        return desc;
    }

    private ScriptableObject getBuiltInDescriptor(String name, Context cx) {
        Object value = null;
        int attr = 0;
        Scriptable scope = this.getParentScope();
        if (scope == null) {
            scope = this;
        }
        int info = this.findInstanceIdInfo(name, cx);
        if (info != 0) {
            int id = info & 65535;
            value = this.getInstanceIdValue(id, cx);
            attr = info >>> 16;
            return buildDataDescriptor(scope, value, attr, cx);
        } else {
            if (this.prototypeValues != null) {
                int id = this.prototypeValues.findId(name);
                if (id != 0) {
                    value = this.prototypeValues.get(id, cx);
                    attr = this.prototypeValues.getAttributes(id, cx);
                    return buildDataDescriptor(scope, value, attr, cx);
                }
            }
            return null;
        }
    }

    private ScriptableObject getBuiltInDescriptor(Symbol key, Context cx) {
        Object value = null;
        int attr = 0;
        Scriptable scope = this.getParentScope();
        if (scope == null) {
            scope = this;
        }
        if (this.prototypeValues != null) {
            int id = this.prototypeValues.findId(key);
            if (id != 0) {
                value = this.prototypeValues.get(id, cx);
                attr = this.prototypeValues.getAttributes(id, cx);
                return buildDataDescriptor(scope, value, attr, cx);
            }
        }
        return null;
    }

    private static final class PrototypeValues {

        private static final int NAME_SLOT = 1;

        private static final int SLOT_SPAN = 2;

        private final IdScriptableObject obj;

        private final int maxId;

        int constructorId;

        private Object[] valueArray;

        private short[] attributeArray;

        private IdFunctionObject constructor;

        private short constructorAttrs;

        PrototypeValues(IdScriptableObject obj, int maxId) {
            if (obj == null) {
                throw new IllegalArgumentException();
            } else if (maxId < 1) {
                throw new IllegalArgumentException();
            } else {
                this.obj = obj;
                this.maxId = maxId;
            }
        }

        int getMaxId() {
            return this.maxId;
        }

        void initValue(int id, String name, Object value, int attributes) {
            if (1 > id || id > this.maxId) {
                throw new IllegalArgumentException();
            } else if (name == null) {
                throw new IllegalArgumentException();
            } else if (value == Scriptable.NOT_FOUND) {
                throw new IllegalArgumentException();
            } else {
                ScriptableObject.checkValidAttributes(attributes);
                if (this.obj.findPrototypeId(name) != id) {
                    throw new IllegalArgumentException(name);
                } else if (id != this.constructorId) {
                    this.initSlot(id, name, value, attributes);
                } else if (!(value instanceof IdFunctionObject)) {
                    throw new IllegalArgumentException("consructor should be initialized with IdFunctionObject");
                } else {
                    this.constructor = (IdFunctionObject) value;
                    this.constructorAttrs = (short) attributes;
                }
            }
        }

        void initValue(int id, Symbol key, Object value, int attributes) {
            if (1 > id || id > this.maxId) {
                throw new IllegalArgumentException();
            } else if (key == null) {
                throw new IllegalArgumentException();
            } else if (value == Scriptable.NOT_FOUND) {
                throw new IllegalArgumentException();
            } else {
                ScriptableObject.checkValidAttributes(attributes);
                if (this.obj.findPrototypeId(key) != id) {
                    throw new IllegalArgumentException(key.toString());
                } else if (id != this.constructorId) {
                    this.initSlot(id, key, value, attributes);
                } else if (!(value instanceof IdFunctionObject)) {
                    throw new IllegalArgumentException("consructor should be initialized with IdFunctionObject");
                } else {
                    this.constructor = (IdFunctionObject) value;
                    this.constructorAttrs = (short) attributes;
                }
            }
        }

        private void initSlot(int id, Object name, Object value, int attributes) {
            Object[] array = this.valueArray;
            if (array == null) {
                throw new IllegalStateException();
            } else {
                if (value == null) {
                    value = UniqueTag.NULL_VALUE;
                }
                int index = (id - 1) * 2;
                synchronized (this) {
                    Object value2 = array[index];
                    if (value2 == null) {
                        array[index] = value;
                        array[index + 1] = name;
                        this.attributeArray[id - 1] = (short) attributes;
                    } else if (!name.equals(array[index + 1])) {
                        throw new IllegalStateException();
                    }
                }
            }
        }

        IdFunctionObject createPrecachedConstructor(Context cx) {
            if (this.constructorId != 0) {
                throw new IllegalStateException();
            } else {
                this.constructorId = this.obj.findPrototypeId("constructor");
                if (this.constructorId == 0) {
                    throw new IllegalStateException("No id for constructor property");
                } else {
                    this.obj.initPrototypeId(this.constructorId, cx);
                    if (this.constructor == null) {
                        throw new IllegalStateException(this.obj.getClass().getName() + ".initPrototypeId() did not initialize id=" + this.constructorId);
                    } else {
                        this.constructor.initFunction(this.obj.getClassName(), ScriptableObject.getTopLevelScope(this.obj));
                        this.constructor.markAsConstructor(this.obj);
                        return this.constructor;
                    }
                }
            }
        }

        int findId(String name) {
            return this.obj.findPrototypeId(name);
        }

        int findId(Symbol key) {
            return this.obj.findPrototypeId(key);
        }

        boolean has(int id) {
            Object[] array = this.valueArray;
            if (array == null) {
                return true;
            } else {
                int valueSlot = (id - 1) * 2;
                Object value = array[valueSlot];
                return value == null ? true : value != Scriptable.NOT_FOUND;
            }
        }

        Object get(int id, Context cx) {
            Object value = this.ensureId(id, cx);
            if (value == UniqueTag.NULL_VALUE) {
                value = null;
            }
            return value;
        }

        void set(int id, Scriptable start, Object value, Context cx) {
            if (value == Scriptable.NOT_FOUND) {
                throw new IllegalArgumentException();
            } else {
                this.ensureId(id, cx);
                int attr = this.attributeArray[id - 1];
                if ((attr & 1) == 0) {
                    if (start == this.obj) {
                        if (value == null) {
                            value = UniqueTag.NULL_VALUE;
                        }
                        int valueSlot = (id - 1) * 2;
                        synchronized (this) {
                            this.valueArray[valueSlot] = value;
                        }
                    } else {
                        int nameSlot = (id - 1) * 2 + 1;
                        Object name = this.valueArray[nameSlot];
                        if (name instanceof Symbol) {
                            if (start instanceof SymbolScriptable) {
                                ((SymbolScriptable) start).put(cx, (Symbol) name, start, value);
                            }
                        } else {
                            start.put(cx, (String) name, start, value);
                        }
                    }
                }
            }
        }

        void delete(int id, Context cx) {
            this.ensureId(id, cx);
            int attr = this.attributeArray[id - 1];
            if ((attr & 4) != 0) {
                if (cx.isStrictMode()) {
                    int nameSlot = (id - 1) * 2 + 1;
                    String name = (String) this.valueArray[nameSlot];
                    throw ScriptRuntime.typeError1(cx, "msg.delete.property.with.configurable.false", name);
                }
            } else {
                int valueSlot = (id - 1) * 2;
                synchronized (this) {
                    this.valueArray[valueSlot] = Scriptable.NOT_FOUND;
                    this.attributeArray[id - 1] = 0;
                }
            }
        }

        int getAttributes(int id, Context cx) {
            this.ensureId(id, cx);
            return this.attributeArray[id - 1];
        }

        void setAttributes(int id, int attributes, Context cx) {
            ScriptableObject.checkValidAttributes(attributes);
            this.ensureId(id, cx);
            synchronized (this) {
                this.attributeArray[id - 1] = (short) attributes;
            }
        }

        Object[] getNames(boolean getAll, boolean getSymbols, Object[] extraEntries, Context cx) {
            Object[] names = null;
            int count = 0;
            for (int id = 1; id <= this.maxId; id++) {
                Object value = this.ensureId(id, cx);
                if ((getAll || (this.attributeArray[id - 1] & 2) == 0) && value != Scriptable.NOT_FOUND) {
                    int nameSlot = (id - 1) * 2 + 1;
                    Object name = this.valueArray[nameSlot];
                    if (name instanceof String) {
                        if (names == null) {
                            names = new Object[this.maxId];
                        }
                        names[count++] = name;
                    } else if (getSymbols && name instanceof Symbol) {
                        if (names == null) {
                            names = new Object[this.maxId];
                        }
                        names[count++] = name.toString();
                    }
                }
            }
            if (count == 0) {
                return extraEntries;
            } else if (extraEntries != null && extraEntries.length != 0) {
                int extra = extraEntries.length;
                Object[] tmp = new Object[extra + count];
                System.arraycopy(extraEntries, 0, tmp, 0, extra);
                System.arraycopy(names, 0, tmp, extra, count);
                return tmp;
            } else {
                if (count != names.length) {
                    Object[] tmp = new Object[count];
                    System.arraycopy(names, 0, tmp, 0, count);
                    names = tmp;
                }
                return names;
            }
        }

        private Object ensureId(int id, Context cx) {
            Object[] array = this.valueArray;
            if (array == null) {
                synchronized (this) {
                    array = this.valueArray;
                    if (array == null) {
                        array = new Object[this.maxId * 2];
                        this.valueArray = array;
                        this.attributeArray = new short[this.maxId];
                    }
                }
            }
            int valueSlot = (id - 1) * 2;
            Object value = array[valueSlot];
            if (value == null) {
                if (id == this.constructorId) {
                    this.initSlot(this.constructorId, "constructor", this.constructor, this.constructorAttrs);
                    this.constructor = null;
                } else {
                    this.obj.initPrototypeId(id, cx);
                }
                value = array[valueSlot];
                if (value == null) {
                    throw new IllegalStateException(this.obj.getClass().getName() + ".initPrototypeId(int id) did not initialize id=" + id);
                }
            }
            return value;
        }
    }
}