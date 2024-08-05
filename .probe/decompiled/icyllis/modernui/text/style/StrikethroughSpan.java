package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.Parcel;

public class StrikethroughSpan extends CharacterStyle implements UpdateAppearance, ParcelableSpan {

    public StrikethroughSpan() {
    }

    public StrikethroughSpan(@NonNull Parcel src) {
    }

    @Override
    public int getSpanTypeId() {
        return 5;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }

    @Override
    public void updateDrawState(@NonNull TextPaint paint) {
        paint.setStrikethrough(true);
    }
}