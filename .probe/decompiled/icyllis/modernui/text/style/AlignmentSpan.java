package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.Layout;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.util.Parcel;

public interface AlignmentSpan extends ParagraphStyle {

    Layout.Alignment getAlignment();

    public static class Standard implements AlignmentSpan, ParcelableSpan {

        private final Layout.Alignment mAlignment;

        public Standard(@NonNull Layout.Alignment align) {
            this.mAlignment = align;
        }

        public Standard(@NonNull Parcel src) {
            this.mAlignment = Layout.Alignment.valueOf(src.readString());
        }

        @Override
        public int getSpanTypeId() {
            return 1;
        }

        @Override
        public Layout.Alignment getAlignment() {
            return this.mAlignment;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(this.mAlignment.name());
        }
    }
}