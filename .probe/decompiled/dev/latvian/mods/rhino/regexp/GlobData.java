package dev.latvian.mods.rhino.regexp;

import dev.latvian.mods.rhino.Function;
import dev.latvian.mods.rhino.Scriptable;

final class GlobData {

    int mode;

    boolean global;

    String str;

    Scriptable arrayobj;

    Function lambda;

    String repstr;

    int dollar = -1;

    StringBuilder charBuf;

    int leftIndex;
}