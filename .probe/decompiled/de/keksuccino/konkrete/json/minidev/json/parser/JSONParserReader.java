package de.keksuccino.konkrete.json.minidev.json.parser;

import de.keksuccino.konkrete.json.minidev.json.JSONValue;
import de.keksuccino.konkrete.json.minidev.json.writer.JsonReaderI;
import java.io.IOException;
import java.io.Reader;

class JSONParserReader extends JSONParserStream {

    private Reader in;

    public JSONParserReader(int permissiveMode) {
        super(permissiveMode);
    }

    public Object parse(Reader in) throws ParseException {
        return this.parse(in, JSONValue.defaultReader.DEFAULT);
    }

    public <T> T parse(Reader in, JsonReaderI<T> mapper) throws ParseException {
        this.base = mapper.base;
        this.in = in;
        return super.parse(mapper);
    }

    @Override
    protected void read() throws IOException {
        int i = this.in.read();
        this.c = i == -1 ? 26 : (char) i;
        this.pos++;
    }

    @Override
    protected void readS() throws IOException {
        this.sb.append(this.c);
        int i = this.in.read();
        if (i == -1) {
            this.c = 26;
        } else {
            this.c = (char) i;
            this.pos++;
        }
    }

    @Override
    protected void readNoEnd() throws ParseException, IOException {
        int i = this.in.read();
        if (i == -1) {
            throw new ParseException(this.pos - 1, 3, "EOF");
        } else {
            this.c = (char) i;
        }
    }
}