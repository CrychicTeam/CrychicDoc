package icyllis.modernui.graphics.text;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.GetChars;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import java.util.Objects;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class CharSequenceBuilder extends CharArrayList implements CharSequence, GetChars {

    public void addChars(@NonNull char[] buf, int start, int end) {
        Objects.checkFromToIndex(start, end, buf.length);
        int offset = this.size;
        int length = end - start;
        this.size(offset + length);
        System.arraycopy(buf, start, this.a, offset, length);
    }

    public int addCodePoint(int codePoint) {
        if (Character.isBmpCodePoint(codePoint)) {
            this.add((char) codePoint);
            return 1;
        } else {
            this.add(Character.highSurrogate(codePoint));
            this.add(Character.lowSurrogate(codePoint));
            return 2;
        }
    }

    public void addString(@NonNull String s) {
        int offset = this.size;
        int length = s.length();
        this.size(offset + length);
        s.getChars(0, length, this.a, offset);
    }

    public void addCharSequence(@NonNull CharSequence s) {
        int offset = this.size;
        int length = s.length();
        this.size(offset + length);
        char[] buf = this.a;
        for (int i = 0; i < length; i++) {
            buf[offset + i] = s.charAt(i);
        }
    }

    @NonNull
    public CharSequenceBuilder updateChars(@NonNull char[] buf, int start, int end) {
        Objects.checkFromToIndex(start, end, buf.length);
        int length = end - start;
        this.size(length);
        System.arraycopy(buf, start, this.a, 0, length);
        return this;
    }

    @Override
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        Objects.checkFromToIndex(srcBegin, srcEnd, this.size);
        System.arraycopy(this.a, srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }

    public int length() {
        return this.size;
    }

    public char charAt(int index) {
        return this.getChar(index);
    }

    @NonNull
    public CharSequence subSequence(int start, int end) {
        Objects.checkFromToIndex(start, end, this.size);
        return new String(this.a, start, end - start);
    }

    @NonNull
    public String toString() {
        return new String(this.a, 0, this.size);
    }

    public int hashCode() {
        char[] buf = this.a;
        int h = 0;
        int s = this.size();
        for (int i = 0; i < s; i++) {
            h = 31 * h + buf[i];
        }
        return h;
    }

    public boolean equals(Object o) {
        if (o instanceof CharSequence csq) {
            int s = this.size();
            if (s != csq.length()) {
                return false;
            } else {
                char[] buf = this.a;
                for (int i = 0; i < s; i++) {
                    if (buf[i] != csq.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    static {
        int[] codePoints = new int[] { 128105, 127996, 8205, 10084, 65039, 8205, 128139, 8205, 128105, 127997 };
        CharSequenceBuilder builder = new CharSequenceBuilder();
        for (int cp : codePoints) {
            builder.addCodePoint(cp);
        }
        String string = new String(codePoints, 0, codePoints.length);
        if (builder.hashCode() != string.hashCode() || builder.hashCode() != builder.toString().hashCode()) {
            throw new AssertionError("Bad String.hashCode() implementation");
        }
    }
}