package icyllis.modernui.mc.text;

import icyllis.modernui.graphics.text.CharSequenceBuilder;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

@Deprecated
class ReorderTextHandler {

    private final CharSequenceBuilder mBuffer = new CharSequenceBuilder();

    @Nullable
    private Style mLast;

    private ReorderTextHandler.IConsumer mConsumer;

    private final FormattedCharSink mSink = new FormattedCharSink() {

        @Override
        public boolean accept(int index, @Nonnull Style style, int codePoint) {
            if (style != ReorderTextHandler.this.mLast) {
                if (!ReorderTextHandler.this.mBuffer.isEmpty() && ReorderTextHandler.this.mLast != null && ReorderTextHandler.this.mConsumer.handle(ReorderTextHandler.this.mBuffer, ReorderTextHandler.this.mLast)) {
                    ReorderTextHandler.this.mBuffer.clear();
                    ReorderTextHandler.this.mLast = style;
                    return false;
                }
                ReorderTextHandler.this.mBuffer.clear();
                ReorderTextHandler.this.mLast = style;
            }
            ReorderTextHandler.this.mBuffer.addCodePoint(codePoint);
            return true;
        }
    };

    public boolean handle(@Nonnull FormattedCharSequence sequence, ReorderTextHandler.IConsumer consumer) {
        this.mConsumer = consumer;
        return sequence.accept(this.mSink) ? this.finish() : false;
    }

    private boolean finish() {
        if (!this.mBuffer.isEmpty() && this.mLast != null && this.mConsumer.handle(this.mBuffer, this.mLast)) {
            this.mBuffer.clear();
            this.mLast = null;
            return false;
        } else {
            this.mBuffer.clear();
            this.mLast = null;
            return true;
        }
    }

    @FunctionalInterface
    public interface IConsumer {

        boolean handle(CharSequence var1, Style var2);
    }
}