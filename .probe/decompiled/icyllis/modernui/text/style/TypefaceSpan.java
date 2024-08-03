package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.util.Parcel;

public class TypefaceSpan extends MetricAffectingSpan implements ParcelableSpan {

    @Nullable
    private final String mFamily;

    @Nullable
    private final Typeface mTypeface;

    public TypefaceSpan(@Nullable String family) {
        this(family, null);
    }

    public TypefaceSpan(@NonNull Typeface typeface) {
        this(null, typeface);
    }

    public TypefaceSpan(@NonNull Parcel src) {
        this.mFamily = src.readString();
        this.mTypeface = null;
    }

    private TypefaceSpan(@Nullable String family, @Nullable Typeface typeface) {
        this.mFamily = family;
        this.mTypeface = typeface;
    }

    @Override
    public int getSpanTypeId() {
        return 13;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.mFamily);
    }

    @Nullable
    public String getFamily() {
        return this.mFamily;
    }

    @Nullable
    public Typeface getTypeface() {
        return this.mTypeface;
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        if (this.mTypeface != null) {
            paint.setTypeface(this.mTypeface);
        } else if (this.mFamily != null) {
            Typeface typeface = Typeface.getSystemFont(this.mFamily);
            paint.setTypeface(typeface);
        }
    }
}