package icyllis.modernui.text.style;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.text.TextPaint;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ReplacementSpan extends MetricAffectingSpan {

    @Override
    public final void updateMeasureState(@Nonnull TextPaint paint) {
    }

    public abstract int getSize(@Nonnull TextPaint var1, CharSequence var2, int var3, int var4, @Nullable FontMetricsInt var5);

    public abstract void draw(@Nonnull Canvas var1, CharSequence var2, int var3, int var4, float var5, int var6, int var7, int var8, @Nonnull TextPaint var9);
}