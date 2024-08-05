package info.journeymap.shaded.org.eclipse.jetty.http;

import info.journeymap.shaded.org.eclipse.jetty.util.ArrayTrie;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Trie;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

public class MimeTypes {

    private static final Logger LOG = Log.getLogger(MimeTypes.class);

    private static final Trie<ByteBuffer> TYPES = new ArrayTrie<>(512);

    private static final Map<String, String> __dftMimeMap = new HashMap();

    private static final Map<String, String> __inferredEncodings = new HashMap();

    private static final Map<String, String> __assumedEncodings = new HashMap();

    public static final Trie<MimeTypes.Type> CACHE = new ArrayTrie<>(512);

    private final Map<String, String> _mimeMap = new HashMap();

    public synchronized Map<String, String> getMimeMap() {
        return this._mimeMap;
    }

    public void setMimeMap(Map<String, String> mimeMap) {
        this._mimeMap.clear();
        if (mimeMap != null) {
            for (Entry<String, String> ext : mimeMap.entrySet()) {
                this._mimeMap.put(StringUtil.asciiToLowerCase((String) ext.getKey()), normalizeMimeType((String) ext.getValue()));
            }
        }
    }

    public static String getDefaultMimeByExtension(String filename) {
        String type = null;
        if (filename != null) {
            int i = -1;
            while (type == null) {
                i = filename.indexOf(".", i + 1);
                if (i < 0 || i >= filename.length()) {
                    break;
                }
                String ext = StringUtil.asciiToLowerCase(filename.substring(i + 1));
                if (type == null) {
                    type = (String) __dftMimeMap.get(ext);
                }
            }
        }
        if (type == null && type == null) {
            type = (String) __dftMimeMap.get("*");
        }
        return type;
    }

    public String getMimeByExtension(String filename) {
        String type = null;
        if (filename != null) {
            int i = -1;
            while (type == null) {
                i = filename.indexOf(".", i + 1);
                if (i < 0 || i >= filename.length()) {
                    break;
                }
                String ext = StringUtil.asciiToLowerCase(filename.substring(i + 1));
                if (this._mimeMap != null) {
                    type = (String) this._mimeMap.get(ext);
                }
                if (type == null) {
                    type = (String) __dftMimeMap.get(ext);
                }
            }
        }
        if (type == null) {
            if (this._mimeMap != null) {
                type = (String) this._mimeMap.get("*");
            }
            if (type == null) {
                type = (String) __dftMimeMap.get("*");
            }
        }
        return type;
    }

    public void addMimeMapping(String extension, String type) {
        this._mimeMap.put(StringUtil.asciiToLowerCase(extension), normalizeMimeType(type));
    }

    public static Set<String> getKnownMimeTypes() {
        return new HashSet(__dftMimeMap.values());
    }

    private static String normalizeMimeType(String type) {
        MimeTypes.Type t = CACHE.get(type);
        return t != null ? t.asString() : StringUtil.asciiToLowerCase(type);
    }

