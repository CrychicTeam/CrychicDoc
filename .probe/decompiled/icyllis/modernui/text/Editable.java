package icyllis.modernui.text;

import javax.annotation.Nonnull;

public interface Editable extends Spannable, GetChars, Appendable {

    Editable.Factory DEFAULT_FACTORY = SpannableStringBuilder::new;

    Editable replace(int var1, int var2, @Nonnull CharSequence var3, int var4, int var5);

    Editable replace(int var1, int var2, @Nonnull CharSequence var3);

    Editable insert(int var1, @Nonnull CharSequence var2, int var3, int var4);

    Editable insert(int var1, @Nonnull CharSequence var2);

    Editable delete(int var1, int var2);

    Editable append(@Nonnull CharSequence var1);

    Editable append(@Nonnull CharSequence var1, int var2, int var3);

    Editable append(char var1);

    void clear();

    void clearSpans();

    void setFilters(@Nonnull InputFilter[] var1);

    @Nonnull
    InputFilter[] getFilters();

    @FunctionalInterface
    public interface Factory {

        @Nonnull
        Editable newEditable(@Nonnull CharSequence var1);
    }
}