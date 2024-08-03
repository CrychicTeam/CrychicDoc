package info.journeymap.shaded.org.eclipse.jetty.util;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map.Entry;

public class UrlEncoded extends MultiMap<String> implements Cloneable {

    static final Logger LOG = Log.getLogger(UrlEncoded.class);

    public static final Charset ENCODING;

    public UrlEncoded(UrlEncoded url) {
        super(url);
    }

    public UrlEncoded() {
    }

    public UrlEncoded(String query) {
        decodeTo(query, this, ENCODING);
    }

    public void decode(String query) {
        decodeTo(query, this, ENCODING);
    }

    public void decode(String query, Charset charset) {
        decodeTo(query, this, charset);
    }

    public String encode() {
        return this.encode(ENCODING, false);
    }

    public String encode(Charset charset) {
        return this.encode(charset, false);
    }

    public synchronized String encode(Charset charset, boolean equalsForNullValue) {
        return encode(this, charset, equalsForNullValue);
    }

    public static String encode(MultiMap<String> map, Charset charset, boolean equalsForNullValue) {
        if (charset == null) {
            charset = ENCODING;
        }
        StringBuilder result = new StringBuilder(128);
        boolean delim = false;
        for (Entry<String, List<String>> entry : map.entrySet()) {
            String key = ((String) entry.getKey()).toString();
            List<String> list = (List<String>) entry.getValue();
            int s = list.size();
            if (delim) {
                result.append('&');
            }
            if (s == 0) {
                result.append(encodeString(key, charset));
                if (equalsForNullValue) {
                    result.append('=');
                }
            } else {
                for (int i = 0; i < s; i++) {
                    if (i > 0) {
                        result.append('&');
                    }
                    String val = (String) list.get(i);
                    result.append(encodeString(key, charset));
                    if (val != null) {
                        String str = val.toString();
                        if (str.length() > 0) {
                            result.append('=');
                            result.append(encodeString(str, charset));
                        } else if (equalsForNullValue) {
                            result.append('=');
                        }
                    } else if (equalsForNullValue) {
                        result.append('=');
                    }
                }
            }
            delim = true;
        }
        return result.toString();
    }

    public static void decodeTo(String content, MultiMap<String> map, String charset) {
        decodeTo(content, map, charset == null ? null : Charset.forName(charset));
    }

    public static void decodeTo(String content, MultiMap<String> map, Charset charset) {
        if (charset == null) {
            charset = ENCODING;
        }
        if (charset == StandardCharsets.UTF_8) {
            decodeUtf8To(content, 0, content.length(), map);
        } else {
            synchronized (map) {
                String key = null;
                String value = null;
                int mark = -1;
                boolean encoded = false;
                for (int i = 0; i < content.length(); i++) {
                    char c = content.charAt(i);
                    switch(c) {
                        case '%':
                            encoded = true;
                            break;
                        case '&':
                            int l = i - mark - 1;
                            value = l == 0 ? "" : (encoded ? decodeString(content, mark + 1, l, charset) : content.substring(mark + 1, i));
                            mark = i;
                            encoded = false;
                            if (key != null) {
                                map.add(key, value);
                            } else if (value != null && value.length() > 0) {
                                map.add(value, "");
                            }
                            key = null;
                            value = null;
                            break;
                        case '+':
                            encoded = true;
                            break;
                        case '=':
                            if (key == null) {
                                key = encoded ? decodeString(content, mark + 1, i - mark - 1, charset) : content.substring(mark + 1, i);
                                mark = i;
                                encoded = false;
                            }
                    }
                }
                if (key != null) {
                    int l = content.length() - mark - 1;
                    value = l == 0 ? "" : (encoded ? decodeString(content, mark + 1, l, charset) : content.substring(mark + 1));
                    map.add(key, value);
                } else if (mark < content.length()) {
                    key = encoded ? decodeString(content, mark + 1, content.length() - mark - 1, charset) : content.substring(mark + 1);
                    if (key != null && key.length() > 0) {
                        map.add(key, "");
                    }
                }
            }
        }
    }

    public static void decodeUtf8To(String query, MultiMap<String> map) {
        decodeUtf8To(query, 0, query.length(), map);
    }

