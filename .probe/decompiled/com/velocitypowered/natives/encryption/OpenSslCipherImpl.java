package com.velocitypowered.natives.encryption;

import java.security.GeneralSecurityException;

class OpenSslCipherImpl {

    static native long init(byte[] var0, boolean var1) throws GeneralSecurityException;

    static native void process(long var0, long var2, int var4, long var5);

    static native void free(long var0);
}