package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.Parcel;
import java.util.Locale;
import java.util.Objects;

public class LocaleSpan extends MetricAffectingSpan implements ParcelableSpan {

    @Nullable
    private final Locale mLocale;

    public LocaleSpan(@Nullable Locale locale) {
        this.mLocale = locale;
    }

    public LocaleSpan(@NonNull Parcel source) {
        String tag = source.readString8();
        if (tag != null && !tag.isEmpty()) {
            this.mLocale = Locale.forLanguageTag(tag);
        } else {
            this.mLocale = null;
        }
    }

    @Override
    public int getSpanTypeId() {
        return 23;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString8(this.mLocale != null ? this.mLocale.toLanguageTag() : "");
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        paint.setTextLocale((Locale) Objects.requireNonNull(this.mLocale, "locale cannot be null"));
    }

    @Nullable
    public Locale getLocale() {
        return this.mLocale;
    }
}