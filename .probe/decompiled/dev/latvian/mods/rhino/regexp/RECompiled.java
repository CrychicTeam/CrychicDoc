package dev.latvian.mods.rhino.regexp;

class RECompiled {

    final char[] source;

    int parenCount;

    int flags;

    byte[] program;

    int classCount;

    RECharSet[] classList;

    int anchorCh = -1;

    RECompiled(String str) {
        this.source = str.toCharArray();
    }
}