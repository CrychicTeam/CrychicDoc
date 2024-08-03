package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonPrimitive;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.SyntaxError;

public class StringParserContext implements ParserContext<JsonPrimitive> {

    private int quote;

    private boolean escape = false;

    private StringBuilder builder = new StringBuilder();

    private boolean complete = false;

    public StringParserContext(int quote) {
        this.quote = quote;
    }

    @Override
    public boolean consume(int codePoint, Jankson loader) {
        if (this.escape) {
            this.escape = false;
            switch(codePoint) {
                case 10:
                    return true;
                case 34:
                    this.builder.append('"');
                    return true;
                case 39:
                    this.builder.append('\'');
                    return true;
                case 92:
                    this.builder.append('\\');
                    return true;
                case 98:
                    this.builder.append('\b');
                    return true;
                case 102:
                    this.builder.append('\f');
                    return true;
                case 110:
                    this.builder.append('\n');
                    return true;
                case 114:
                    this.builder.append('\r');
                    return true;
                case 116:
                    this.builder.append('\t');
                    return true;
                default:
                    this.builder.append((char) codePoint);
                    return true;
            }
        } else if (codePoint == this.quote) {
            this.complete = true;
            return true;
        } else if (codePoint == 92) {
            this.escape = true;
            return true;
        } else if (codePoint == 10) {
            this.complete = true;
            return false;
        } else if (codePoint < 65535) {
            this.builder.append((char) codePoint);
            return true;
        } else {
            int temp = codePoint - 65536;
            int highSurrogate = (temp >>> 10) + 55296;
            int lowSurrogate = (temp & 1023) + 56320;
            this.builder.append((char) highSurrogate);
            this.builder.append((char) lowSurrogate);
            return true;
        }
    }

    @Override
    public boolean isComplete() {
        return this.complete;
    }

    public JsonPrimitive getResult() {
        return new JsonPrimitive(this.builder.toString());
    }

    @Override
    public void eof() throws SyntaxError {
        throw new SyntaxError("Expected to find '" + (char) this.quote + "' to end a String, found EOF instead.");
    }
}