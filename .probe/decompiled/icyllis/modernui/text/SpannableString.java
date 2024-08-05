package icyllis.modernui.text;

import icyllis.modernui.annotation.NonNull;

public class SpannableString extends SpannableStringInternal implements Spannable, GetChars {

    public SpannableString(@NonNull CharSequence source, boolean ignoreNoCopySpan) {
        super(source, 0, source.length(), ignoreNoCopySpan);
    }

    public SpannableString(@NonNull CharSequence source, int start, int end, boolean ignoreNoCopySpan) {
        super(source, start, end, ignoreNoCopySpan);
    }

    public SpannableString(@NonNull CharSequence source) {
        this(source, false);
    }

    public SpannableString(@NonNull CharSequence source, int start, int end) {
        super(source, start, end, false);
    }

    @NonNull
    public static SpannableString valueOf(@NonNull CharSequence source) {
        return source instanceof SpannableString ? (SpannableString) source : new SpannableString(source);
    }

    @NonNull
    public CharSequence subSequence(int start, int end) {
        return new SpannableString(this, start, end);
    }
}