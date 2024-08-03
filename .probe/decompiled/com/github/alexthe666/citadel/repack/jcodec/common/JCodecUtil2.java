package com.github.alexthe666.citadel.repack.jcodec.common;

import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public class JCodecUtil2 {

    public static void writeBER32(ByteBuffer buffer, int value) {
        buffer.put((byte) (value >> 21 | 128));
        buffer.put((byte) (value >> 14 | 128));
        buffer.put((byte) (value >> 7 | 128));
        buffer.put((byte) (value & 127));
    }

    public static int readBER32(ByteBuffer input) {
        int size = 0;
        for (int i = 0; i < 4; i++) {
            byte b = input.get();
            size = size << 7 | b & 127;
            if ((b & 255) >> 7 == 0) {
                break;
            }
        }
        return size;
    }

    public static void writeBER32Var(ByteBuffer bb, int value) {
        int i = 0;
        for (int bits = MathUtil.log2(value); i < 4 && bits > 0; i++) {
            bits -= 7;
            int out = value >> bits;
            if (bits > 0) {
                out |= 128;
            }
            bb.put((byte) out);
        }
    }

    public static byte[] asciiString(String fourcc) {
        return Platform.getBytes(fourcc);
    }

    public static int[] getAsIntArray(ByteBuffer yuv, int size) {
        byte[] b = new byte[size];
        int[] result = new int[size];
        yuv.get(b);
        for (int i = 0; i < b.length; i++) {
            result[i] = b[i] & 255;
        }
        return result;
    }

    public static String removeExtension(String name) {
        return name == null ? null : name.replaceAll("\\.[^\\.]+$", "");
    }
}