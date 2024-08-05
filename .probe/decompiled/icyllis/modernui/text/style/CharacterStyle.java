package icyllis.modernui.text.style;

import icyllis.modernui.text.TextPaint;
import javax.annotation.Nonnull;

public abstract class CharacterStyle {

    public abstract void updateDrawState(@Nonnull TextPaint var1);

    @Nonnull
    public static CharacterStyle wrap(CharacterStyle cs) {
        cs = cs.getUnderlying();
        return (CharacterStyle) (cs instanceof MetricAffectingSpan ? new MetricAffectingSpan.Passthrough((MetricAffectingSpan) cs) : new CharacterStyle.Passthrough(cs));
    }

    public CharacterStyle getUnderlying() {
        return this;
    }

    private static class Passthrough extends CharacterStyle {

        private final CharacterStyle mStyle;

        private Passthrough(CharacterStyle cs) {
            this.mStyle = cs;
        }

        @Override
        public void updateDrawState(@Nonnull TextPaint paint) {
            this.mStyle.updateDrawState(paint);
        }

        @Override
        public CharacterStyle getUnderlying() {
            return this.mStyle.getUnderlying();
        }
    }
}