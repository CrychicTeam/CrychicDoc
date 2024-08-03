package dev.latvian.mods.unit.token;

public class CharStream {

    public final char[] chars;

    public int position;

    public boolean skipWhitespace;

    public CharStream(char[] c) {
        this.chars = c;
        this.position = -1;
        this.skipWhitespace = true;
    }

    public char next() {
        if (++this.position >= this.chars.length) {
            return '\u0000';
        } else {
            while (this.skipWhitespace && this.chars[this.position] <= ' ') {
                this.position++;
                if (this.position >= this.chars.length) {
                    return '\u0000';
                }
            }
            return this.chars[this.position];
        }
    }

    public boolean nextIf(char match) {
        if (this.peek() == match) {
            this.next();
            return true;
        } else {
            return false;
        }
    }

    public char peek(int ahead) {
        if (this.position + ahead >= this.chars.length) {
            return '\u0000';
        } else {
            while (this.skipWhitespace && this.chars[this.position + ahead] <= ' ') {
                if (this.position + ++ahead >= this.chars.length) {
                    return '\u0000';
                }
            }
            return this.chars[this.position + ahead];
        }
    }

    public char peek() {
        return this.peek(1);
    }
}