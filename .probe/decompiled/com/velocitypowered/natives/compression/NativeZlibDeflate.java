package com.velocitypowered.natives.compression;

class NativeZlibDeflate {

    static native long init(int var0);

    static native long free(long var0);

    static native int process(long var0, long var2, int var4, long var5, int var7);
}