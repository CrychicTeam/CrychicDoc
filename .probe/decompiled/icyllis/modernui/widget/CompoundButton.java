package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.util.ColorStateList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class CompoundButton extends Button implements Checkable {

    public static final int[] CHECKED_STATE_SET = new int[] { 16842912 };

    private boolean mChecked;

    private boolean mBroadcasting;

    private Drawable mButtonDrawable;

    private ColorStateList mButtonTintList;

    private boolean mHasButtonTint;

    private Checkable.OnCheckedChangeListener mOnCheckedChangeListener;

    public CompoundButton(Context context) {
        super(context);
        this.setGravity(8388627);
    }

    @Override
    public void toggle() {
        this.setChecked(!this.mChecked);
    }

    @Override
    public boolean performClick() {
        this.toggle();
        boolean handled = super.performClick();
        if (!handled) {
            this.playSoundEffect(0);
        }
        return handled;
    }

    @Override
    public final boolean isChecked() {
        return this.mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.mChecked != checked) {
            this.mChecked = checked;
            this.refreshDrawableState();
            if (this.mBroadcasting) {
                return;
            }
            this.mBroadcasting = true;
            if (this.mOnCheckedChangeListener != null) {
                this.mOnCheckedChangeListener.onCheckedChanged(this, this.mChecked);
            }
            this.mBroadcasting = false;
        }
    }

    public void setOnCheckedChangeListener(@Nullable Checkable.OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }

    public void setButtonDrawable(@Nullable Drawable drawable) {
        if (this.mButtonDrawable != drawable) {
            if (this.mButtonDrawable != null) {
                this.mButtonDrawable.setCallback(null);
                this.unscheduleDrawable(this.mButtonDrawable);
            }
            this.mButtonDrawable = drawable;
            if (drawable != null) {
                drawable.setCallback(this);
                drawable.setLayoutDirection(this.getLayoutDirection());
                if (drawable.isStateful()) {
                    drawable.setState(this.getDrawableState());
                }
                drawable.setVisible(this.getVisibility() == 0, false);
                this.setMinHeight(drawable.getIntrinsicHeight());
                this.applyButtonTint();
            }
        }
    }

    @Nullable
    public Drawable getButtonDrawable() {
        return this.mButtonDrawable;
    }

    public void setButtonTintList(@Nullable ColorStateList tint) {
        this.mButtonTintList = tint;
        this.mHasButtonTint = true;
        this.applyButtonTint();
    }

    @Nullable
    public final ColorStateList getButtonTintList() {
        return this.mButtonTintList;
    }

    private void applyButtonTint() {
        if (this.mButtonDrawable != null && this.mHasButtonTint) {
            this.mButtonDrawable = this.mButtonDrawable.mutate();
            if (this.mHasButtonTint) {
                this.mButtonDrawable.setTintList(this.mButtonTintList);
            }
            if (this.mButtonDrawable.isStateful()) {
                this.mButtonDrawable.setState(this.getDrawableState());
            }
        }
    }

    @Override
    public int getCompoundPaddingLeft() {
        int padding = super.getCompoundPaddingLeft();
        if (!this.isLayoutRtl()) {
            Drawable buttonDrawable = this.mButtonDrawable;
            if (buttonDrawable != null) {
                padding += buttonDrawable.getIntrinsicWidth();
            }
        }
        return padding;
    }

    @Override
    public int getCompoundPaddingRight() {
        int padding = super.getCompoundPaddingRight();
        if (this.isLayoutRtl()) {
            Drawable buttonDrawable = this.mButtonDrawable;
            if (buttonDrawable != null) {
                padding += buttonDrawable.getIntrinsicWidth();
            }
        }
        return padding;
    }

    @Override
    public int getHorizontalOffsetForDrawables() {
        Drawable buttonDrawable = this.mButtonDrawable;
        return buttonDrawable != null ? buttonDrawable.getIntrinsicWidth() : 0;
    }

    @Override
    protected void onDraw(@Nonnull Canvas canvas) {
        Drawable buttonDrawable = this.mButtonDrawable;
        if (buttonDrawable != null) {
            int verticalGravity = this.getGravity() & 112;
            int drawableHeight = buttonDrawable.getIntrinsicHeight();
            int drawableWidth = buttonDrawable.getIntrinsicWidth();
            int top = switch(verticalGravity) {
                case 16 ->
                    (this.getHeight() - drawableHeight) / 2;
                case 80 ->
                    this.getHeight() - drawableHeight;
                default ->
                    0;
            };
            int bottom = top + drawableHeight;
            int left = this.isLayoutRtl() ? this.getWidth() - drawableWidth : 0;
            int right = this.isLayoutRtl() ? this.getWidth() : drawableWidth;
            buttonDrawable.setBounds(left, top, right, bottom);
            Drawable background = this.getBackground();
            if (background != null) {
                background.setHotspotBounds(left, top, right, bottom);
            }
        }
        super.onDraw(canvas);
        if (buttonDrawable != null) {
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            if (scrollX == 0 && scrollY == 0) {
                buttonDrawable.draw(canvas);
            } else {
                canvas.translate((float) scrollX, (float) scrollY);
                buttonDrawable.draw(canvas);
                canvas.translate((float) (-scrollX), (float) (-scrollY));
            }
        }
    }

    @Nonnull
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (this.isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable buttonDrawable = this.mButtonDrawable;
        if (buttonDrawable != null && buttonDrawable.isStateful() && buttonDrawable.setState(this.getDrawableState())) {
            this.invalidateDrawable(buttonDrawable);
        }
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mButtonDrawable != null) {
            this.mButtonDrawable.setHotspot(x, y);
        }
    }

    @Override
    public void onResolveDrawables(int layoutDirection) {
        super.onResolveDrawables(layoutDirection);
        if (this.mButtonDrawable != null) {
            this.mButtonDrawable.setLayoutDirection(layoutDirection);
        }
    }

    @Override
    protected boolean verifyDrawable(@Nonnull Drawable who) {
        return super.verifyDrawable(who) || who == this.mButtonDrawable;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mButtonDrawable != null) {
            this.mButtonDrawable.jumpToCurrentState();
        }
    }
}