    public static String getCharsetFromContentType(String value) {
        if (value == null) {
            return null;
        } else {
            int end = value.length();
            int state = 0;
            int start = 0;
            boolean quote = false;
            int i;
            for (i = 0; i < end; i++) {
                char b = value.charAt(i);
                if (quote && state != 10) {
                    if ('"' == b) {
                        quote = false;
                    }
                } else if (';' == b && state <= 8) {
                    state = 1;
                } else {
                    switch(state) {
                        case 0:
                            if ('"' == b) {
                                quote = true;
                            }
                            break;
                        case 1:
                            if ('c' == b) {
                                state = 2;
                            } else if (' ' != b) {
                                state = 0;
                            }
                            break;
                        case 2:
                            if ('h' == b) {
                                state = 3;
                            } else {
                                state = 0;
                            }
                            break;
                        case 3:
                            if ('a' == b) {
                                state = 4;
                            } else {
                                state = 0;
                            }
                            break;
                        case 4:
                            if ('r' == b) {
                                state = 5;
                            } else {
                                state = 0;
                            }
                            break;
                        case 5:
                            if ('s' == b) {
                                state = 6;
                            } else {
                                state = 0;
                            }
                            break;
                        case 6:
                            if ('e' == b) {
                                state = 7;
                            } else {
                                state = 0;
                            }
                            break;
                        case 7:
                            if ('t' == b) {
                                state = 8;
                            } else {
                                state = 0;
                            }
                            break;
                        case 8:
                            if ('=' == b) {
                                state = 9;
                            } else if (' ' != b) {
                                state = 0;
                            }
                            break;
                        case 9:
                            if (' ' != b) {
                                if ('"' == b) {
                                    quote = true;
                                    start = i + 1;
                                    state = 10;
                                } else {
                                    start = i;
                                    state = 10;
                                }
                            }
                            break;
                        case 10:
                            if (!quote && (';' == b || ' ' == b) || quote && '"' == b) {
                                return StringUtil.normalizeCharset(value, start, i - start);
                            }
                    }
                }
            }
            return state == 10 ? StringUtil.normalizeCharset(value, start, i - start) : null;
        }
    }

    public static Map<String, String> getInferredEncodings() {
        return __inferredEncodings;
    }

    public static Map<String, String> getAssumedEncodings() {
        return __inferredEncodings;
    }

    @Deprecated
    public static String inferCharsetFromContentType(String contentType) {
        return getCharsetAssumedFromContentType(contentType);
    }

    public static String getCharsetInferredFromContentType(String contentType) {
        return (String) __inferredEncodings.get(contentType);
    }

    public static String getCharsetAssumedFromContentType(String contentType) {
        return (String) __assumedEncodings.get(contentType);
    }

    public static String getContentTypeWithoutCharset(String value) {
        int end = value.length();
        int state = 0;
        int start = 0;
        boolean quote = false;
        int i = 0;
        StringBuilder builder;
        for (builder = null; i < end; i++) {
            char b = value.charAt(i);
            if ('"' == b) {
                if (quote) {
                    quote = false;
                } else {
                    quote = true;
                }
                switch(state) {
                    case 9:
                        builder = new StringBuilder();
                        builder.append(value, 0, start + 1);
                        state = 10;
                    case 10:
                        break;
                    case 11:
                        builder.append(b);
                        break;
                    default:
                        start = i;
                        state = 0;
                }
            } else if (quote) {
                if (builder != null && state != 10) {
                    builder.append(b);
                }
            } else {
                switch(state) {
                    case 0:
                        if (';' == b) {
                            state = 1;
                        } else if (' ' != b) {
                            start = i;
                        }
                        break;
                    case 1:
                        if ('c' == b) {
                            state = 2;
                        } else if (' ' != b) {
                            state = 0;
                        }
                        break;
                    case 2:
                        if ('h' == b) {
                            state = 3;
                        } else {
                            state = 0;
                        }
                        break;
                    case 3:
                        if ('a' == b) {
                            state = 4;
                        } else {
                            state = 0;
                        }
                        break;
                    case 4:
                        if ('r' == b) {
                            state = 5;
                        } else {
                            state = 0;
                        }
                        break;
                    case 5:
                        if ('s' == b) {
                            state = 6;
                        } else {
                            state = 0;
                        }
                        break;
                    case 6:
                        if ('e' == b) {
                            state = 7;
                        } else {
                            state = 0;
                        }
                        break;
                    case 7:
                        if ('t' == b) {
                            state = 8;
                        } else {
                            state = 0;
                        }
                        break;
                    case 8:
                        if ('=' == b) {
                            state = 9;
                        } else if (' ' != b) {
                            state = 0;
                        }
                        break;
                    case 9:
                        if (' ' != b) {
                            builder = new StringBuilder();
                            builder.append(value, 0, start + 1);
                            state = 10;
                        }
                        break;
                    case 10:
                        if (';' == b) {
                            builder.append(b);
                            state = 11;
                        }
                        break;
                    case 11:
                        if (' ' != b) {
                            builder.append(b);
                        }
                }
            }
        }
        return builder == null ? value : builder.toString();
    }

