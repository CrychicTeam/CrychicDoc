package icyllis.arc3d.core.effects;

import icyllis.arc3d.core.ColorFilter;
import java.util.Objects;

public class ComposedColorFilter extends ColorFilter {

    private final ColorFilter mAfter;

    private final ColorFilter mBefore;

    public ComposedColorFilter(ColorFilter before, ColorFilter after) {
        this.mBefore = (ColorFilter) Objects.requireNonNull(before);
        this.mAfter = (ColorFilter) Objects.requireNonNull(after);
    }

    public ColorFilter getBefore() {
        return this.mBefore;
    }

    public ColorFilter getAfter() {
        return this.mAfter;
    }

    @Override
    public boolean isAlphaUnchanged() {
        return this.mAfter.isAlphaUnchanged() && this.mBefore.isAlphaUnchanged();
    }

    @Override
    public void filterColor4f(float[] col, float[] out) {
        this.mBefore.filterColor4f(col, out);
        this.mAfter.filterColor4f(out, out);
    }
}