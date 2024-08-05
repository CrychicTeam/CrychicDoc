package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonPrimitive;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.SyntaxError;

public class TokenParserContext implements ParserContext<JsonPrimitive> {

    private String token = "";

    private boolean complete = false;

    public TokenParserContext(int firstCodePoint) {
        this.token = this.token + (char) firstCodePoint;
    }

    @Override
    public boolean consume(int codePoint, Jankson loader) throws SyntaxError {
        if (this.complete) {
            return false;
        } else if (codePoint != 126 && !Character.isUnicodeIdentifierPart(codePoint)) {
            this.complete = true;
            return false;
        } else if (codePoint < 65535) {
            this.token = this.token + (char) codePoint;
            return true;
        } else {
            int temp = codePoint - 65536;
            int highSurrogate = (temp >>> 10) + 55296;
            int lowSurrogate = (temp & 1023) + 56320;
            this.token = this.token + (char) highSurrogate;
            this.token = this.token + (char) lowSurrogate;
            return true;
        }
    }

    @Override
    public void eof() throws SyntaxError {
        this.complete = true;
    }

    @Override
    public boolean isComplete() {
        return this.complete;
    }

    public JsonPrimitive getResult() throws SyntaxError {
        return new JsonPrimitive(this.token);
    }
}