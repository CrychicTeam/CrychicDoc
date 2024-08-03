package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl;

import java.util.Locale;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonPrimitive;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.SyntaxError;

public class NumberParserContext implements ParserContext<JsonPrimitive> {

    private String numberString = "";

    private boolean complete = false;

    private String acceptedChars = "0123456789.+-eExabcdefInityNn";

    public NumberParserContext(int firstCodePoint) {
        this.numberString = this.numberString + (char) firstCodePoint;
    }

    @Override
    public boolean consume(int codePoint, Jankson loader) throws SyntaxError {
        if (this.complete) {
            return false;
        } else if (this.acceptedChars.indexOf(codePoint) != -1) {
            this.numberString = this.numberString + (char) codePoint;
            return true;
        } else {
            this.complete = true;
            return false;
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
        String lc = this.numberString.toLowerCase(Locale.ROOT);
        if (lc.equals("infinity") || lc.equals("+infinity")) {
            return new JsonPrimitive(Double.POSITIVE_INFINITY);
        } else if (lc.equals("-infinity")) {
            return new JsonPrimitive(Double.NEGATIVE_INFINITY);
        } else if (lc.equals("nan")) {
            return new JsonPrimitive(Double.NaN);
        } else {
            if (this.numberString.startsWith(".")) {
                this.numberString = '0' + this.numberString;
            }
            if (this.numberString.endsWith(".")) {
                this.numberString = this.numberString + '0';
            }
            if (this.numberString.startsWith("0x")) {
                this.numberString = this.numberString.substring(2);
                try {
                    Long l = Long.parseUnsignedLong(this.numberString, 16);
                    return new JsonPrimitive(l);
                } catch (NumberFormatException var3) {
                    throw new SyntaxError("Tried to parse '" + this.numberString + "' as a hexadecimal number, but it appears to be invalid.");
                }
            } else if (this.numberString.startsWith("-0x")) {
                this.numberString = this.numberString.substring(3);
                try {
                    Long l = -Long.parseUnsignedLong(this.numberString, 16);
                    return new JsonPrimitive(l);
                } catch (NumberFormatException var4) {
                    throw new SyntaxError("Tried to parse '" + this.numberString + "' as a hexadecimal number, but it appears to be invalid.");
                }
            } else if (this.numberString.indexOf(46) != -1) {
                try {
                    Double d = Double.valueOf(this.numberString);
                    return new JsonPrimitive(d);
                } catch (NumberFormatException var5) {
                    throw new SyntaxError("Tried to parse '" + this.numberString + "' as a floating-point number, but it appears to be invalid.");
                }
            } else {
                try {
                    Long l = Long.valueOf(this.numberString);
                    return new JsonPrimitive(l);
                } catch (NumberFormatException var6) {
                    throw new SyntaxError("Tried to parse '" + this.numberString + "' as an integer, but it appears to be invalid.");
                }
            }
        }
    }
}