package dev.latvian.mods.rhino;

public interface ConstProperties {

    void putConst(Context var1, String var2, Scriptable var3, Object var4);

    void defineConst(Context var1, String var2, Scriptable var3);

    boolean isConst(String var1);
}