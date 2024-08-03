package icyllis.modernui.graphics.text;

import java.text.CharacterIterator;
import java.util.Objects;
import javax.annotation.Nonnull;

public class CharArrayIterator implements CharacterIterator {

    private final char[] text;

    private final int start;

    private final int end;

    private int pos;

    public CharArrayIterator(char[] text) {
        this(text, 0, text.length);
    }

    public CharArrayIterator(char[] text, int start) {
        this(text, start, text.length);
    }

    public CharArrayIterator(@Nonnull char[] text, int start, int end) {
        this.pos = Objects.checkFromToIndex(start, end, text.length);
        this.text = text;
        this.start = start;
        this.end = end;
    }

    public char first() {
        this.pos = this.start;
        return this.current();
    }

    public char last() {
        this.pos = this.end != this.start ? this.end - 1 : this.end;
        return this.current();
    }

    public char current() {
        return this.pos >= this.start && this.pos < this.end ? this.text[this.pos] : '\uffff';
    }

    public char next() {
        this.pos++;
        if (this.pos >= this.end) {
            this.pos = this.end;
            return '\uffff';
        } else {
            return this.current();
        }
    }

    public char previous() {
        if (this.pos == this.start) {
            return '\uffff';
        } else {
            this.pos--;
            return this.current();
        }
    }

    public char setIndex(int position) {
        if (position >= this.start && position <= this.end) {
            this.pos = position;
            return this.current();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getBeginIndex() {
        return this.start;
    }

    public int getEndIndex() {
        return this.end;
    }

    public int getIndex() {
        return this.pos;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException var2) {
            return null;
        }
    }
}