package icyllis.modernui.text.method;

import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.BreakIterator;
import icyllis.modernui.ModernUI;
import icyllis.modernui.text.CharSequenceIterator;
import java.util.Locale;
import javax.annotation.Nonnull;

public class WordIterator {

    private static final int WINDOW_WIDTH = 50;

    private int mStart;

    private int mEnd;

    private CharSequence mCharSeq;

    private final BreakIterator mIterator;

    public static final int GC_P_MASK = UCharacterProperty.getMask(22) | UCharacterProperty.getMask(19) | UCharacterProperty.getMask(21) | UCharacterProperty.getMask(29) | UCharacterProperty.getMask(28) | UCharacterProperty.getMask(23) | UCharacterProperty.getMask(20);

    public WordIterator() {
        this(ModernUI.getSelectedLocale());
    }

    public WordIterator(Locale locale) {
        this.mIterator = BreakIterator.getWordInstance(locale);
    }

    public void setCharSequence(@Nonnull CharSequence charSequence, int start, int end) {
        if (0 <= start && end <= charSequence.length()) {
            this.mCharSeq = charSequence;
            this.mStart = Math.max(0, start - 50);
            this.mEnd = Math.min(charSequence.length(), end + 50);
            this.mIterator.setText(new CharSequenceIterator(charSequence, this.mStart, this.mEnd));
        } else {
            throw new IndexOutOfBoundsException("input indexes are outside the CharSequence");
        }
    }

    public int preceding(int offset) {
        this.checkOffsetIsValid(offset);
        do {
            offset = this.mIterator.preceding(offset);
        } while (offset != -1 && !this.isOnLetterOrDigit(offset));
        return offset;
    }

    public int following(int offset) {
        this.checkOffsetIsValid(offset);
        do {
            offset = this.mIterator.following(offset);
        } while (offset != -1 && !this.isAfterLetterOrDigit(offset));
        return offset;
    }

    public boolean isBoundary(int offset) {
        this.checkOffsetIsValid(offset);
        return this.mIterator.isBoundary(offset);
    }

    public int nextBoundary(int offset) {
        this.checkOffsetIsValid(offset);
        return this.mIterator.following(offset);
    }

    public int prevBoundary(int offset) {
        this.checkOffsetIsValid(offset);
        return this.mIterator.preceding(offset);
    }

    private int getBeginning(int offset, boolean getPrevWordBeginningOnTwoWordsBoundary) {
        this.checkOffsetIsValid(offset);
        if (!this.isOnLetterOrDigit(offset)) {
            return this.isAfterLetterOrDigit(offset) ? this.mIterator.preceding(offset) : -1;
        } else {
            return !this.mIterator.isBoundary(offset) || this.isAfterLetterOrDigit(offset) && getPrevWordBeginningOnTwoWordsBoundary ? this.mIterator.preceding(offset) : offset;
        }
    }

    private int getEnd(int offset, boolean getNextWordEndOnTwoWordBoundary) {
        this.checkOffsetIsValid(offset);
        if (!this.isAfterLetterOrDigit(offset)) {
            return this.isOnLetterOrDigit(offset) ? this.mIterator.following(offset) : -1;
        } else {
            return !this.mIterator.isBoundary(offset) || this.isOnLetterOrDigit(offset) && getNextWordEndOnTwoWordBoundary ? this.mIterator.following(offset) : offset;
        }
    }

    public int getPunctuationBeginning(int offset) {
        this.checkOffsetIsValid(offset);
        while (offset != -1 && !this.isPunctuationStartBoundary(offset)) {
            offset = this.prevBoundary(offset);
        }
        return offset;
    }

    public int getPunctuationEnd(int offset) {
        this.checkOffsetIsValid(offset);
        while (offset != -1 && !this.isPunctuationEndBoundary(offset)) {
            offset = this.nextBoundary(offset);
        }
        return offset;
    }

    public boolean isAfterPunctuation(int offset) {
        if (this.mStart < offset && offset <= this.mEnd) {
            int codePoint = Character.codePointBefore(this.mCharSeq, offset);
            return isPunctuation(codePoint);
        } else {
            return false;
        }
    }

    public boolean isOnPunctuation(int offset) {
        if (this.mStart <= offset && offset < this.mEnd) {
            int codePoint = Character.codePointAt(this.mCharSeq, offset);
            return isPunctuation(codePoint);
        } else {
            return false;
        }
    }

    public static boolean isMidWordPunctuation(Locale locale, int codePoint) {
        int wb = UCharacter.getIntPropertyValue(codePoint, 4116);
        return wb == 4 || wb == 11 || wb == 15;
    }

    private boolean isPunctuationStartBoundary(int offset) {
        return this.isOnPunctuation(offset) && !this.isAfterPunctuation(offset);
    }

    private boolean isPunctuationEndBoundary(int offset) {
        return !this.isOnPunctuation(offset) && this.isAfterPunctuation(offset);
    }

    public static boolean isPunctuation(int cp) {
        return (UCharacterProperty.getMask(UCharacter.getType(cp)) & GC_P_MASK) != 0;
    }

    private boolean isAfterLetterOrDigit(int offset) {
        if (this.mStart < offset && offset <= this.mEnd) {
            int codePoint = Character.codePointBefore(this.mCharSeq, offset);
            return UCharacter.isLetterOrDigit(codePoint);
        } else {
            return false;
        }
    }

    private boolean isOnLetterOrDigit(int offset) {
        if (this.mStart <= offset && offset < this.mEnd) {
            int codePoint = Character.codePointAt(this.mCharSeq, offset);
            return UCharacter.isLetterOrDigit(codePoint);
        } else {
            return false;
        }
    }

    private void checkOffsetIsValid(int offset) {
        if (this.mStart > offset || offset > this.mEnd) {
            throw new IllegalArgumentException("Invalid offset: " + offset + ". Valid range is [" + this.mStart + ", " + this.mEnd + "]");
        }
    }
}