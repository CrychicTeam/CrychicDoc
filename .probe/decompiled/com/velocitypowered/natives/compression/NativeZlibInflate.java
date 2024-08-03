package com.velocitypowered.natives.compression;

import java.util.zip.DataFormatException;

class NativeZlibInflate {

    static native long init();

    static native long free(long var0);

    static native boolean process(long var0, long var2, int var4, long var5, int var7) throws DataFormatException;
}