    public static void decodeUtf8To(String query, int offset, int length, MultiMap<String> map) {
        Utf8StringBuilder buffer = new Utf8StringBuilder();
        synchronized (map) {
            String key = null;
            String value = null;
            int end = offset + length;
            for (int i = offset; i < end; i++) {
                char c = query.charAt(i);
                switch(c) {
                    case '%':
                        if (i + 2 >= end) {
                            throw new Utf8Appendable.NotUtf8Exception("Incomplete % encoding");
                        }
                        char hi = query.charAt(++i);
                        char lo = query.charAt(++i);
                        buffer.append(decodeHexByte(hi, lo));
                        break;
                    case '&':
                        value = buffer.toReplacedString();
                        buffer.reset();
                        if (key != null) {
                            map.add(key, value);
                        } else if (value != null && value.length() > 0) {
                            map.add(value, "");
                        }
                        key = null;
                        value = null;
                        break;
                    case '+':
                        buffer.append((byte) 32);
                        break;
                    case '=':
                        if (key != null) {
                            buffer.append(c);
                        } else {
                            key = buffer.toReplacedString();
                            buffer.reset();
                        }
                        break;
                    default:
                        buffer.append(c);
                }
            }
            if (key != null) {
                value = buffer.toReplacedString();
                buffer.reset();
                map.add(key, value);
            } else if (buffer.length() > 0) {
                map.add(buffer.toReplacedString(), "");
            }
        }
    }

    public static void decode88591To(InputStream in, MultiMap<String> map, int maxLength, int maxKeys) throws IOException {
        synchronized (map) {
            StringBuffer buffer = new StringBuffer();
            String key = null;
            String value = null;
            int totalLength = 0;
            int b;
            while ((b = in.read()) >= 0) {
                switch((char) b) {
                    case '%':
                        int code0 = in.read();
                        int code1 = in.read();
                        buffer.append(decodeHexChar(code0, code1));
                        break;
                    case '&':
                        value = buffer.length() == 0 ? "" : buffer.toString();
                        buffer.setLength(0);
                        if (key != null) {
                            map.add(key, value);
                        } else if (value != null && value.length() > 0) {
                            map.add(value, "");
                        }
                        key = null;
                        value = null;
                        if (maxKeys > 0 && map.size() > maxKeys) {
                            throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", map.size(), maxKeys));
                        }
                        break;
                    case '+':
                        buffer.append(' ');
                        break;
                    case '=':
                        if (key != null) {
                            buffer.append((char) b);
                        } else {
                            key = buffer.toString();
                            buffer.setLength(0);
                        }
                        break;
                    default:
                        buffer.append((char) b);
                }
                if (maxLength >= 0) {
                    if (++totalLength > maxLength) {
                        throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", map.size(), maxKeys));
                    }
                }
            }
            if (key != null) {
                value = buffer.length() == 0 ? "" : buffer.toString();
                buffer.setLength(0);
                map.add(key, value);
            } else if (buffer.length() > 0) {
                map.add(buffer.toString(), "");
            }
        }
    }

    public static void decodeUtf8To(InputStream in, MultiMap<String> map, int maxLength, int maxKeys) throws IOException {
        synchronized (map) {
            Utf8StringBuilder buffer = new Utf8StringBuilder();
            String key = null;
            String value = null;
            int totalLength = 0;
            int b;
            while ((b = in.read()) >= 0) {
                switch((char) b) {
                    case '%':
                        char code0 = (char) in.read();
                        char code1 = (char) in.read();
                        buffer.append(decodeHexByte(code0, code1));
                        break;
                    case '&':
                        value = buffer.toReplacedString();
                        buffer.reset();
                        if (key != null) {
                            map.add(key, value);
                        } else if (value != null && value.length() > 0) {
                            map.add(value, "");
                        }
                        key = null;
                        value = null;
                        if (maxKeys > 0 && map.size() > maxKeys) {
                            throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", map.size(), maxKeys));
                        }
                        break;
                    case '+':
                        buffer.append((byte) 32);
                        break;
                    case '=':
                        if (key != null) {
                            buffer.append((byte) b);
                        } else {
                            key = buffer.toReplacedString();
                            buffer.reset();
                        }
                        break;
                    default:
                        buffer.append((byte) b);
                }
                if (maxLength >= 0) {
                    if (++totalLength > maxLength) {
                        throw new IllegalStateException("Form is too large");
                    }
                }
            }
            if (key != null) {
                value = buffer.toReplacedString();
                buffer.reset();
                map.add(key, value);
            } else if (buffer.length() > 0) {
                map.add(buffer.toReplacedString(), "");
            }
        }
    }

    public static void decodeUtf16To(InputStream in, MultiMap<String> map, int maxLength, int maxKeys) throws IOException {
        InputStreamReader input = new InputStreamReader(in, StandardCharsets.UTF_16);
        StringWriter buf = new StringWriter(8192);
        IO.copy(input, buf, (long) maxLength);
        decodeTo(buf.getBuffer().toString(), map, StandardCharsets.UTF_16);
    }