    static {
        for (MimeTypes.Type type : MimeTypes.Type.values()) {
            CACHE.put(type.toString(), type);
            TYPES.put(type.toString(), type.asBuffer());
            int charset = type.toString().indexOf(";charset=");
            if (charset > 0) {
                String alt = type.toString().replace(";charset=", "; charset=");
                CACHE.put(alt, type);
                TYPES.put(alt, type.asBuffer());
            }
            if (type.isCharsetAssumed()) {
                __assumedEncodings.put(type.asString(), type.getCharsetString());
            }
        }
        String resourceName = "info/journeymap/shaded/org/eclipse/jetty/http/mime.properties";
        try {
            InputStream stream = MimeTypes.class.getClassLoader().getResourceAsStream(resourceName);
            Throwable var108 = null;
            try {
                if (stream == null) {
                    LOG.warn("Missing mime-type resource: {}", resourceName);
                } else {
                    try {
                        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                        Throwable var112 = null;
                        try {
                            Properties props = new Properties();
                            props.load(reader);
                            props.stringPropertyNames().stream().filter(x -> x != null).forEach(x -> {
                                String var10000 = (String) __dftMimeMap.put(StringUtil.asciiToLowerCase(x), normalizeMimeType(props.getProperty(x)));
                            });
                            if (__dftMimeMap.size() == 0) {
                                LOG.warn("Empty mime types at {}", resourceName);
                            } else if (__dftMimeMap.size() < props.keySet().size()) {
                                LOG.warn("Duplicate or null mime-type extension in resource: {}", resourceName);
                            }
                        } catch (Throwable var93) {
                            var112 = var93;
                            throw var93;
                        } finally {
                            if (reader != null) {
                                if (var112 != null) {
                                    try {
                                        reader.close();
                                    } catch (Throwable var92) {
                                        var112.addSuppressed(var92);
                                    }
                                } else {
                                    reader.close();
                                }
                            }
                        }
                    } catch (IOException var100) {
                        LOG.warn(var100.toString());
                        LOG.debug(var100);
                    }
                }
            } catch (Throwable var101) {
                var108 = var101;
                throw var101;
            } finally {
                if (stream != null) {
                    if (var108 != null) {
                        try {
                            stream.close();
                        } catch (Throwable var91) {
                            var108.addSuppressed(var91);
                        }
                    } else {
                        stream.close();
                    }
                }
            }
        } catch (IOException var103) {
            LOG.warn(var103.toString());
            LOG.debug(var103);
        }
        resourceName = "info/journeymap/shaded/org/eclipse/jetty/http/encoding.properties";
        try {
            InputStream stream = MimeTypes.class.getClassLoader().getResourceAsStream(resourceName);
            Throwable var109 = null;
            try {
                if (stream == null) {
                    LOG.warn("Missing encoding resource: {}", resourceName);
                } else {
                    try {
                        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                        Throwable var113 = null;
                        try {
                            Properties props = new Properties();
                            props.load(reader);
                            props.stringPropertyNames().stream().filter(t -> t != null).forEach(t -> {
                                String charsetx = props.getProperty(t);
                                if (charsetx.startsWith("-")) {
                                    __assumedEncodings.put(t, charsetx.substring(1));
                                } else {
                                    __inferredEncodings.put(t, props.getProperty(t));
                                }
                            });
                            if (__inferredEncodings.size() == 0) {
                                LOG.warn("Empty encodings at {}", resourceName);
                            } else if (__inferredEncodings.size() + __assumedEncodings.size() < props.keySet().size()) {
                                LOG.warn("Null or duplicate encodings in resource: {}", resourceName);
                            }
                        } catch (Throwable var90) {
                            var113 = var90;
                            throw var90;
                        } finally {
                            if (reader != null) {
                                if (var113 != null) {
                                    try {
                                        reader.close();
                                    } catch (Throwable var89) {
                                        var113.addSuppressed(var89);
                                    }
                                } else {
                                    reader.close();
                                }
                            }
                        }
                    } catch (IOException var95) {
                        LOG.warn(var95.toString());
                        LOG.debug(var95);
                    }
                }
            } catch (Throwable var96) {
                var109 = var96;
                throw var96;
            } finally {
                if (stream != null) {
                    if (var109 != null) {
                        try {
                            stream.close();
                        } catch (Throwable var88) {
                            var109.addSuppressed(var88);
                        }
                    } else {
                        stream.close();
                    }
                }
            }
        } catch (IOException var98) {
            LOG.warn(var98.toString());
            LOG.debug(var98);
        }
    }

