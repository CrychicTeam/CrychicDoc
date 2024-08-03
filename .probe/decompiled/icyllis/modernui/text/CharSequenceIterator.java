package icyllis.modernui.text;

import java.text.CharacterIterator;
import java.util.Objects;
import javax.annotation.Nonnull;

public class CharSequenceIterator implements CharacterIterator {

    private final int mBeginIndex;

    private final int mEndIndex;

    private int mIndex;

    private final CharSequence mCharSeq;

    public CharSequenceIterator(@Nonnull CharSequence text, int start, int end) {
        this.mIndex = Objects.checkFromToIndex(start, end, text.length());
        this.mCharSeq = text;
        this.mBeginIndex = start;
        this.mEndIndex = end;
    }

    public char first() {
        this.mIndex = this.mBeginIndex;
        return this.current();
    }

    public char last() {
        if (this.mBeginIndex == this.mEndIndex) {
            this.mIndex = this.mEndIndex;
            return '\uffff';
        } else {
            this.mIndex = this.mEndIndex - 1;
            return this.mCharSeq.charAt(this.mIndex);
        }
    }

    public char current() {
        return this.mIndex == this.mEndIndex ? '\uffff' : this.mCharSeq.charAt(this.mIndex);
    }

    public char next() {
        this.mIndex++;
        if (this.mIndex >= this.mEndIndex) {
            this.mIndex = this.mEndIndex;
            return '\uffff';
        } else {
            return this.mCharSeq.charAt(this.mIndex);
        }
    }

    public char previous() {
        if (this.mIndex <= this.mBeginIndex) {
            return '\uffff';
        } else {
            this.mIndex--;
            return this.mCharSeq.charAt(this.mIndex);
        }
    }

    public char setIndex(int position) {
        if (this.mBeginIndex <= position && position <= this.mEndIndex) {
            this.mIndex = position;
            return this.current();
        } else {
            throw new IllegalArgumentException("invalid position");
        }
    }

    public int getBeginIndex() {
        return this.mBeginIndex;
    }

    public int getEndIndex() {
        return this.mEndIndex;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new InternalError();
        }
    }
}