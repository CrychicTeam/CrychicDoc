package de.keksuccino.konkrete.json.minidev.json.parser;

import de.keksuccino.konkrete.json.minidev.json.JSONValue;
import de.keksuccino.konkrete.json.minidev.json.writer.JsonReaderI;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class JSONParser {

    public static final int ACCEPT_SIMPLE_QUOTE = 1;

    public static final int ACCEPT_NON_QUOTE = 2;

    public static final int ACCEPT_NAN = 4;

    public static final int IGNORE_CONTROL_CHAR = 8;

    public static final int USE_INTEGER_STORAGE = 16;

    public static final int ACCEPT_LEADING_ZERO = 32;

    public static final int ACCEPT_USELESS_COMMA = 64;

    public static final int USE_HI_PRECISION_FLOAT = 128;

    public static final int ACCEPT_TAILLING_DATA = 256;

    public static final int ACCEPT_TAILLING_SPACE = 512;

    public static final int REJECT_127_CHAR = 1024;

    public static final int BIG_DIGIT_UNRESTRICTED = 2048;

    public static final int MODE_PERMISSIVE = -1;

    public static final int MODE_RFC4627 = 656;

    public static final int MODE_JSON_SIMPLE = 4032;

    public static final int MODE_STRICTEST = 1168;

    public static int DEFAULT_PERMISSIVE_MODE = System.getProperty("JSON_SMART_SIMPLE") != null ? 4032 : -1;

    private int mode;

    private JSONParserInputStream pBinStream;

    private JSONParserByteArray pBytes;

    private JSONParserReader pStream;

    private JSONParserString pString;

    private JSONParserReader getPStream() {
        if (this.pStream == null) {
            this.pStream = new JSONParserReader(this.mode);
        }
        return this.pStream;
    }

    private JSONParserInputStream getPBinStream() {
        if (this.pBinStream == null) {
            this.pBinStream = new JSONParserInputStream(this.mode);
        }
        return this.pBinStream;
    }

    private JSONParserString getPString() {
        if (this.pString == null) {
            this.pString = new JSONParserString(this.mode);
        }
        return this.pString;
    }

    private JSONParserByteArray getPBytes() {
        if (this.pBytes == null) {
            this.pBytes = new JSONParserByteArray(this.mode);
        }
        return this.pBytes;
    }

    /**
     * @deprecated
     */
    public JSONParser() {
        this.mode = DEFAULT_PERMISSIVE_MODE;
    }

    public JSONParser(int permissifMode) {
        this.mode = permissifMode;
    }

    public Object parse(byte[] in) throws ParseException {
        return this.getPBytes().parse(in);
    }

    public <T> T parse(byte[] in, JsonReaderI<T> mapper) throws ParseException {
        return this.getPBytes().parse(in, mapper);
    }

    public <T> T parse(byte[] in, Class<T> mapTo) throws ParseException {
        return this.getPBytes().parse(in, JSONValue.defaultReader.getMapper(mapTo));
    }

    public Object parse(InputStream in) throws ParseException, UnsupportedEncodingException {
        return this.getPBinStream().parse(in);
    }

    public <T> T parse(InputStream in, JsonReaderI<T> mapper) throws ParseException, UnsupportedEncodingException {
        return this.getPBinStream().parse(in, mapper);
    }

    public <T> T parse(InputStream in, Class<T> mapTo) throws ParseException, UnsupportedEncodingException {
        return this.getPBinStream().parse(in, JSONValue.defaultReader.getMapper(mapTo));
    }

    public Object parse(Reader in) throws ParseException {
        return this.getPStream().parse(in);
    }

    public <T> T parse(Reader in, JsonReaderI<T> mapper) throws ParseException {
        return this.getPStream().parse(in, mapper);
    }

    public <T> T parse(Reader in, Class<T> mapTo) throws ParseException {
        return this.getPStream().parse(in, JSONValue.defaultReader.getMapper(mapTo));
    }

    public Object parse(String in) throws ParseException {
        return this.getPString().parse(in);
    }

    public <T> T parse(String in, JsonReaderI<T> mapper) throws ParseException {
        return this.getPString().parse(in, mapper);
    }

    public <T> T parse(String in, Class<T> mapTo) throws ParseException {
        return this.getPString().parse(in, JSONValue.defaultReader.getMapper(mapTo));
    }
}