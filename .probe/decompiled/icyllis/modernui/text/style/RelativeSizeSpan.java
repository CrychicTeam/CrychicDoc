package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.Parcel;

public class RelativeSizeSpan extends MetricAffectingSpan implements ParcelableSpan {

    private final float mProportion;

    public RelativeSizeSpan(float proportion) {
        this.mProportion = proportion;
    }

    public RelativeSizeSpan(@NonNull Parcel src) {
        this.mProportion = src.readFloat();
    }

    @Override
    public int getSpanTypeId() {
        return 3;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeFloat(this.mProportion);
    }

    public float getSizeChange() {
        return this.mProportion;
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        paint.setTextSize(paint.getTextSize() * this.mProportion);
    }
}