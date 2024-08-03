package icyllis.modernui.text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface InputFilter {

    @Nullable
    CharSequence filter(@Nonnull CharSequence var1, int var2, int var3, @Nonnull Spanned var4, int var5, int var6);

    public static class LengthFilter implements InputFilter {

        private final int mMax;

        public LengthFilter(int max) {
            this.mMax = max;
        }

        @Nullable
        @Override
        public CharSequence filter(@Nonnull CharSequence source, int start, int end, @Nonnull Spanned dest, int dstart, int dend) {
            int keep = this.mMax - (dest.length() - (dend - dstart));
            if (keep <= 0) {
                return "";
            } else if (keep >= end - start) {
                return null;
            } else {
                keep += start;
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    if (--keep == start) {
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
        }

        public int getMax() {
            return this.mMax;
        }
    }
}