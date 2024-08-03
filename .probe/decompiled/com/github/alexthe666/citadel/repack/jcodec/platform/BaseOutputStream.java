package com.github.alexthe666.citadel.repack.jcodec.platform;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseOutputStream extends OutputStream {

    protected abstract void writeByte(int var1) throws IOException;

    public void write(int b) throws IOException {
        this.writeByte(b);
    }
}