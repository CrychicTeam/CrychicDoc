package com.github.alexthe666.citadel.repack.jcodec.platform;

import java.io.IOException;
import java.io.InputStream;

public abstract class BaseInputStream extends InputStream {

    protected abstract int readByte() throws IOException;

    protected abstract int readBuffer(byte[] var1, int var2, int var3) throws IOException;

    public int read(byte[] b) throws IOException {
        return this.readBuffer(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return this.readBuffer(b, off, len);
    }

    public int read() throws IOException {
        return this.readByte();
    }
}