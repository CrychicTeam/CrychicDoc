package icyllis.modernui.text.style;

import icyllis.modernui.annotation.ColorInt;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.util.Parcel;

public interface LineBackgroundSpan extends ParagraphStyle {

    void drawBackground(@NonNull Canvas var1, @NonNull Paint var2, int var3, int var4, int var5, int var6, int var7, @NonNull CharSequence var8, int var9, int var10, int var11);

    public static class Standard implements LineBackgroundSpan, ParcelableSpan {

        private final int mColor;

        public Standard(@ColorInt int color) {
            this.mColor = color;
        }

        public Standard(@NonNull Parcel src) {
            this.mColor = src.readInt();
        }

        @Override
        public int getSpanTypeId() {
            return 27;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(this.mColor);
        }

        @ColorInt
        public final int getColor() {
            return this.mColor;
        }

        @Override
        public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
            int color = paint.getColor();
            paint.setColor(this.mColor);
            canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, paint);
            paint.setColor(color);
        }
    }
}