    public static void decodeTo(InputStream in, MultiMap<String> map, String charset, int maxLength, int maxKeys) throws IOException {
        if (charset == null) {
            if (ENCODING.equals(StandardCharsets.UTF_8)) {
                decodeUtf8To(in, map, maxLength, maxKeys);
            } else {
                decodeTo(in, map, ENCODING, maxLength, maxKeys);
            }
        } else if ("utf-8".equalsIgnoreCase(charset)) {
            decodeUtf8To(in, map, maxLength, maxKeys);
        } else if ("iso-8859-1".equalsIgnoreCase(charset)) {
            decode88591To(in, map, maxLength, maxKeys);
        } else if ("utf-16".equalsIgnoreCase(charset)) {
            decodeUtf16To(in, map, maxLength, maxKeys);
        } else {
            decodeTo(in, map, Charset.forName(charset), maxLength, maxKeys);
        }
    }

    public static void decodeTo(InputStream in, MultiMap<String> map, Charset charset, int maxLength, int maxKeys) throws IOException {
        if (charset == null) {
            charset = ENCODING;
        }
        if (StandardCharsets.UTF_8.equals(charset)) {
            decodeUtf8To(in, map, maxLength, maxKeys);
        } else if (StandardCharsets.ISO_8859_1.equals(charset)) {
            decode88591To(in, map, maxLength, maxKeys);
        } else if (StandardCharsets.UTF_16.equals(charset)) {
            decodeUtf16To(in, map, maxLength, maxKeys);
        } else {
            synchronized (map) {
                String key = null;
                String value = null;
                int totalLength = 0;
                ByteArrayOutputStream2 output = new ByteArrayOutputStream2();
                Throwable var11 = null;
                try {
                    int size = 0;
                    int c;
                    while ((c = in.read()) > 0) {
                        switch((char) c) {
                            case '%':
                                int code0 = in.read();
                                int code1 = in.read();
                                output.write(decodeHexChar(code0, code1));
                                break;
                            case '&':
                                size = output.size();
                                value = size == 0 ? "" : output.toString(charset);
                                output.setCount(0);
                                if (key != null) {
                                    map.add(key, value);
                                } else if (value != null && value.length() > 0) {
                                    map.add(value, "");
                                }
                                key = null;
                                value = null;
                                if (maxKeys > 0 && map.size() > maxKeys) {
                                    throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", map.size(), maxKeys));
                                }
                                break;
                            case '+':
                                output.write(32);
                                break;
                            case '=':
                                if (key != null) {
                                    output.write(c);
                                } else {
                                    size = output.size();
                                    key = size == 0 ? "" : output.toString(charset);
                                    output.setCount(0);
                                }
                                break;
                            default:
                                output.write(c);
                        }
                        if (maxLength >= 0 && ++totalLength > maxLength) {
                            throw new IllegalStateException("Form is too large");
                        }
                    }
                    size = output.size();
                    if (key != null) {
                        value = size == 0 ? "" : output.toString(charset);
                        output.setCount(0);
                        map.add(key, value);
                    } else if (size > 0) {
                        map.add(output.toString(charset), "");
                    }
                } catch (Throwable var24) {
                    var11 = var24;
                    throw var24;
                } finally {
                    if (output != null) {
                        if (var11 != null) {
                            try {
                                output.close();
                            } catch (Throwable var23) {
                                var11.addSuppressed(var23);
                            }
                        } else {
                            output.close();
                        }
                    }
                }
            }
        }
    }

    public static String decodeString(String encoded) {
        return decodeString(encoded, 0, encoded.length(), ENCODING);
    }

