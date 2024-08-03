package de.keksuccino.konkrete.json.minidev.json.parser;

import de.keksuccino.konkrete.json.minidev.json.writer.JsonReaderI;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

class JSONParserInputStream extends JSONParserReader {

    public JSONParserInputStream(int permissiveMode) {
        super(permissiveMode);
    }

    public Object parse(InputStream in) throws ParseException, UnsupportedEncodingException {
        InputStreamReader i2 = new InputStreamReader(in, "utf8");
        return super.parse(i2);
    }

    public <T> T parse(InputStream in, JsonReaderI<T> mapper) throws ParseException, UnsupportedEncodingException {
        InputStreamReader i2 = new InputStreamReader(in, "utf8");
        return super.parse(i2, mapper);
    }
}