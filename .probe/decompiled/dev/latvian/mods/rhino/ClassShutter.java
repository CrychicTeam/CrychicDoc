package dev.latvian.mods.rhino;

public interface ClassShutter {

    int TYPE_UNKNOWN = 0;

    int TYPE_MEMBER = 1;

    int TYPE_CLASS_IN_PACKAGE = 2;

    int TYPE_EXCEPTION = 3;

    boolean visibleToScripts(String var1, int var2);
}