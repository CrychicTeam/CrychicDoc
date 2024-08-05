package de.keksuccino.konkrete.json.jsonpath.internal;

import de.keksuccino.konkrete.json.jsonpath.InvalidPathException;

public class CharacterIndex {

    private static final char OPEN_PARENTHESIS = '(';

    private static final char CLOSE_PARENTHESIS = ')';

    private static final char CLOSE_SQUARE_BRACKET = ']';

    private static final char SPACE = ' ';

    private static final char ESCAPE = '\\';

    private static final char SINGLE_QUOTE = '\'';

    private static final char DOUBLE_QUOTE = '"';

    private static final char MINUS = '-';

    private static final char PERIOD = '.';

    private static final char REGEX = '/';

    private static final char SCI_E = 'E';

    private static final char SCI_e = 'e';

    private final CharSequence charSequence;

    private int position;

    private int endPosition;

    public CharacterIndex(CharSequence charSequence) {
        this.charSequence = charSequence;
        this.position = 0;
        this.endPosition = charSequence.length() - 1;
    }

    public int length() {
        return this.endPosition + 1;
    }

    public char charAt(int idx) {
        return this.charSequence.charAt(idx);
    }

    public char currentChar() {
        return this.charSequence.charAt(this.position);
    }

    public boolean currentCharIs(char c) {
        return this.charSequence.charAt(this.position) == c;
    }

    public boolean lastCharIs(char c) {
        return this.charSequence.charAt(this.endPosition) == c;
    }

    public boolean nextCharIs(char c) {
        return this.inBounds(this.position + 1) && this.charSequence.charAt(this.position + 1) == c;
    }

    public int incrementPosition(int charCount) {
        return this.setPosition(this.position + charCount);
    }

    public int decrementEndPosition(int charCount) {
        return this.setEndPosition(this.endPosition - charCount);
    }

    public int setPosition(int newPosition) {
        this.position = newPosition;
        return this.position;
    }

    private int setEndPosition(int newPosition) {
        this.endPosition = newPosition;
        return this.endPosition;
    }

    public int position() {
        return this.position;
    }

    public int indexOfClosingSquareBracket(int startPosition) {
        for (int readPosition = startPosition; this.inBounds(readPosition); readPosition++) {
            if (this.charAt(readPosition) == ']') {
                return readPosition;
            }
        }
        return -1;
    }

    public int indexOfMatchingCloseChar(int startPosition, char openChar, char closeChar, boolean skipStrings, boolean skipRegex) {
        if (this.charAt(startPosition) != openChar) {
            throw new InvalidPathException("Expected " + openChar + " but found " + this.charAt(startPosition));
        } else {
            int opened = 1;
            for (int readPosition = startPosition + 1; this.inBounds(readPosition); readPosition++) {
                if (skipStrings) {
                    char quoteChar = this.charAt(readPosition);
                    if (quoteChar == '\'' || quoteChar == '"') {
                        readPosition = this.nextIndexOfUnescaped(readPosition, quoteChar);
                        if (readPosition == -1) {
                            throw new InvalidPathException("Could not find matching close quote for " + quoteChar + " when parsing : " + this.charSequence);
                        }
                        readPosition++;
                    }
                }
                if (skipRegex && this.charAt(readPosition) == '/') {
                    readPosition = this.nextIndexOfUnescaped(readPosition, '/');
                    if (readPosition == -1) {
                        throw new InvalidPathException("Could not find matching close for / when parsing regex in : " + this.charSequence);
                    }
                    readPosition++;
                }
                if (this.charAt(readPosition) == openChar) {
                    opened++;
                }
                if (this.charAt(readPosition) == closeChar) {
                    if (--opened == 0) {
                        return readPosition;
                    }
                }
            }
            return -1;
        }
    }

    public int indexOfClosingBracket(int startPosition, boolean skipStrings, boolean skipRegex) {
        return this.indexOfMatchingCloseChar(startPosition, (char) 40, (char) 41, skipStrings, skipRegex);
    }

    public int indexOfNextSignificantChar(char c) {
        return this.indexOfNextSignificantChar(this.position, c);
    }

    public int indexOfNextSignificantChar(int startPosition, char c) {
        int readPosition = startPosition + 1;
        while (!this.isOutOfBounds(readPosition) && this.charAt(readPosition) == ' ') {
            readPosition++;
        }
        return this.charAt(readPosition) == c ? readPosition : -1;
    }

    public int nextIndexOf(char c) {
        return this.nextIndexOf(this.position + 1, c);
    }

