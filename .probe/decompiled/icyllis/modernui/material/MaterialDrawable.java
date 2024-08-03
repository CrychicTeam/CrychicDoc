package icyllis.modernui.material;

import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.util.ColorStateList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MaterialDrawable extends Drawable {

    protected ColorStateList mTint;

    protected int mColor = -1;

    protected int mAlpha = 255;

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        if (this.mTint != tint) {
            this.mTint = tint;
            if (tint != null) {
                this.mColor = tint.getColorForState(this.getState(), -1);
            } else {
                this.mColor = -1;
            }
            this.invalidateSelf();
        }
    }

    @Override
    protected boolean onStateChange(@Nonnull int[] stateSet) {
        if (this.mTint != null) {
            this.mColor = this.mTint.getColorForState(stateSet, -1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isStateful() {
        return super.isStateful() || this.mTint != null && this.mTint.isStateful();
    }

    @Override
    public boolean hasFocusStateSpecified() {
        return this.mTint != null && this.mTint.hasFocusStateSpecified();
    }

    @Override
    public void setAlpha(int alpha) {
        if (this.mAlpha != alpha) {
            this.mAlpha = alpha;
            this.invalidateSelf();
        }
    }

    @Override
    public int getAlpha() {
        return this.mAlpha;
    }
}