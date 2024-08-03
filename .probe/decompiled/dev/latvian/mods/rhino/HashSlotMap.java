package dev.latvian.mods.rhino;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class HashSlotMap implements SlotMap {

    private final LinkedHashMap<Object, ScriptableObject.Slot> map = new LinkedHashMap();

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public ScriptableObject.Slot query(Object key, int index) {
        Object name = key == null ? String.valueOf(index) : key;
        return (ScriptableObject.Slot) this.map.get(name);
    }

    @Override
    public ScriptableObject.Slot get(Object key, int index, ScriptableObject.SlotAccess accessType) {
        Object name = key == null ? String.valueOf(index) : key;
        ScriptableObject.Slot slot = (ScriptableObject.Slot) this.map.get(name);
        switch(accessType) {
            case QUERY:
                return slot;
            case MODIFY:
            case MODIFY_CONST:
                if (slot != null) {
                    return slot;
                }
                break;
            case MODIFY_GETTER_SETTER:
                if (slot instanceof ScriptableObject.GetterSlot) {
                    return slot;
                }
                break;
            case CONVERT_ACCESSOR_TO_DATA:
                if (!(slot instanceof ScriptableObject.GetterSlot)) {
                    return slot;
                }
        }
        return this.createSlot(key, index, name, accessType);
    }

    private ScriptableObject.Slot createSlot(Object key, int index, Object name, ScriptableObject.SlotAccess accessType) {
        ScriptableObject.Slot slot = (ScriptableObject.Slot) this.map.get(name);
        if (slot == null) {
            ScriptableObject.Slot newSlot = (ScriptableObject.Slot) (accessType == ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER ? new ScriptableObject.GetterSlot(key, index, 0) : new ScriptableObject.Slot(key, index, 0));
            if (accessType == ScriptableObject.SlotAccess.MODIFY_CONST) {
                newSlot.setAttributes(13);
            }
            this.addSlot(newSlot);
            return newSlot;
        } else {
            ScriptableObject.Slot newSlot;
            if (accessType == ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER && !(slot instanceof ScriptableObject.GetterSlot)) {
                newSlot = new ScriptableObject.GetterSlot(name, slot.indexOrHash, slot.getAttributes());
            } else {
                if (accessType != ScriptableObject.SlotAccess.CONVERT_ACCESSOR_TO_DATA || !(slot instanceof ScriptableObject.GetterSlot)) {
                    if (accessType == ScriptableObject.SlotAccess.MODIFY_CONST) {
                        return null;
                    }
                    return slot;
                }
                newSlot = new ScriptableObject.Slot(name, slot.indexOrHash, slot.getAttributes());
            }
            newSlot.value = slot.value;
            this.map.put(name, newSlot);
            return newSlot;
        }
    }

    @Override
    public void addSlot(ScriptableObject.Slot newSlot) {
        Object name = newSlot.name == null ? String.valueOf(newSlot.indexOrHash) : newSlot.name;
        this.map.put(name, newSlot);
    }

    @Override
    public void remove(Object key, int index, Context cx) {
        Object name = key == null ? String.valueOf(index) : key;
        ScriptableObject.Slot slot = (ScriptableObject.Slot) this.map.get(name);
        if (slot != null) {
            if ((slot.getAttributes() & 4) != 0) {
                if (cx.isStrictMode()) {
                    throw ScriptRuntime.typeError1(cx, "msg.delete.property.with.configurable.false", key);
                }
                return;
            }
            this.map.remove(name);
        }
    }

    public Iterator<ScriptableObject.Slot> iterator() {
        return this.map.values().iterator();
    }
}