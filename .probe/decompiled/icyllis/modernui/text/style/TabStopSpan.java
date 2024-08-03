package icyllis.modernui.text.style;

public interface TabStopSpan extends ParagraphStyle {

    int getTabStop();

    public static class Standard implements TabStopSpan {

        private final int mTabOffset;

        public Standard(int offset) {
            this.mTabOffset = offset;
        }

        @Override
        public int getTabStop() {
            return this.mTabOffset;
        }
    }
}