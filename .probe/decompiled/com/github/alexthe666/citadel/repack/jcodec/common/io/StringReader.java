package com.github.alexthe666.citadel.repack.jcodec.common.io;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.IOException;
import java.io.InputStream;

public abstract class StringReader {

    public static String readString(InputStream input, int len) throws IOException {
        byte[] bs = _sureRead(input, len);
        return bs == null ? null : Platform.stringFromBytes(bs);
    }

    public static byte[] _sureRead(InputStream input, int len) throws IOException {
        byte[] res = new byte[len];
        return sureRead(input, res, res.length) == len ? res : null;
    }

    public static int sureRead(InputStream input, byte[] buf, int len) throws IOException {
        int read = 0;
        while (read < len) {
            int tmp = input.read(buf, read, len - read);
            if (tmp == -1) {
                break;
            }
            read += tmp;
        }
        return read;
    }

    public static void sureSkip(InputStream is, long l) throws IOException {
        while (l > 0L) {
            l -= is.skip(l);
        }
    }
}