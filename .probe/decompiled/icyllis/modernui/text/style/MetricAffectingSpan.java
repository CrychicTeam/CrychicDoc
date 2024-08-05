package icyllis.modernui.text.style;

import icyllis.modernui.text.TextPaint;
import javax.annotation.Nonnull;

public abstract class MetricAffectingSpan extends CharacterStyle implements UpdateLayout {

    @Override
    public void updateDrawState(@Nonnull TextPaint paint) {
        this.updateMeasureState(paint);
    }

    public abstract void updateMeasureState(@Nonnull TextPaint var1);

    public MetricAffectingSpan getUnderlying() {
        return this;
    }

    static class Passthrough extends MetricAffectingSpan {

        private final MetricAffectingSpan mStyle;

        Passthrough(@Nonnull MetricAffectingSpan cs) {
            this.mStyle = cs;
        }

        @Override
        public void updateMeasureState(@Nonnull TextPaint paint) {
            this.mStyle.updateMeasureState(paint);
        }

        @Override
        public MetricAffectingSpan getUnderlying() {
            return this.mStyle.getUnderlying();
        }
    }
}