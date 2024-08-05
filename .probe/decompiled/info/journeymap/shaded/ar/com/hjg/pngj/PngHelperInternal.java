package info.journeymap.shaded.ar.com.hjg.pngj;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public final class PngHelperInternal {

    public static final String KEY_LOGGER = "info.journeymap.shaded.ar.com.pngj";

    public static final Logger LOGGER = Logger.getLogger("info.journeymap.shaded.ar.com.pngj");

    public static String charsetLatin1name = "ISO-8859-1";

    public static Charset charsetLatin1 = Charset.forName(charsetLatin1name);

    public static String charsetUTF8name = "UTF-8";

    public static Charset charsetUTF8 = Charset.forName(charsetUTF8name);

    private static ThreadLocal<Boolean> DEBUG = new ThreadLocal<Boolean>() {

        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    public static byte[] getPngIdSignature() {
        return new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 };
    }

    public static int doubleToInt100000(double d) {
        return (int) (d * 100000.0 + 0.5);
    }

    public static double intToDouble100000(int i) {
        return (double) i / 100000.0;
    }

    public static int readByte(InputStream is) {
        try {
            return is.read();
        } catch (IOException var2) {
            throw new PngjInputException("error reading byte", var2);
        }
    }

    public static int readInt2(InputStream is) {
        try {
            int b1 = is.read();
            int b2 = is.read();
            return b1 != -1 && b2 != -1 ? b1 << 8 | b2 : -1;
        } catch (IOException var3) {
            throw new PngjInputException("error reading Int2", var3);
        }
    }

    public static int readInt4(InputStream is) {
        try {
            int b1 = is.read();
            int b2 = is.read();
            int b3 = is.read();
            int b4 = is.read();
            return b1 != -1 && b2 != -1 && b3 != -1 && b4 != -1 ? b1 << 24 | b2 << 16 | (b3 << 8) + b4 : -1;
        } catch (IOException var5) {
            throw new PngjInputException("error reading Int4", var5);
        }
    }

    public static int readInt1fromByte(byte[] b, int offset) {
        return b[offset] & 0xFF;
    }

    public static int readInt2fromBytes(byte[] b, int offset) {
        return (b[offset] & 0xFF) << 8 | b[offset + 1] & 0xFF;
    }

    public static final int readInt4fromBytes(byte[] b, int offset) {
        return (b[offset] & 0xFF) << 24 | (b[offset + 1] & 0xFF) << 16 | (b[offset + 2] & 0xFF) << 8 | b[offset + 3] & 0xFF;
    }

    public static void writeByte(OutputStream os, byte b) {
        try {
            os.write(b);
        } catch (IOException var3) {
            throw new PngjOutputException(var3);
        }
    }

    public static void writeByte(OutputStream os, byte[] bs) {
        try {
            os.write(bs);
        } catch (IOException var3) {
            throw new PngjOutputException(var3);
        }
    }

    public static void writeInt2(OutputStream os, int n) {
        byte[] temp = new byte[] { (byte) (n >> 8 & 0xFF), (byte) (n & 0xFF) };
        writeBytes(os, temp);
    }

    public static void writeInt4(OutputStream os, int n) {
        byte[] temp = new byte[4];
        writeInt4tobytes(n, temp, 0);
        writeBytes(os, temp);
    }

    public static void writeInt2tobytes(int n, byte[] b, int offset) {
        b[offset] = (byte) (n >> 8 & 0xFF);
        b[offset + 1] = (byte) (n & 0xFF);
    }

    public static void writeInt4tobytes(int n, byte[] b, int offset) {
        b[offset] = (byte) (n >> 24 & 0xFF);
        b[offset + 1] = (byte) (n >> 16 & 0xFF);
        b[offset + 2] = (byte) (n >> 8 & 0xFF);
        b[offset + 3] = (byte) (n & 0xFF);
    }

    public static void readBytes(InputStream is, byte[] b, int offset, int len) {
        if (len != 0) {
            try {
                int read = 0;
                while (read < len) {
                    int n = is.read(b, offset + read, len - read);
                    if (n < 1) {
                        throw new PngjInputException("error reading bytes, " + n + " !=" + len);
                    }
                    read += n;
                }
            } catch (IOException var6) {
                throw new PngjInputException("error reading", var6);
            }
        }
    }

    public static void skipBytes(InputStream is, long len) {
        try {
            while (len > 0L) {
                long n1 = is.skip(len);
                if (n1 > 0L) {
                    len -= n1;
                } else {
                    if (n1 != 0L) {
                        throw new IOException("skip() returned a negative value ???");
                    }
                    if (is.read() == -1) {
                        break;
                    }
                    len--;
                }
            }
        } catch (IOException var5) {
            throw new PngjInputException(var5);
        }
    }

    public static void writeBytes(OutputStream os, byte[] b) {
        try {
            os.write(b);
        } catch (IOException var3) {
            throw new PngjOutputException(var3);
        }
    }

    public static void writeBytes(OutputStream os, byte[] b, int offset, int n) {
        try {
            os.write(b, offset, n);
        } catch (IOException var5) {
            throw new PngjOutputException(var5);
        }
    }

    public static void logdebug(String msg) {
        if (isDebug()) {
            System.err.println("logdebug: " + msg);
        }
    }

    public static int filterRowNone(int r) {
        return r & 0xFF;
    }

    public static int filterRowSub(int r, int left) {
        return r - left & 0xFF;
    }

    public static int filterRowUp(int r, int up) {
        return r - up & 0xFF;
    }

    public static int filterRowAverage(int r, int left, int up) {
        return r - (left + up) / 2 & 0xFF;
    }

    public static int filterRowPaeth(int r, int left, int up, int upleft) {
        return r - filterPaethPredictor(left, up, upleft) & 0xFF;
    }

    static final int filterPaethPredictor(int a, int b, int c) {
        int p = a + b - c;
        int pa = p >= a ? p - a : a - p;
        int pb = p >= b ? p - b : b - p;
        int pc = p >= c ? p - c : c - p;
        if (pa <= pb && pa <= pc) {
            return a;
        } else {
            return pb <= pc ? b : c;
        }
    }

    public static void debug(Object obj) {
        debug(obj, 1, true);
    }

    static void debug(Object obj, int offset) {
        debug(obj, offset, true);
    }

    public static InputStream istreamFromFile(File f) {
        try {
            return new FileInputStream(f);
        } catch (Exception var3) {
            throw new PngjInputException("Could not open " + f, var3);
        }
    }

    static OutputStream ostreamFromFile(File f) {
        return ostreamFromFile(f, true);
    }

    static OutputStream ostreamFromFile(File f, boolean overwrite) {
        return PngHelperInternal2.ostreamFromFile(f, overwrite);
    }

    static void debug(Object obj, int offset, boolean newLine) {
        StackTraceElement ste = new Exception().getStackTrace()[1 + offset];
        String steStr = ste.getClassName();
        int ind = steStr.lastIndexOf(46);
        steStr = steStr.substring(ind + 1);
        steStr = steStr + "." + ste.getMethodName() + "(" + ste.getLineNumber() + "): " + (obj == null ? null : obj.toString());
        System.err.println(steStr);
    }

    public static void setDebug(boolean b) {
        DEBUG.set(b);
    }

    public static boolean isDebug() {
        return (Boolean) DEBUG.get();
    }

    public static long getDigest(PngReader pngr) {
        return pngr.getSimpleDigest();
    }

    public static void initCrcForTests(PngReader pngr) {
        pngr.prepareSimpleDigestComputation();
    }

    public static long getRawIdatBytes(PngReader r) {
        return r.interlaced ? r.getChunkseq().getDeinterlacer().getTotalRawBytes() : r.imgInfo.getTotalRawBytes();
    }
}