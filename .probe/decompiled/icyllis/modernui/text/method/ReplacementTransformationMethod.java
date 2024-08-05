package icyllis.modernui.text.method;

import icyllis.modernui.graphics.Rect;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.GetChars;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.text.SpannedString;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.view.View;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ReplacementTransformationMethod implements TransformationMethod {

    protected ReplacementTransformationMethod() {
    }

    protected abstract char[] getOriginal();

    protected abstract char[] getReplacement();

    @Nonnull
    @Override
    public CharSequence getTransformation(@Nonnull CharSequence source, @Nonnull View v) {
        char[] original = this.getOriginal();
        char[] replacement = this.getReplacement();
        if (!(source instanceof Editable)) {
            boolean doNothing = true;
            for (char c : original) {
                if (TextUtils.indexOf(source, c) >= 0) {
                    doNothing = false;
                    break;
                }
            }
            if (doNothing) {
                return source;
            }
            if (!(source instanceof Spannable)) {
                if (source instanceof Spanned) {
                    return new SpannedString(new ReplacementTransformationMethod.SpannedReplacementCharSequence((Spanned) source, original, replacement));
                }
                return new ReplacementTransformationMethod.ReplacementCharSequence(source, original, replacement).toString();
            }
        }
        return new ReplacementTransformationMethod.SpannedReplacementCharSequence((Spanned) source, original, replacement);
    }

    @Override
    public void onFocusChanged(@Nonnull View view, @Nonnull CharSequence sourceText, boolean focused, int direction, @Nullable Rect previouslyFocusedRect) {
    }

    private static class ReplacementCharSequence implements CharSequence, GetChars {

        private final CharSequence mSource;

        private final char[] mOriginal;

        private final char[] mReplacement;

        public ReplacementCharSequence(CharSequence source, char[] original, char[] replacement) {
            this.mSource = source;
            this.mOriginal = original;
            this.mReplacement = replacement;
        }

        public int length() {
            return this.mSource.length();
        }

        public char charAt(int i) {
            char c = this.mSource.charAt(i);
            int n = this.mOriginal.length;
            for (int j = 0; j < n; j++) {
                if (c == this.mOriginal[j]) {
                    c = this.mReplacement[j];
                }
            }
            return c;
        }

        public CharSequence subSequence(int start, int end) {
            char[] c = new char[end - start];
            this.getChars(start, end, c, 0);
            return new String(c);
        }

        public String toString() {
            char[] c = new char[this.length()];
            this.getChars(0, this.length(), c, 0);
            return new String(c);
        }

        @Override
        public void getChars(int start, int end, char[] dest, int off) {
            TextUtils.getChars(this.mSource, start, end, dest, off);
            int offend = end - start + off;
            int n = this.mOriginal.length;
            for (int i = off; i < offend; i++) {
                char c = dest[i];
                for (int j = 0; j < n; j++) {
                    if (c == this.mOriginal[j]) {
                        dest[i] = this.mReplacement[j];
                    }
                }
            }
        }
    }

    private static class SpannedReplacementCharSequence extends ReplacementTransformationMethod.ReplacementCharSequence implements Spanned {

        private final Spanned mSpanned;

        public SpannedReplacementCharSequence(Spanned source, char[] original, char[] replacement) {
            super(source, original, replacement);
            this.mSpanned = source;
        }

        @Nonnull
        @Override
        public CharSequence subSequence(int start, int end) {
            return new SpannedString(this).subSequence(start, end);
        }

        @Nonnull
        @Override
        public <T> List<T> getSpans(int start, int end, Class<? extends T> type, @Nullable List<T> out) {
            return this.mSpanned.getSpans(start, end, type, out);
        }

        @Override
        public int getSpanStart(@Nonnull Object tag) {
            return this.mSpanned.getSpanStart(tag);
        }

        @Override
        public int getSpanEnd(@Nonnull Object tag) {
            return this.mSpanned.getSpanEnd(tag);
        }

        @Override
        public int getSpanFlags(@Nonnull Object tag) {
            return this.mSpanned.getSpanFlags(tag);
        }

        @Override
        public int nextSpanTransition(int start, int end, Class<?> type) {
            return this.mSpanned.nextSpanTransition(start, end, type);
        }
    }
}