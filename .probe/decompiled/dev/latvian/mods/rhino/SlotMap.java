package dev.latvian.mods.rhino;

public interface SlotMap extends Iterable<ScriptableObject.Slot> {

    int size();

    boolean isEmpty();

    ScriptableObject.Slot get(Object var1, int var2, ScriptableObject.SlotAccess var3);

    ScriptableObject.Slot query(Object var1, int var2);

    void addSlot(ScriptableObject.Slot var1);

    void remove(Object var1, int var2, Context var3);
}