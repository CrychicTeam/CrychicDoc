package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.text.Layout;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.Parcel;

public interface LeadingMarginSpan extends ParagraphStyle {

    int getLeadingMargin(boolean var1);

    void drawLeadingMargin(Canvas var1, TextPaint var2, int var3, int var4, int var5, int var6, int var7, CharSequence var8, int var9, int var10, boolean var11, Layout var12);

    default void drawMargin(Canvas c, TextPaint p, int left, int right, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        int x = dir == -1 ? right : left;
        this.drawLeadingMargin(c, p, x, dir, top, baseline, bottom, text, start, end, first, layout);
    }

    public interface LeadingMarginSpan2 extends LeadingMarginSpan, WrapTogetherSpan {

        int getLeadingMarginLineCount();
    }

    public static class Standard implements LeadingMarginSpan, ParcelableSpan {

        private final int mFirst;

        private final int mRest;

        public Standard(int first, int rest) {
            this.mFirst = first;
            this.mRest = rest;
        }

        public Standard(int every) {
            this(every, every);
        }

        public Standard(@NonNull Parcel src) {
            this.mFirst = src.readInt();
            this.mRest = src.readInt();
        }

        @Override
        public int getSpanTypeId() {
            return 10;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(this.mFirst);
            dest.writeInt(this.mRest);
        }

        @Override
        public int getLeadingMargin(boolean first) {
            return first ? this.mFirst : this.mRest;
        }

        @Override
        public void drawLeadingMargin(Canvas c, TextPaint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        }
    }
}