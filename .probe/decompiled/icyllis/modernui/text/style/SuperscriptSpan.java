package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.Parcel;

public class SuperscriptSpan extends MetricAffectingSpan implements ParcelableSpan {

    public SuperscriptSpan() {
    }

    public SuperscriptSpan(@NonNull Parcel src) {
    }

    @Override
    public int getSpanTypeId() {
        return 14;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint textPaint) {
        textPaint.setTextSize(textPaint.getTextSize() * 2.0F / 3.0F);
        this.applyBaselineShift(textPaint);
    }

    protected void applyBaselineShift(@NonNull TextPaint textPaint) {
        textPaint.baselineShift = textPaint.baselineShift - (int) (textPaint.getTextSize() * 0.375F);
    }

    public String toString() {
        return "SuperscriptSpan{}";
    }
}