package de.keksuccino.konkrete.json.minidev.json.parser;

import de.keksuccino.konkrete.json.minidev.json.JSONValue;
import de.keksuccino.konkrete.json.minidev.json.writer.JsonReaderI;

class JSONParserString extends JSONParserMemory {

    private String in;

    public JSONParserString(int permissiveMode) {
        super(permissiveMode);
    }

    public Object parse(String in) throws ParseException {
        return this.parse(in, JSONValue.defaultReader.DEFAULT);
    }

    public <T> T parse(String in, JsonReaderI<T> mapper) throws ParseException {
        this.base = mapper.base;
        this.in = in;
        this.len = in.length();
        return this.parse(mapper);
    }

    @Override
    protected void extractString(int beginIndex, int endIndex) {
        this.xs = this.in.substring(beginIndex, endIndex);
    }

    @Override
    protected void extractStringTrim(int start, int stop) {
        while (start < stop - 1 && Character.isWhitespace(this.in.charAt(start))) {
            start++;
        }
        while (stop - 1 > start && Character.isWhitespace(this.in.charAt(stop - 1))) {
            stop--;
        }
        this.extractString(start, stop);
    }

    @Override
    protected int indexOf(char c, int pos) {
        return this.in.indexOf(c, pos);
    }

    @Override
    protected void read() {
        if (++this.pos >= this.len) {
            this.c = 26;
        } else {
            this.c = this.in.charAt(this.pos);
        }
    }

    @Override
    protected void readS() {
        if (++this.pos >= this.len) {
            this.c = 26;
        } else {
            this.c = this.in.charAt(this.pos);
        }
    }

    @Override
    protected void readNoEnd() throws ParseException {
        if (++this.pos >= this.len) {
            this.c = 26;
            throw new ParseException(this.pos - 1, 3, "EOF");
        } else {
            this.c = this.in.charAt(this.pos);
        }
    }
}