    public static enum Type {

        FORM_ENCODED("application/x-www-form-urlencoded"),
        MESSAGE_HTTP("message/http"),
        MULTIPART_BYTERANGES("multipart/byteranges"),
        TEXT_HTML("text/html"),
        TEXT_PLAIN("text/plain"),
        TEXT_XML("text/xml"),
        TEXT_JSON("text/json", StandardCharsets.UTF_8),
        APPLICATION_JSON("application/json", StandardCharsets.UTF_8),
        TEXT_HTML_8859_1("text/html;charset=iso-8859-1", TEXT_HTML),
        TEXT_HTML_UTF_8("text/html;charset=utf-8", TEXT_HTML),
        TEXT_PLAIN_8859_1("text/plain;charset=iso-8859-1", TEXT_PLAIN),
        TEXT_PLAIN_UTF_8("text/plain;charset=utf-8", TEXT_PLAIN),
        TEXT_XML_8859_1("text/xml;charset=iso-8859-1", TEXT_XML),
        TEXT_XML_UTF_8("text/xml;charset=utf-8", TEXT_XML),
        TEXT_JSON_8859_1("text/json;charset=iso-8859-1", TEXT_JSON),
        TEXT_JSON_UTF_8("text/json;charset=utf-8", TEXT_JSON),
        APPLICATION_JSON_8859_1("application/json;charset=iso-8859-1", APPLICATION_JSON),
        APPLICATION_JSON_UTF_8("application/json;charset=utf-8", APPLICATION_JSON);

        private final String _string;

        private final MimeTypes.Type _base;

        private final ByteBuffer _buffer;

        private final Charset _charset;

        private final String _charsetString;

        private final boolean _assumedCharset;

        private final HttpField _field;

        private Type(String s) {
            this._string = s;
            this._buffer = BufferUtil.toBuffer(s);
            this._base = this;
            this._charset = null;
            this._charsetString = null;
            this._assumedCharset = false;
            this._field = new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, this._string);
        }

        private Type(String s, MimeTypes.Type base) {
            this._string = s;
            this._buffer = BufferUtil.toBuffer(s);
            this._base = base;
            int i = s.indexOf(";charset=");
            this._charset = Charset.forName(s.substring(i + 9));
            this._charsetString = this._charset.toString().toLowerCase(Locale.ENGLISH);
            this._assumedCharset = false;
            this._field = new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, this._string);
        }

        private Type(String s, Charset cs) {
            this._string = s;
            this._base = this;
            this._buffer = BufferUtil.toBuffer(s);
            this._charset = cs;
            this._charsetString = this._charset == null ? null : this._charset.toString().toLowerCase(Locale.ENGLISH);
            this._assumedCharset = true;
            this._field = new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, this._string);
        }

        public ByteBuffer asBuffer() {
            return this._buffer.asReadOnlyBuffer();
        }

        public Charset getCharset() {
            return this._charset;
        }

        public String getCharsetString() {
            return this._charsetString;
        }

        public boolean is(String s) {
            return this._string.equalsIgnoreCase(s);
        }

        public String asString() {
            return this._string;
        }

        public String toString() {
            return this._string;
        }

        public boolean isCharsetAssumed() {
            return this._assumedCharset;
        }

        public HttpField getContentTypeField() {
            return this._field;
        }

        public MimeTypes.Type getBaseType() {
            return this._base;
        }
    }
}