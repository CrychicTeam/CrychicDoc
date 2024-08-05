package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.Parcel;

public class StyleSpan extends MetricAffectingSpan implements ParcelableSpan {

    private final int mStyle;

    public StyleSpan(int style) {
        this.mStyle = style;
    }

    public StyleSpan(@NonNull Parcel src) {
        this.mStyle = src.readInt();
    }

    @Override
    public int getSpanTypeId() {
        return 7;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.mStyle);
    }

    public int getStyle() {
        return this.mStyle;
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        paint.setTextStyle(this.mStyle);
    }
}