package icyllis.arc3d.core;

import icyllis.arc3d.core.effects.ComposedColorFilter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ColorFilter {

    protected ColorFilter() {
    }

    public boolean isAlphaUnchanged() {
        return false;
    }

    @ColorInt
    public int filterColor(@ColorInt int col) {
        float[] dst4 = Color.load_and_premul(col);
        this.filterColor4f(dst4, dst4);
        float a = MathUtil.clamp(dst4[3], 0.0F, 1.0F);
        if (a == 0.0F) {
            return 0;
        } else {
            int result = (int) (a * 255.0F + 0.5F) << 24;
            a = 255.0F / a;
            for (int i = 0; i < 3; i++) {
                result |= (int) MathUtil.clamp(dst4[2 - i] * a + 0.5F, 0.0F, 255.0F) << (i << 3);
            }
            return result;
        }
    }

    public abstract void filterColor4f(@Size(4L) float[] var1, @Size(4L) float[] var2);

    @Nonnull
    public ColorFilter compose(@Nullable ColorFilter before) {
        return (ColorFilter) (before == null ? this : new ComposedColorFilter(before, this));
    }

    @Nonnull
    public ColorFilter andThen(@Nullable ColorFilter after) {
        return (ColorFilter) (after == null ? this : new ComposedColorFilter(this, after));
    }
}