package icyllis.modernui.text;

import javax.annotation.Nonnull;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface Spannable extends Spanned {

    Spannable.Factory DEFAULT_FACTORY = SpannableString::new;

    void setSpan(@Nonnull Object var1, int var2, int var3, int var4);

    void removeSpan(@Nonnull Object var1);

    @Internal
    default void removeSpan(@Nonnull Object span, int flags) {
        this.removeSpan(span);
    }

    @FunctionalInterface
    public interface Factory {

        @Nonnull
        Spannable newSpannable(@Nonnull CharSequence var1);
    }
}