    public static String decodeString(String encoded, int offset, int length, Charset charset) {
        if (charset != null && !StandardCharsets.UTF_8.equals(charset)) {
            StringBuffer buffer = null;
            for (int i = 0; i < length; i++) {
                char c = encoded.charAt(offset + i);
                if (c >= 0 && c <= 255) {
                    if (c == '+') {
                        if (buffer == null) {
                            buffer = new StringBuffer(length);
                            buffer.append(encoded, offset, offset + i);
                        }
                        buffer.append(' ');
                    } else if (c != '%') {
                        if (buffer != null) {
                            buffer.append(c);
                        }
                    } else {
                        if (buffer == null) {
                            buffer = new StringBuffer(length);
                            buffer.append(encoded, offset, offset + i);
                        }
                        byte[] ba = new byte[length];
                        int n;
                        for (n = 0; c >= 0 && c <= 255; c = encoded.charAt(offset + i)) {
                            if (c == '%') {
                                if (i + 2 < length) {
                                    int o = offset + i + 1;
                                    i += 3;
                                    ba[n] = (byte) TypeUtil.parseInt(encoded, o, 2, 16);
                                    n++;
                                } else {
                                    ba[n++] = 63;
                                    i = length;
                                }
                            } else if (c == '+') {
                                ba[n++] = 32;
                                i++;
                            } else {
                                ba[n++] = (byte) c;
                                i++;
                            }
                            if (i >= length) {
                                break;
                            }
                        }
                        i--;
                        buffer.append(new String(ba, 0, n, charset));
                    }
                } else if (buffer == null) {
                    buffer = new StringBuffer(length);
                    buffer.append(encoded, offset, offset + i + 1);
                } else {
                    buffer.append(c);
                }
            }
            if (buffer != null) {
                return buffer.toString();
            } else {
                return offset == 0 && encoded.length() == length ? encoded : encoded.substring(offset, offset + length);
            }
        } else {
            Utf8StringBuffer buffer = null;
            for (int ix = 0; ix < length; ix++) {
                char c = encoded.charAt(offset + ix);
                if (c >= 0 && c <= 255) {
                    if (c == '+') {
                        if (buffer == null) {
                            buffer = new Utf8StringBuffer(length);
                            buffer.getStringBuffer().append(encoded, offset, offset + ix);
                        }
                        buffer.getStringBuffer().append(' ');
                    } else if (c == '%') {
                        if (buffer == null) {
                            buffer = new Utf8StringBuffer(length);
                            buffer.getStringBuffer().append(encoded, offset, offset + ix);
                        }
                        if (ix + 2 < length) {
                            int o = offset + ix + 1;
                            ix += 2;
                            byte b = (byte) TypeUtil.parseInt(encoded, o, 2, 16);
                            buffer.append(b);
                        } else {
                            buffer.getStringBuffer().append('�');
                            ix = length;
                        }
                    } else if (buffer != null) {
                        buffer.getStringBuffer().append(c);
                    }
                } else if (buffer == null) {
                    buffer = new Utf8StringBuffer(length);
                    buffer.getStringBuffer().append(encoded, offset, offset + ix + 1);
                } else {
                    buffer.getStringBuffer().append(c);
                }
            }
            if (buffer != null) {
                return buffer.toReplacedString();
            } else {
                return offset == 0 && encoded.length() == length ? encoded : encoded.substring(offset, offset + length);
            }
        }
    }

    private static char decodeHexChar(int hi, int lo) {
        try {
            return (char) ((TypeUtil.convertHexDigit(hi) << 4) + TypeUtil.convertHexDigit(lo));
        } catch (NumberFormatException var3) {
            throw new IllegalArgumentException("Not valid encoding '%" + (char) hi + (char) lo + "'");
        }
    }

    private static byte decodeHexByte(char hi, char lo) {
        try {
            return (byte) ((TypeUtil.convertHexDigit(hi) << 4) + TypeUtil.convertHexDigit(lo));
        } catch (NumberFormatException var3) {
            throw new IllegalArgumentException("Not valid encoding '%" + hi + lo + "'");
        }
    }

    public static String encodeString(String string) {
        return encodeString(string, ENCODING);
    }

    public static String encodeString(String string, Charset charset) {
        if (charset == null) {
            charset = ENCODING;
        }
        byte[] bytes = null;
        bytes = string.getBytes(charset);
        int len = bytes.length;
        byte[] encoded = new byte[bytes.length * 3];
        int n = 0;
        boolean noEncode = true;
        for (int i = 0; i < len; i++) {
            byte b = bytes[i];
            if (b == 32) {
                noEncode = false;
                encoded[n++] = 43;
            } else if ((b < 97 || b > 122) && (b < 65 || b > 90) && (b < 48 || b > 57)) {
                noEncode = false;
                encoded[n++] = 37;
                byte nibble = (byte) ((b & 240) >> 4);
                if (nibble >= 10) {
                    encoded[n++] = (byte) (65 + nibble - 10);
                } else {
                    encoded[n++] = (byte) (48 + nibble);
                }
                nibble = (byte) (b & 15);
                if (nibble >= 10) {
                    encoded[n++] = (byte) (65 + nibble - 10);
                } else {
                    encoded[n++] = (byte) (48 + nibble);
                }
            } else {
                encoded[n++] = b;
            }
        }
        return noEncode ? string : new String(encoded, 0, n, charset);
    }

    public Object clone() {
        return new UrlEncoded(this);
    }

    static {
        Charset encoding;
        try {
            String charset = System.getProperty("info.journeymap.shaded.org.eclipse.jetty.util.UrlEncoding.charset");
            encoding = charset == null ? StandardCharsets.UTF_8 : Charset.forName(charset);
        } catch (Exception var2) {
            LOG.warn(var2);
            encoding = StandardCharsets.UTF_8;
        }
        ENCODING = encoding;
    }
}