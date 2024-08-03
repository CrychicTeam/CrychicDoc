package net.raphimc.immediatelyfastapi;

public interface ConfigAccess {

    boolean getBoolean(String var1, boolean var2);

    int getInt(String var1, int var2);

    long getLong(String var1, long var2);

    String getString(String var1, String var2);
}