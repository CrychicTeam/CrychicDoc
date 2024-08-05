package dev.latvian.mods.rhino.json;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ScriptRuntime;
import dev.latvian.mods.rhino.Scriptable;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    private final Scriptable scope;

    private int pos;

    private int length;

    private String src;

    private static int fromHex(char c) {
        return c >= 48 && c <= 57 ? c - 48 : (c >= 65 && c <= 70 ? c - 65 + 10 : (c >= 97 && c <= 102 ? c - 97 + 10 : -1));
    }

    public JsonParser(Scriptable scope) {
        this.scope = scope;
    }

    public synchronized Object parseValue(Context cx, String json) throws JsonParser.ParseException {
        if (json == null) {
            throw new JsonParser.ParseException("Input string may not be null");
        } else {
            this.pos = 0;
            this.length = json.length();
            this.src = json;
            Object value = this.readValue(cx);
            this.consumeWhitespace();
            if (this.pos < this.length) {
                throw new JsonParser.ParseException("Expected end of stream at char " + this.pos);
            } else {
                return value;
            }
        }
    }

    private Object readValue(Context cx) throws JsonParser.ParseException {
        this.consumeWhitespace();
        if (this.pos < this.length) {
            char c = this.src.charAt(this.pos++);
            return switch(c) {
                case '"' ->
                    this.readString();
                case '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' ->
                    this.readNumber(c);
                case '[' ->
                    this.readArray(cx);
                case 'f' ->
                    this.readFalse();
                case 'n' ->
                    this.readNull();
                case 't' ->
                    this.readTrue();
                case '{' ->
                    this.readObject(cx);
                default ->
                    throw new JsonParser.ParseException("Unexpected token: " + c);
            };
        } else {
            throw new JsonParser.ParseException("Empty JSON string");
        }
    }

    private Object readObject(Context cx) throws JsonParser.ParseException {
        this.consumeWhitespace();
        Scriptable object = cx.newObject(this.scope);
        if (this.pos < this.length && this.src.charAt(this.pos) == '}') {
            this.pos++;
            return object;
        } else {
            for (boolean needsComma = false; this.pos < this.length; this.consumeWhitespace()) {
                char c = this.src.charAt(this.pos++);
                switch(c) {
                    case '"':
                        if (needsComma) {
                            throw new JsonParser.ParseException("Missing comma in object literal");
                        }
                        String id = this.readString();
                        this.consume(':');
                        Object value = this.readValue(cx);
                        long index = ScriptRuntime.indexFromString(id);
                        if (index < 0L) {
                            object.put(cx, id, object, value);
                        } else {
                            object.put(cx, (int) index, object, value);
                        }
                        needsComma = true;
                        break;
                    case ',':
                        if (!needsComma) {
                            throw new JsonParser.ParseException("Unexpected comma in object literal");
                        }
                        needsComma = false;
                        break;
                    case '}':
                        if (!needsComma) {
                            throw new JsonParser.ParseException("Unexpected comma in object literal");
                        }
                        return object;
                    default:
                        throw new JsonParser.ParseException("Unexpected token in object literal");
                }
            }
            throw new JsonParser.ParseException("Unterminated object literal");
        }
    }

    private Object readArray(Context cx) throws JsonParser.ParseException {
        this.consumeWhitespace();
        if (this.pos < this.length && this.src.charAt(this.pos) == ']') {
            this.pos++;
            return cx.newArray(this.scope, 0);
        } else {
            List<Object> list = new ArrayList();
            for (boolean needsComma = false; this.pos < this.length; this.consumeWhitespace()) {
                char c = this.src.charAt(this.pos);
                switch(c) {
                    case ',':
                        if (!needsComma) {
                            throw new JsonParser.ParseException("Unexpected comma in array literal");
                        }
                        needsComma = false;
                        this.pos++;
                        break;
                    case ']':
                        if (!needsComma) {
                            throw new JsonParser.ParseException("Unexpected comma in array literal");
                        }
                        this.pos++;
                        return cx.newArray(this.scope, list.toArray());
                    default:
                        if (needsComma) {
                            throw new JsonParser.ParseException("Missing comma in array literal");
                        }
                        list.add(this.readValue(cx));
                        needsComma = true;
                }
            }
            throw new JsonParser.ParseException("Unterminated array literal");
        }
    }

    private String readString() throws JsonParser.ParseException {
        int stringStart = this.pos;
        while (this.pos < this.length) {
            char c = this.src.charAt(this.pos++);
            if (c <= 31) {
                throw new JsonParser.ParseException("String contains control character");
            }
            if (c == '\\') {
                break;
            }
            if (c == '"') {
                return this.src.substring(stringStart, this.pos - 1);
            }
        }
        StringBuilder b = new StringBuilder();
        while (this.pos < this.length) {
            assert this.src.charAt(this.pos - 1) == '\\';
            b.append(this.src, stringStart, this.pos - 1);
            if (this.pos >= this.length) {
                throw new JsonParser.ParseException("Unterminated string");
            }
            char cx = this.src.charAt(this.pos++);
            switch(cx) {
                case '"':
                    b.append('"');
                    break;
                case '/':
                    b.append('/');
                    break;
                case '\\':
                    b.append('\\');
                    break;
                case 'b':
                    b.append('\b');
                    break;
                case 'f':
                    b.append('\f');
                    break;
                case 'n':
                    b.append('\n');
                    break;
                case 'r':
                    b.append('\r');
                    break;
                case 't':
                    b.append('\t');
                    break;
                case 'u':
                    if (this.length - this.pos < 5) {
                        throw new JsonParser.ParseException("Invalid character code: \\u" + this.src.substring(this.pos));
                    }
                    int code = fromHex(this.src.charAt(this.pos)) << 12 | fromHex(this.src.charAt(this.pos + 1)) << 8 | fromHex(this.src.charAt(this.pos + 2)) << 4 | fromHex(this.src.charAt(this.pos + 3));
                    if (code < 0) {
                        throw new JsonParser.ParseException("Invalid character code: " + this.src.substring(this.pos, this.pos + 4));
                    }
                    this.pos += 4;
                    b.append((char) code);
                    break;
                default:
                    throw new JsonParser.ParseException("Unexpected character in string: '\\" + cx + "'");
            }
            stringStart = this.pos;
            while (this.pos < this.length) {
                cx = this.src.charAt(this.pos++);
                if (cx <= 31) {
                    throw new JsonParser.ParseException("String contains control character");
                }
                if (cx == '\\') {
                    break;
                }
                if (cx == '"') {
                    b.append(this.src, stringStart, this.pos - 1);
                    return b.toString();
                }
            }
        }
        throw new JsonParser.ParseException("Unterminated string literal");
    }

    private Number readNumber(char c) throws JsonParser.ParseException {
        assert c == '-' || c >= '0' && c <= '9';
        int numberStart = this.pos - 1;
        if (c == '-') {
            c = this.nextOrNumberError(numberStart);
            if (c < '0' || c > '9') {
                throw this.numberError(numberStart, this.pos);
            }
        }
        if (c != '0') {
            this.readDigits();
        }
        if (this.pos < this.length) {
            c = this.src.charAt(this.pos);
            if (c == '.') {
                this.pos++;
                c = this.nextOrNumberError(numberStart);
                if (c < '0' || c > '9') {
                    throw this.numberError(numberStart, this.pos);
                }
                this.readDigits();
            }
        }
        if (this.pos < this.length) {
            c = this.src.charAt(this.pos);
            if (c == 'e' || c == 'E') {
                this.pos++;
                c = this.nextOrNumberError(numberStart);
                if (c == '-' || c == '+') {
                    c = this.nextOrNumberError(numberStart);
                }
                if (c < '0' || c > '9') {
                    throw this.numberError(numberStart, this.pos);
                }
                this.readDigits();
            }
        }
        String num = this.src.substring(numberStart, this.pos);
        double dval = Double.parseDouble(num);
        int ival = (int) dval;
        return dval;
    }

    private JsonParser.ParseException numberError(int start, int end) {
        return new JsonParser.ParseException("Unsupported number format: " + this.src.substring(start, end));
    }

    private char nextOrNumberError(int numberStart) throws JsonParser.ParseException {
        if (this.pos >= this.length) {
            throw this.numberError(numberStart, this.length);
        } else {
            return this.src.charAt(this.pos++);
        }
    }

    private void readDigits() {
        while (this.pos < this.length) {
            char c = this.src.charAt(this.pos);
            if (c >= '0' && c <= '9') {
                this.pos++;
                continue;
            }
            break;
        }
    }

    private Boolean readTrue() throws JsonParser.ParseException {
        if (this.length - this.pos >= 3 && this.src.charAt(this.pos) == 'r' && this.src.charAt(this.pos + 1) == 'u' && this.src.charAt(this.pos + 2) == 'e') {
            this.pos += 3;
            return Boolean.TRUE;
        } else {
            throw new JsonParser.ParseException("Unexpected token: t");
        }
    }

    private Boolean readFalse() throws JsonParser.ParseException {
        if (this.length - this.pos >= 4 && this.src.charAt(this.pos) == 'a' && this.src.charAt(this.pos + 1) == 'l' && this.src.charAt(this.pos + 2) == 's' && this.src.charAt(this.pos + 3) == 'e') {
            this.pos += 4;
            return Boolean.FALSE;
        } else {
            throw new JsonParser.ParseException("Unexpected token: f");
        }
    }

    private Object readNull() throws JsonParser.ParseException {
        if (this.length - this.pos >= 3 && this.src.charAt(this.pos) == 'u' && this.src.charAt(this.pos + 1) == 'l' && this.src.charAt(this.pos + 2) == 'l') {
            this.pos += 3;
            return null;
        } else {
            throw new JsonParser.ParseException("Unexpected token: n");
        }
    }

    private void consumeWhitespace() {
        while (this.pos < this.length) {
            char c = this.src.charAt(this.pos);
            switch(c) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    this.pos++;
                    break;
                default:
                    return;
            }
        }
    }

    private void consume(char token) throws JsonParser.ParseException {
        this.consumeWhitespace();
        if (this.pos >= this.length) {
            throw new JsonParser.ParseException("Expected " + token + " but reached end of stream");
        } else {
            char c = this.src.charAt(this.pos++);
            if (c != token) {
                throw new JsonParser.ParseException("Expected " + token + " found " + c);
            }
        }
    }

    public static class ParseException extends Exception {

        private static final long serialVersionUID = 4804542791749920772L;

        ParseException(String message) {
            super(message);
        }

        ParseException(Exception cause) {
            super(cause);
        }
    }
}