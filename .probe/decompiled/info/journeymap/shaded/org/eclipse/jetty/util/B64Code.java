package info.journeymap.shaded.org.eclipse.jetty.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class B64Code {

    private static final char __pad = '=';

    private static final char[] __rfc1421alphabet = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

    private static final byte[] __rfc1421nibbles = new byte[256];

    private static final char[] __rfc4648urlAlphabet;

    private static final byte[] __rfc4648urlNibbles;

    private B64Code() {
    }

    public static String encode(String s) {
        return encode(s, (Charset) null);
    }

    public static String encode(String s, String charEncoding) {
        byte[] bytes;
        if (charEncoding == null) {
            bytes = s.getBytes(StandardCharsets.ISO_8859_1);
        } else {
            bytes = s.getBytes(Charset.forName(charEncoding));
        }
        return new String(encode(bytes));
    }

    public static String encode(String s, Charset charEncoding) {
        byte[] bytes = s.getBytes(charEncoding == null ? StandardCharsets.ISO_8859_1 : charEncoding);
        return new String(encode(bytes));
    }

    public static char[] encode(byte[] b) {
        if (b == null) {
            return null;
        } else {
            int bLen = b.length;
            int cLen = (bLen + 2) / 3 * 4;
            char[] c = new char[cLen];
            int ci = 0;
            int bi = 0;
            int stop = bLen / 3 * 3;
            while (bi < stop) {
                byte b0 = b[bi++];
                byte b1 = b[bi++];
                byte b2 = b[bi++];
                c[ci++] = __rfc1421alphabet[b0 >>> 2 & 63];
                c[ci++] = __rfc1421alphabet[b0 << 4 & 63 | b1 >>> 4 & 15];
                c[ci++] = __rfc1421alphabet[b1 << 2 & 63 | b2 >>> 6 & 3];
                c[ci++] = __rfc1421alphabet[b2 & 63];
            }
            if (bLen != bi) {
                switch(bLen % 3) {
                    case 1:
                        {
                            byte b0 = b[bi++];
                            c[ci++] = __rfc1421alphabet[b0 >>> 2 & 63];
                            c[ci++] = __rfc1421alphabet[b0 << 4 & 63];
                            c[ci++] = '=';
                            c[ci++] = '=';
                            break;
                        }
                    case 2:
                        {
                            byte b0 = b[bi++];
                            byte b1 = b[bi++];
                            c[ci++] = __rfc1421alphabet[b0 >>> 2 & 63];
                            c[ci++] = __rfc1421alphabet[b0 << 4 & 63 | b1 >>> 4 & 15];
                            c[ci++] = __rfc1421alphabet[b1 << 2 & 63];
                            c[ci++] = '=';
                        }
                }
            }
            return c;
        }
    }

    public static char[] encode(byte[] b, boolean rfc2045) {
        if (b == null) {
            return null;
        } else if (!rfc2045) {
            return encode(b);
        } else {
            int bLen = b.length;
            int cLen = (bLen + 2) / 3 * 4;
            cLen += 2 + 2 * (cLen / 76);
            char[] c = new char[cLen];
            int ci = 0;
            int bi = 0;
            int stop = bLen / 3 * 3;
            int l = 0;
            while (bi < stop) {
                byte b0 = b[bi++];
                byte b1 = b[bi++];
                byte b2 = b[bi++];
                c[ci++] = __rfc1421alphabet[b0 >>> 2 & 63];
                c[ci++] = __rfc1421alphabet[b0 << 4 & 63 | b1 >>> 4 & 15];
                c[ci++] = __rfc1421alphabet[b1 << 2 & 63 | b2 >>> 6 & 3];
                c[ci++] = __rfc1421alphabet[b2 & 63];
                l += 4;
                if (l % 76 == 0) {
                    c[ci++] = '\r';
                    c[ci++] = '\n';
                }
            }
            if (bLen != bi) {
                switch(bLen % 3) {
                    case 1:
                        {
                            byte b0 = b[bi++];
                            c[ci++] = __rfc1421alphabet[b0 >>> 2 & 63];
                            c[ci++] = __rfc1421alphabet[b0 << 4 & 63];
                            c[ci++] = '=';
                            c[ci++] = '=';
                            break;
                        }
                    case 2:
                        {
                            byte b0 = b[bi++];
                            byte b1 = b[bi++];
                            c[ci++] = __rfc1421alphabet[b0 >>> 2 & 63];
                            c[ci++] = __rfc1421alphabet[b0 << 4 & 63 | b1 >>> 4 & 15];
                            c[ci++] = __rfc1421alphabet[b1 << 2 & 63];
                            c[ci++] = '=';
                        }
                }
            }
            c[ci++] = '\r';
            c[ci++] = '\n';
            return c;
        }
    }

    public static String decode(String encoded, String charEncoding) {
        byte[] decoded = decode(encoded);
        return charEncoding == null ? new String(decoded) : new String(decoded, Charset.forName(charEncoding));
    }

    public static String decode(String encoded, Charset charEncoding) {
        byte[] decoded = decode(encoded);
        return charEncoding == null ? new String(decoded) : new String(decoded, charEncoding);
    }

    public static byte[] decode(char[] b) {
        if (b == null) {
            return null;
        } else {
            int bLen = b.length;
            if (bLen % 4 != 0) {
                throw new IllegalArgumentException("Input block size is not 4");
            } else {
                int li = bLen - 1;
                while (li >= 0 && b[li] == '=') {
                    li--;
                }
                if (li < 0) {
                    return new byte[0];
                } else {
                    int rLen = (li + 1) * 3 / 4;
                    byte[] r = new byte[rLen];
                    int ri = 0;
                    int bi = 0;
                    int stop = rLen / 3 * 3;
                    try {
                        while (ri < stop) {
                            byte b0 = __rfc1421nibbles[b[bi++]];
                            byte b1 = __rfc1421nibbles[b[bi++]];
                            byte b2 = __rfc1421nibbles[b[bi++]];
                            byte b3 = __rfc1421nibbles[b[bi++]];
                            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
                                throw new IllegalArgumentException("Not B64 encoded");
                            }
                            r[ri++] = (byte) (b0 << 2 | b1 >>> 4);
                            r[ri++] = (byte) (b1 << 4 | b2 >>> 2);
                            r[ri++] = (byte) (b2 << 6 | b3);
                        }
                        if (rLen != ri) {
                            switch(rLen % 3) {
                                case 1:
                                    byte b0 = __rfc1421nibbles[b[bi++]];
                                    byte b1 = __rfc1421nibbles[b[bi++]];
                                    if (b0 < 0 || b1 < 0) {
                                        throw new IllegalArgumentException("Not B64 encoded");
                                    }
                                    r[ri++] = (byte) (b0 << 2 | b1 >>> 4);
                                    break;
                                case 2:
                                    byte b0 = __rfc1421nibbles[b[bi++]];
                                    byte b1 = __rfc1421nibbles[b[bi++]];
                                    byte b2 = __rfc1421nibbles[b[bi++]];
                                    if (b0 < 0 || b1 < 0 || b2 < 0) {
                                        throw new IllegalArgumentException("Not B64 encoded");
                                    }
                                    r[ri++] = (byte) (b0 << 2 | b1 >>> 4);
                                    r[ri++] = (byte) (b1 << 4 | b2 >>> 2);
                            }
                        }
                        return r;
                    } catch (IndexOutOfBoundsException var13) {
                        throw new IllegalArgumentException("char " + bi + " was not B64 encoded");
                    }
                }
            }
        }
    }

    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        } else {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(4 * encoded.length() / 3);
            decode(encoded, bout);
            return bout.toByteArray();
        }
    }

    public static void decode(String encoded, ByteArrayOutputStream bout) {
        if (encoded != null) {
            if (bout == null) {
                throw new IllegalArgumentException("No outputstream for decoded bytes");
            } else {
                int ci = 0;
                byte[] nibbles = new byte[4];
                int s = 0;
                while (ci < encoded.length()) {
                    char c = encoded.charAt(ci++);
                    if (c == '=') {
                        break;
                    }
                    if (!Character.isWhitespace(c)) {
                        byte nibble = __rfc1421nibbles[c];
                        if (nibble < 0) {
                            throw new IllegalArgumentException("Not B64 encoded");
                        }
                        nibbles[s++] = __rfc1421nibbles[c];
                        switch(s) {
                            case 1:
                            default:
                                break;
                            case 2:
                                bout.write(nibbles[0] << 2 | nibbles[1] >>> 4);
                                break;
                            case 3:
                                bout.write(nibbles[1] << 4 | nibbles[2] >>> 2);
                                break;
                            case 4:
                                bout.write(nibbles[2] << 6 | nibbles[3]);
                                s = 0;
                        }
                    }
                }
            }
        }
    }

    public static byte[] decodeRFC4648URL(String encoded) {
        if (encoded == null) {
            return null;
        } else {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(4 * encoded.length() / 3);
            decodeRFC4648URL(encoded, bout);
            return bout.toByteArray();
        }
    }

    public static void decodeRFC4648URL(String encoded, ByteArrayOutputStream bout) {
        if (encoded != null) {
            if (bout == null) {
                throw new IllegalArgumentException("No outputstream for decoded bytes");
            } else {
                int ci = 0;
                byte[] nibbles = new byte[4];
                int s = 0;
                while (ci < encoded.length()) {
                    char c = encoded.charAt(ci++);
                    if (c == '=') {
                        break;
                    }
                    if (!Character.isWhitespace(c)) {
                        byte nibble = __rfc4648urlNibbles[c];
                        if (nibble < 0) {
                            throw new IllegalArgumentException("Not B64 encoded");
                        }
                        nibbles[s++] = __rfc4648urlNibbles[c];
                        switch(s) {
                            case 1:
                            default:
                                break;
                            case 2:
                                bout.write(nibbles[0] << 2 | nibbles[1] >>> 4);
                                break;
                            case 3:
                                bout.write(nibbles[1] << 4 | nibbles[2] >>> 2);
                                break;
                            case 4:
                                bout.write(nibbles[2] << 6 | nibbles[3]);
                                s = 0;
                        }
                    }
                }
            }
        }
    }

    public static void encode(int value, Appendable buf) throws IOException {
        buf.append(__rfc1421alphabet[63 & (-67108864 & value) >> 26]);
        buf.append(__rfc1421alphabet[63 & (66060288 & value) >> 20]);
        buf.append(__rfc1421alphabet[63 & (1032192 & value) >> 14]);
        buf.append(__rfc1421alphabet[63 & (16128 & value) >> 8]);
        buf.append(__rfc1421alphabet[63 & (252 & value) >> 2]);
        buf.append(__rfc1421alphabet[63 & (3 & value) << 4]);
        buf.append('=');
    }

    public static void encode(long lvalue, Appendable buf) throws IOException {
        int value = (int) (-4L & lvalue >> 32);
        buf.append(__rfc1421alphabet[63 & (-67108864 & value) >> 26]);
        buf.append(__rfc1421alphabet[63 & (66060288 & value) >> 20]);
        buf.append(__rfc1421alphabet[63 & (1032192 & value) >> 14]);
        buf.append(__rfc1421alphabet[63 & (16128 & value) >> 8]);
        buf.append(__rfc1421alphabet[63 & (252 & value) >> 2]);
        buf.append(__rfc1421alphabet[63 & ((3 & value) << 4) + (15 & (int) (lvalue >> 28))]);
        value = 268435455 & (int) lvalue;
        buf.append(__rfc1421alphabet[63 & (264241152 & value) >> 22]);
        buf.append(__rfc1421alphabet[63 & (4128768 & value) >> 16]);
        buf.append(__rfc1421alphabet[63 & (64512 & value) >> 10]);
        buf.append(__rfc1421alphabet[63 & (1008 & value) >> 4]);
        buf.append(__rfc1421alphabet[63 & (15 & value) << 2]);
    }

    static {
        for (int i = 0; i < 256; i++) {
            __rfc1421nibbles[i] = -1;
        }
        byte b = 0;
        while (b < 64) {
            __rfc1421nibbles[(byte) __rfc1421alphabet[b]] = b++;
        }
        __rfc1421nibbles[61] = 0;
        __rfc4648urlAlphabet = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_' };
        __rfc4648urlNibbles = new byte[256];
        for (int i = 0; i < 256; i++) {
            __rfc4648urlNibbles[i] = -1;
        }
        b = 0;
        while (b < 64) {
            __rfc4648urlNibbles[(byte) __rfc4648urlAlphabet[b]] = b++;
        }
        __rfc4648urlNibbles[61] = 0;
    }
}