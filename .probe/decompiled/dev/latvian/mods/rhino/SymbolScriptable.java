package dev.latvian.mods.rhino;

public interface SymbolScriptable {

    Object get(Context var1, Symbol var2, Scriptable var3);

    boolean has(Context var1, Symbol var2, Scriptable var3);

    void put(Context var1, Symbol var2, Scriptable var3, Object var4);

    void delete(Context var1, Symbol var2);
}