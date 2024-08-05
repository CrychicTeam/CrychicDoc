package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.util.Deletable;
import dev.latvian.mods.rhino.util.ValueUnwrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NativeJavaMap extends NativeJavaObject {

    private final Map map;

    private final Class<?> mapValueType;

    private final ValueUnwrapper valueUnwrapper;

    public NativeJavaMap(Context cx, Scriptable scope, Object jo, Map map, Class<?> mapValueType, ValueUnwrapper valueUnwrapper) {
        super(scope, jo, jo.getClass(), cx);
        this.map = map;
        this.mapValueType = mapValueType;
        this.valueUnwrapper = valueUnwrapper;
    }

    public NativeJavaMap(Context cx, Scriptable scope, Object jo, Map map) {
        this(cx, scope, jo, map, Object.class, ValueUnwrapper.DEFAULT);
    }

    @Override
    public String getClassName() {
        return "JavaMap";
    }

    @Override
    public boolean has(Context cx, String name, Scriptable start) {
        return this.map.containsKey(name) ? true : super.has(cx, name, start);
    }

    @Override
    public boolean has(Context cx, int index, Scriptable start) {
        return this.map.containsKey(index) ? true : super.has(cx, index, start);
    }

    @Override
    public Object get(Context cx, String name, Scriptable start) {
        return this.map.containsKey(name) ? this.valueUnwrapper.unwrap(cx, this, this.map.get(name)) : super.get(cx, name, start);
    }

    @Override
    public Object get(Context cx, int index, Scriptable start) {
        return this.map.containsKey(index) ? this.valueUnwrapper.unwrap(cx, this, this.map.get(index)) : super.get(cx, index, start);
    }

    @Override
    public void put(Context cx, String name, Scriptable start, Object value) {
        this.map.put(name, Context.jsToJava(cx, value, this.mapValueType));
    }

    @Override
    public void put(Context cx, int index, Scriptable start, Object value) {
        this.map.put(index, Context.jsToJava(cx, value, this.mapValueType));
    }

    @Override
    public Object[] getIds(Context cx) {
        List<Object> ids = new ArrayList(this.map.size());
        for (Object key : this.map.keySet()) {
            if (key instanceof Integer) {
                ids.add(key);
            } else {
                ids.add(ScriptRuntime.toString(cx, key));
            }
        }
        return ids.toArray();
    }

    @Override
    public void delete(Context cx, String name) {
        Deletable.deleteObject(this.map.remove(name));
    }

    @Override
    public void delete(Context cx, int index) {
        Deletable.deleteObject(this.map.remove(index));
    }

    @Override
    protected void initMembers(Context cx, Scriptable scope) {
        super.initMembers(cx, scope);
        this.addCustomFunction("hasOwnProperty", this::hasOwnProperty, new Class[] { String.class });
    }

    private boolean hasOwnProperty(Context cx, Object[] args) {
        return this.map.containsKey(ScriptRuntime.toString(cx, args[0]));
    }
}