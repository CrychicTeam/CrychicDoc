package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.fd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

class Base64Decoder {

    private static final byte[] CHAR_CONVERT_ARRAY = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 };

    public static byte[] decode(byte[] b) {
        ByteArrayInputStream in = new ByteArrayInputStream(b);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PushbackInputStream ps = new PushbackInputStream(in);
        try {
            while (true) {
                int i;
                for (i = 0; i + 4 < 72; i += 4) {
                    decodeAtom(ps, out, 4);
                }
                if (i + 4 == 72) {
                    decodeAtom(ps, out, 4);
                } else {
                    decodeAtom(ps, out, 72 - i);
                }
            }
        } catch (IOException var6) {
            return out.toByteArray();
        }
    }

    private static void decodeAtom(InputStream in, OutputStream out, int rem) throws IOException {
        if (rem < 2) {
            throw new IOException();
        } else {
            int i;
            do {
                i = in.read();
                if (i == -1) {
                    throw new IOException();
                }
            } while (i == 10 || i == 13);
            byte[] buf = new byte[4];
            buf[0] = (byte) i;
            i = readFully(in, buf, 1, rem - 1);
            if (i == -1) {
                throw new IOException();
            } else {
                if (rem > 3 && buf[3] == 61) {
                    rem = 3;
                }
                if (rem > 2 && buf[2] == 61) {
                    rem = 2;
                }
                byte a = -1;
                byte b = -1;
                byte c = -1;
                byte d = -1;
                switch(rem) {
                    case 4:
                        d = CHAR_CONVERT_ARRAY[buf[3] & 255];
                    case 3:
                        c = CHAR_CONVERT_ARRAY[buf[2] & 255];
                    case 2:
                        b = CHAR_CONVERT_ARRAY[buf[1] & 255];
                        a = CHAR_CONVERT_ARRAY[buf[0] & 255];
                    default:
                        switch(rem) {
                            case 2:
                                out.write((byte) (a << 2 & 252 | b >>> 4 & 3));
                                break;
                            case 3:
                                out.write((byte) (a << 2 & 252 | b >>> 4 & 3));
                                out.write((byte) (b << 4 & 240 | c >>> 2 & 15));
                                break;
                            case 4:
                                out.write((byte) (a << 2 & 252 | b >>> 4 & 3));
                                out.write((byte) (b << 4 & 240 | c >>> 2 & 15));
                                out.write((byte) (c << 6 & 192 | d & 63));
                        }
                }
            }
        }
    }

    private static int readFully(InputStream in, byte[] b, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            int q = in.read();
            if (q == -1) {
                return i == 0 ? -1 : i;
            }
            b[i + off] = (byte) q;
        }
        return len;
    }
}