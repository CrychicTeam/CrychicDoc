package com.github.alexthe666.citadel.repack.jcodec.codecs.wav;

import java.io.IOException;
import java.io.OutputStream;

public abstract class WriterLE {

    public static void writeShort(OutputStream out, short s) throws IOException {
        out.write(s & 255);
        out.write(s >> 8 & 0xFF);
    }

    public static void writeInt(OutputStream out, int i) throws IOException {
        out.write(i & 0xFF);
        out.write(i >> 8 & 0xFF);
        out.write(i >> 16 & 0xFF);
        out.write(i >> 24 & 0xFF);
    }

    public static void writeLong(OutputStream out, long l) throws IOException {
        out.write((int) (l & 255L));
        out.write((int) (l >> 8 & 255L));
        out.write((int) (l >> 16 & 255L));
        out.write((int) (l >> 24 & 255L));
        out.write((int) (l >> 32 & 255L));
        out.write((int) (l >> 40 & 255L));
        out.write((int) (l >> 48 & 255L));
        out.write((int) (l >> 56 & 255L));
    }
}