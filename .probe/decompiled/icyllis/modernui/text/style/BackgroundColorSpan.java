package icyllis.modernui.text.style;

import icyllis.modernui.annotation.ColorInt;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.Parcel;

public class BackgroundColorSpan extends CharacterStyle implements UpdateAppearance, ParcelableSpan {

    private final int mColor;

    public BackgroundColorSpan(@ColorInt int color) {
        this.mColor = color;
    }

    public BackgroundColorSpan(@NonNull Parcel src) {
        this.mColor = src.readInt();
    }

    @Override
    public int getSpanTypeId() {
        return 12;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.mColor);
    }

    @ColorInt
    public int getBackgroundColor() {
        return this.mColor;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint paint) {
        paint.bgColor = this.mColor;
    }
}