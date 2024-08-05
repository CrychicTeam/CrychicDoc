package icyllis.modernui.text.method;

import icyllis.modernui.text.InputFilter;
import icyllis.modernui.text.SpannableStringBuilder;
import icyllis.modernui.text.Spanned;
import javax.annotation.Nonnull;

public abstract class NumberInputFilter implements InputFilter {

    @Nonnull
    protected abstract char[] getAcceptedChars();

    @Override
    public CharSequence filter(@Nonnull CharSequence source, int start, int end, @Nonnull Spanned dest, int dstart, int dend) {
        char[] accept = this.getAcceptedChars();
        int i = start;
        while (i < end && !not(accept, source.charAt(i))) {
            i++;
        }
        if (i == end) {
            return null;
        } else if (end - start == 1) {
            return "";
        } else {
            SpannableStringBuilder filtered = new SpannableStringBuilder(source, start, end);
            i -= start;
            end -= start;
            for (int j = end - 1; j >= i; j--) {
                if (not(accept, source.charAt(j))) {
                    filtered.delete(j, j + 1);
                }
            }
            return filtered;
        }
    }

    protected static boolean not(@Nonnull char[] accept, char c) {
        for (int i = accept.length - 1; i >= 0; i--) {
            if (accept[i] == c) {
                return false;
            }
        }
        return true;
    }
}