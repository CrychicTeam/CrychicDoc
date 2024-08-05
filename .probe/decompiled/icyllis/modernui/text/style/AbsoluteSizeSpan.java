package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.Parcel;

public class AbsoluteSizeSpan extends MetricAffectingSpan implements ParcelableSpan {

    private final int mSize;

    private final boolean mScaled;

    public AbsoluteSizeSpan(int size) {
        this(size, false);
    }

    public AbsoluteSizeSpan(int size, boolean scaled) {
        this.mSize = size;
        this.mScaled = scaled;
    }

    public AbsoluteSizeSpan(@NonNull Parcel src) {
        this.mSize = src.readInt();
        this.mScaled = src.readBoolean();
    }

    @Override
    public int getSpanTypeId() {
        return 16;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.mSize);
        dest.writeBoolean(this.mScaled);
    }

    public int getSize() {
        return this.mSize;
    }

    public boolean isScaled() {
        return this.mScaled;
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        if (this.mScaled) {
            paint.setTextSize((float) this.mSize * paint.density);
        } else {
            paint.setTextSize((float) this.mSize);
        }
    }
}