    public int nextIndexOf(int startPosition, char c) {
        for (int readPosition = startPosition; !this.isOutOfBounds(readPosition); readPosition++) {
            if (this.charAt(readPosition) == c) {
                return readPosition;
            }
        }
        return -1;
    }

    public int nextIndexOfUnescaped(char c) {
        return this.nextIndexOfUnescaped(this.position, c);
    }

    public int nextIndexOfUnescaped(int startPosition, char c) {
        int readPosition = startPosition + 1;
        for (boolean inEscape = false; !this.isOutOfBounds(readPosition); readPosition++) {
            if (inEscape) {
                inEscape = false;
            } else if ('\\' == this.charAt(readPosition)) {
                inEscape = true;
            } else if (c == this.charAt(readPosition)) {
                return readPosition;
            }
        }
        return -1;
    }

    public char charAtOr(int postition, char defaultChar) {
        return !this.inBounds(postition) ? defaultChar : this.charAt(postition);
    }

    public boolean nextSignificantCharIs(int startPosition, char c) {
        int readPosition = startPosition + 1;
        while (!this.isOutOfBounds(readPosition) && this.charAt(readPosition) == ' ') {
            readPosition++;
        }
        return !this.isOutOfBounds(readPosition) && this.charAt(readPosition) == c;
    }

    public boolean nextSignificantCharIs(char c) {
        return this.nextSignificantCharIs(this.position, c);
    }

    public char nextSignificantChar() {
        return this.nextSignificantChar(this.position);
    }

    public char nextSignificantChar(int startPosition) {
        int readPosition = startPosition + 1;
        while (!this.isOutOfBounds(readPosition) && this.charAt(readPosition) == ' ') {
            readPosition++;
        }
        return !this.isOutOfBounds(readPosition) ? this.charAt(readPosition) : ' ';
    }

    public void readSignificantChar(char c) {
        if (this.skipBlanks().currentChar() != c) {
            throw new InvalidPathException(String.format("Expected character: %c", c));
        } else {
            this.incrementPosition(1);
        }
    }

    public boolean hasSignificantSubSequence(CharSequence s) {
        this.skipBlanks();
        if (!this.inBounds(this.position + s.length() - 1)) {
            return false;
        } else if (!this.subSequence(this.position, this.position + s.length()).equals(s)) {
            return false;
        } else {
            this.incrementPosition(s.length());
            return true;
        }
    }

    public int indexOfPreviousSignificantChar(int startPosition) {
        int readPosition = startPosition - 1;
        while (!this.isOutOfBounds(readPosition) && this.charAt(readPosition) == ' ') {
            readPosition--;
        }
        return !this.isOutOfBounds(readPosition) ? readPosition : -1;
    }

    public int indexOfPreviousSignificantChar() {
        return this.indexOfPreviousSignificantChar(this.position);
    }

    public char previousSignificantChar(int startPosition) {
        int previousSignificantCharIndex = this.indexOfPreviousSignificantChar(startPosition);
        return previousSignificantCharIndex == -1 ? ' ' : this.charAt(previousSignificantCharIndex);
    }

    public char previousSignificantChar() {
        return this.previousSignificantChar(this.position);
    }

    public boolean currentIsTail() {
        return this.position >= this.endPosition;
    }

    public boolean hasMoreCharacters() {
        return this.inBounds(this.position + 1);
    }

    public boolean inBounds(int idx) {
        return idx >= 0 && idx <= this.endPosition;
    }

    public boolean inBounds() {
        return this.inBounds(this.position);
    }

    public boolean isOutOfBounds(int idx) {
        return !this.inBounds(idx);
    }

    public CharSequence subSequence(int start, int end) {
        return this.charSequence.subSequence(start, end);
    }

    public CharSequence charSequence() {
        return this.charSequence;
    }

    public String toString() {
        return this.charSequence.toString();
    }

    public boolean isNumberCharacter(int readPosition) {
        char c = this.charAt(readPosition);
        return Character.isDigit(c) || c == '-' || c == '.' || c == 'E' || c == 'e';
    }

    public CharacterIndex skipBlanks() {
        while (this.inBounds() && this.position < this.endPosition && this.currentChar() == ' ') {
            this.incrementPosition(1);
        }
        return this;
    }

    private CharacterIndex skipBlanksAtEnd() {
        while (this.inBounds() && this.position < this.endPosition && this.lastCharIs(' ')) {
            this.decrementEndPosition(1);
        }
        return this;
    }

    public CharacterIndex trim() {
        this.skipBlanks();
        this.skipBlanksAtEnd();
        return this;
    }
}