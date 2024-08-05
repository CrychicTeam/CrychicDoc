package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.util.Parcel;

public interface TrailingMarginSpan extends ParagraphStyle {

    int getTrailingMargin();

    public static class Standard implements TrailingMarginSpan, ParcelableSpan {

        private final int mTrailing;

        public Standard(int trailing) {
            this.mTrailing = trailing;
        }

        public Standard(@NonNull Parcel src) {
            this.mTrailing = src.readInt();
        }

        @Override
        public int getSpanTypeId() {
            return 30;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(this.mTrailing);
        }

        @Override
        public int getTrailingMargin() {
            return this.mTrailing;
        }
    }
}