package icyllis.modernui.text;

import icyllis.modernui.annotation.NonNull;

public final class SpannedString extends SpannableStringInternal implements Spanned, GetChars {

    public SpannedString(@NonNull CharSequence source, boolean ignoreNoCopySpan) {
        super(source, 0, source.length(), ignoreNoCopySpan);
    }

    public SpannedString(@NonNull CharSequence source, int start, int end, boolean ignoreNoCopySpan) {
        super(source, start, end, ignoreNoCopySpan);
    }

    public SpannedString(@NonNull CharSequence source) {
        this(source, false);
    }

    public SpannedString(@NonNull CharSequence source, int start, int end) {
        super(source, start, end, false);
    }

    @NonNull
    public static SpannedString valueOf(@NonNull CharSequence source) {
        return source instanceof SpannedString ? (SpannedString) source : new SpannedString(source);
    }

    @NonNull
    public CharSequence subSequence(int start, int end) {
        return new SpannedString(this, start, end);
    }
}