package icyllis.modernui.graphics.drawable;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.SparseArray;

public class DrawableContainer extends Drawable implements Drawable.Callback {

    private DrawableContainer.DrawableContainerState mDrawableContainerState;

    private Rect mHotspotBounds;

    private Drawable mCurrDrawable;

    private Drawable mLastDrawable;

    private int mAlpha = 255;

    private boolean mHasAlpha;

    private int mCurIndex = -1;

    private boolean mMutated;

    private Runnable mAnimationRunnable;

    private long mEnterAnimationEnd;

    private long mExitAnimationEnd;

    private DrawableContainer.BlockInvalidateCallback mBlockInvalidateCallback;

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.draw(canvas);
        }
        if (this.mLastDrawable != null) {
            this.mLastDrawable.draw(canvas);
        }
    }

    private boolean needsMirroring() {
        return this.isAutoMirrored() && this.getLayoutDirection() == 1;
    }

    @Override
    public boolean getPadding(@NonNull Rect padding) {
        Rect r = this.mDrawableContainerState.getConstantPadding();
        boolean result;
        if (r != null) {
            padding.set(r);
            result = (r.left | r.top | r.bottom | r.right) != 0;
        } else if (this.mCurrDrawable != null) {
            result = this.mCurrDrawable.getPadding(padding);
        } else {
            result = super.getPadding(padding);
        }
        if (this.needsMirroring()) {
            int left = padding.left;
            padding.left = padding.right;
            padding.right = left;
        }
        return result;
    }

    @Override
    public void setAlpha(int alpha) {
        if (!this.mHasAlpha || this.mAlpha != alpha) {
            this.mHasAlpha = true;
            this.mAlpha = alpha;
            if (this.mCurrDrawable != null) {
                if (this.mEnterAnimationEnd == 0L) {
                    this.mCurrDrawable.setAlpha(alpha);
                } else {
                    this.animate(false);
                }
            }
        }
    }

    @Override
    public int getAlpha() {
        return this.mAlpha;
    }

    @Override
    public void setTintList(ColorStateList tint) {
        this.mDrawableContainerState.mHasTintList = true;
        if (this.mDrawableContainerState.mTintList != tint) {
            this.mDrawableContainerState.mTintList = tint;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.setTintList(tint);
            }
        }
    }

    @Override
    public void setTintBlendMode(@NonNull BlendMode blendMode) {
        this.mDrawableContainerState.mHasTintMode = true;
        if (this.mDrawableContainerState.mBlendMode != blendMode) {
            this.mDrawableContainerState.mBlendMode = blendMode;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.setTintBlendMode(blendMode);
            }
        }
    }

    public void setEnterFadeDuration(int ms) {
        this.mDrawableContainerState.mEnterFadeDuration = ms;
    }

    public void setExitFadeDuration(int ms) {
        this.mDrawableContainerState.mExitFadeDuration = ms;
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        if (this.mLastDrawable != null) {
            this.mLastDrawable.setBounds(bounds);
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setBounds(bounds);
        }
    }

    @Override
    public boolean isStateful() {
        return this.mDrawableContainerState.isStateful();
    }

    @Override
    public boolean hasFocusStateSpecified() {
        if (this.mCurrDrawable != null) {
            return this.mCurrDrawable.hasFocusStateSpecified();
        } else {
            return this.mLastDrawable != null ? this.mLastDrawable.hasFocusStateSpecified() : false;
        }
    }

    @Override
    public void setAutoMirrored(boolean mirrored) {
        if (this.mDrawableContainerState.mAutoMirrored != mirrored) {
            this.mDrawableContainerState.mAutoMirrored = mirrored;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
            }
        }
    }

    @Override
    public boolean isAutoMirrored() {
        return this.mDrawableContainerState.mAutoMirrored;
    }

    @Override
    public void jumpToCurrentState() {
        boolean changed = false;
        if (this.mLastDrawable != null) {
            this.mLastDrawable.jumpToCurrentState();
            this.mLastDrawable = null;
            changed = true;
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.jumpToCurrentState();
            if (this.mHasAlpha) {
                this.mCurrDrawable.setAlpha(this.mAlpha);
            }
        }
        if (this.mExitAnimationEnd != 0L) {
            this.mExitAnimationEnd = 0L;
            changed = true;
        }
        if (this.mEnterAnimationEnd != 0L) {
            this.mEnterAnimationEnd = 0L;
            changed = true;
        }
        if (changed) {
            this.invalidateSelf();
        }
    }

    @Override
    public void setHotspot(float x, float y) {
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setHotspot(x, y);
        }
    }

    @Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        if (this.mHotspotBounds == null) {
            this.mHotspotBounds = new Rect(left, top, right, bottom);
        } else {
            this.mHotspotBounds.set(left, top, right, bottom);
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setHotspotBounds(left, top, right, bottom);
        }
    }

    @Override
    public void getHotspotBounds(@NonNull Rect outRect) {
        if (this.mHotspotBounds != null) {
            outRect.set(this.mHotspotBounds);
        } else {
            super.getHotspotBounds(outRect);
        }
    }

    @Override
    protected boolean onStateChange(@NonNull int[] state) {
        if (this.mLastDrawable != null) {
            return this.mLastDrawable.setState(state);
        } else {
            return this.mCurrDrawable != null ? this.mCurrDrawable.setState(state) : false;
        }
    }

    @Override
    protected boolean onLevelChange(int level) {
        if (this.mLastDrawable != null) {
            return this.mLastDrawable.setLevel(level);
        } else {
            return this.mCurrDrawable != null ? this.mCurrDrawable.setLevel(level) : false;
        }
    }

    @Override
    protected boolean onLayoutDirectionChanged(int layoutDirection) {
        return this.mDrawableContainerState.setLayoutDirection(layoutDirection, this.getCurrentIndex());
    }

    @Override
    public int getIntrinsicWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantWidth();
        } else {
            return this.mCurrDrawable != null ? this.mCurrDrawable.getIntrinsicWidth() : -1;
        }
    }

    @Override
    public int getIntrinsicHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantHeight();
        } else {
            return this.mCurrDrawable != null ? this.mCurrDrawable.getIntrinsicHeight() : -1;
        }
    }

    @Override
    public int getMinimumWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumWidth();
        } else {
            return this.mCurrDrawable != null ? this.mCurrDrawable.getMinimumWidth() : 0;
        }
    }

    @Override
    public int getMinimumHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumHeight();
        } else {
            return this.mCurrDrawable != null ? this.mCurrDrawable.getMinimumHeight() : 0;
        }
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        if (this.mDrawableContainerState != null) {
            this.mDrawableContainerState.invalidateCache();
        }
        if (who == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        if (who == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        if (who == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().unscheduleDrawable(this, what);
        }
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (this.mLastDrawable != null) {
            this.mLastDrawable.setVisible(visible, restart);
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setVisible(visible, restart);
        }
        return changed;
    }

    public int getCurrentIndex() {
        return this.mCurIndex;
    }

    public boolean selectDrawable(int index) {
        if (index == this.mCurIndex) {
            return false;
        } else {
            long now = Core.timeMillis();
            if (this.mDrawableContainerState.mExitFadeDuration > 0) {
                if (this.mLastDrawable != null) {
                    this.mLastDrawable.setVisible(false, false);
                }
                if (this.mCurrDrawable != null) {
                    this.mLastDrawable = this.mCurrDrawable;
                    this.mExitAnimationEnd = now + (long) this.mDrawableContainerState.mExitFadeDuration;
                } else {
                    this.mLastDrawable = null;
                    this.mExitAnimationEnd = 0L;
                }
            } else if (this.mCurrDrawable != null) {
                this.mCurrDrawable.setVisible(false, false);
            }
            if (index >= 0 && index < this.mDrawableContainerState.mNumChildren) {
                Drawable d = this.mDrawableContainerState.getChild(index);
                this.mCurrDrawable = d;
                this.mCurIndex = index;
                if (d != null) {
                    if (this.mDrawableContainerState.mEnterFadeDuration > 0) {
                        this.mEnterAnimationEnd = now + (long) this.mDrawableContainerState.mEnterFadeDuration;
                    }
                    this.initializeDrawableForDisplay(d);
                }
            } else {
                this.mCurrDrawable = null;
                this.mCurIndex = -1;
            }
            if (this.mEnterAnimationEnd != 0L || this.mExitAnimationEnd != 0L) {
                if (this.mAnimationRunnable == null) {
                    this.mAnimationRunnable = () -> {
                        this.animate(true);
                        this.invalidateSelf();
                    };
                } else {
                    this.unscheduleSelf(this.mAnimationRunnable);
                }
                this.animate(true);
            }
            this.invalidateSelf();
            return true;
        }
    }

    private void initializeDrawableForDisplay(@NonNull Drawable d) {
        if (this.mBlockInvalidateCallback == null) {
            this.mBlockInvalidateCallback = new DrawableContainer.BlockInvalidateCallback();
        }
        d.setCallback(this.mBlockInvalidateCallback.wrap(d.getCallback()));
        try {
            if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha) {
                d.setAlpha(this.mAlpha);
            }
            if (this.mDrawableContainerState.mHasTintList) {
                d.setTintList(this.mDrawableContainerState.mTintList);
            }
            if (this.mDrawableContainerState.mHasTintMode) {
                d.setTintBlendMode(this.mDrawableContainerState.mBlendMode);
            }
            d.setVisible(this.isVisible(), true);
            d.setState(this.getState());
            d.setLevel(this.getLevel());
            d.setBounds(this.getBounds());
            d.setLayoutDirection(this.getLayoutDirection());
            d.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
            Rect hotspotBounds = this.mHotspotBounds;
            if (hotspotBounds != null) {
                d.setHotspotBounds(hotspotBounds.left, hotspotBounds.top, hotspotBounds.right, hotspotBounds.bottom);
            }
        } finally {
            d.setCallback(this.mBlockInvalidateCallback.unwrap());
        }
    }

    void animate(boolean schedule) {
        this.mHasAlpha = true;
        long now = Core.timeMillis();
        boolean animating = false;
        if (this.mCurrDrawable != null) {
            if (this.mEnterAnimationEnd != 0L) {
                if (this.mEnterAnimationEnd <= now) {
                    this.mCurrDrawable.setAlpha(this.mAlpha);
                    this.mEnterAnimationEnd = 0L;
                } else {
                    int animAlpha = (int) ((this.mEnterAnimationEnd - now) * 255L) / this.mDrawableContainerState.mEnterFadeDuration;
                    this.mCurrDrawable.setAlpha((255 - animAlpha) * this.mAlpha / 255);
                    animating = true;
                }
            }
        } else {
            this.mEnterAnimationEnd = 0L;
        }
        if (this.mLastDrawable != null) {
            if (this.mExitAnimationEnd != 0L) {
                if (this.mExitAnimationEnd <= now) {
                    this.mLastDrawable.setVisible(false, false);
                    this.mLastDrawable = null;
                    this.mExitAnimationEnd = 0L;
                } else {
                    int animAlpha = (int) ((this.mExitAnimationEnd - now) * 255L) / this.mDrawableContainerState.mExitFadeDuration;
                    this.mLastDrawable.setAlpha(animAlpha * this.mAlpha / 255);
                    animating = true;
                }
            }
        } else {
            this.mExitAnimationEnd = 0L;
        }
        if (schedule && animating) {
            this.scheduleSelf(this.mAnimationRunnable, now + 16L);
        }
    }

    @Nullable
    @Override
    public Drawable getCurrent() {
        return this.mCurrDrawable;
    }

    @Override
    public Drawable.ConstantState getConstantState() {
        return this.mDrawableContainerState.canConstantState() ? this.mDrawableContainerState : null;
    }

    @NonNull
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            DrawableContainer.DrawableContainerState clone = this.cloneConstantState();
            clone.mutate();
            this.setConstantState(clone);
            this.mMutated = true;
        }
        return this;
    }

    DrawableContainer.DrawableContainerState cloneConstantState() {
        return this.mDrawableContainerState;
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        this.mDrawableContainerState.clearMutated();
        this.mMutated = false;
    }

    protected void setConstantState(DrawableContainer.DrawableContainerState state) {
        this.mDrawableContainerState = state;
        if (this.mCurIndex >= 0) {
            this.mCurrDrawable = state.getChild(this.mCurIndex);
            if (this.mCurrDrawable != null) {
                this.initializeDrawableForDisplay(this.mCurrDrawable);
            }
        }
        this.mLastDrawable = null;
    }

    private static class BlockInvalidateCallback implements Drawable.Callback {

        private Drawable.Callback mCallback;

        public DrawableContainer.BlockInvalidateCallback wrap(Drawable.Callback callback) {
            this.mCallback = callback;
            return this;
        }

        public Drawable.Callback unwrap() {
            Drawable.Callback callback = this.mCallback;
            this.mCallback = null;
            return callback;
        }

        @Override
        public void invalidateDrawable(@NonNull Drawable who) {
        }

        @Override
        public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
            if (this.mCallback != null) {
                this.mCallback.scheduleDrawable(who, what, when);
            }
        }

        @Override
        public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
            if (this.mCallback != null) {
                this.mCallback.unscheduleDrawable(who, what);
            }
        }
    }

    protected abstract static class DrawableContainerState extends Drawable.ConstantState {

        final DrawableContainer mOwner;

        SparseArray<Drawable.ConstantState> mDrawableFutures;

        Drawable[] mDrawables;

        int mNumChildren;

        boolean mVariablePadding = false;

        boolean mCheckedPadding;

        Rect mConstantPadding;

        boolean mConstantSize = false;

        boolean mCheckedConstantSize;

        int mConstantWidth;

        int mConstantHeight;

        int mConstantMinimumWidth;

        int mConstantMinimumHeight;

        boolean mCheckedStateful;

        boolean mStateful;

        boolean mCheckedConstantState;

        boolean mCanConstantState;

        boolean mMutated;

        int mLayoutDirection;

        int mEnterFadeDuration = 0;

        int mExitFadeDuration = 0;

        boolean mAutoMirrored;

        ColorStateList mTintList;

        BlendMode mBlendMode;

        boolean mHasTintList;

        boolean mHasTintMode;

        protected DrawableContainerState(@Nullable DrawableContainer.DrawableContainerState orig, DrawableContainer owner) {
            this.mOwner = owner;
            if (orig != null) {
                this.mCheckedConstantState = true;
                this.mCanConstantState = true;
                this.mVariablePadding = orig.mVariablePadding;
                this.mConstantSize = orig.mConstantSize;
                this.mMutated = orig.mMutated;
                this.mLayoutDirection = orig.mLayoutDirection;
                this.mEnterFadeDuration = orig.mEnterFadeDuration;
                this.mExitFadeDuration = orig.mExitFadeDuration;
                this.mAutoMirrored = orig.mAutoMirrored;
                this.mTintList = orig.mTintList;
                this.mBlendMode = orig.mBlendMode;
                this.mHasTintList = orig.mHasTintList;
                this.mHasTintMode = orig.mHasTintMode;
                if (orig.mCheckedStateful) {
                    this.mStateful = orig.mStateful;
                    this.mCheckedStateful = true;
                }
                Drawable[] origDr = orig.mDrawables;
                this.mDrawables = new Drawable[origDr.length];
                this.mNumChildren = orig.mNumChildren;
                SparseArray<Drawable.ConstantState> origDf = orig.mDrawableFutures;
                if (origDf != null) {
                    this.mDrawableFutures = origDf.clone();
                } else {
                    this.mDrawableFutures = new SparseArray<>(this.mNumChildren);
                }
                int N = this.mNumChildren;
                for (int i = 0; i < N; i++) {
                    if (origDr[i] != null) {
                        Drawable.ConstantState cs = origDr[i].getConstantState();
                        if (cs != null) {
                            this.mDrawableFutures.put(i, cs);
                        } else {
                            this.mDrawables[i] = origDr[i];
                        }
                    }
                }
            } else {
                this.mDrawables = new Drawable[10];
                this.mNumChildren = 0;
            }
        }

        public final int addChild(Drawable dr) {
            int pos = this.mNumChildren;
            if (pos >= this.mDrawables.length) {
                this.growArray(pos, pos + 10);
            }
            dr.mutate();
            dr.setVisible(false, true);
            dr.setCallback(this.mOwner);
            this.mDrawables[pos] = dr;
            this.mNumChildren++;
            this.invalidateCache();
            this.mConstantPadding = null;
            this.mCheckedPadding = false;
            this.mCheckedConstantSize = false;
            this.mCheckedConstantState = false;
            return pos;
        }

        void invalidateCache() {
            this.mCheckedStateful = false;
        }

        final int getCapacity() {
            return this.mDrawables.length;
        }

        private void createAllFutures() {
            if (this.mDrawableFutures != null) {
                int futureCount = this.mDrawableFutures.size();
                for (int keyIndex = 0; keyIndex < futureCount; keyIndex++) {
                    int index = this.mDrawableFutures.keyAt(keyIndex);
                    Drawable.ConstantState cs = this.mDrawableFutures.valueAt(keyIndex);
                    this.mDrawables[index] = this.prepareDrawable(cs.newDrawable());
                }
                this.mDrawableFutures = null;
            }
        }

        @NonNull
        private Drawable prepareDrawable(@NonNull Drawable child) {
            child.setLayoutDirection(this.mLayoutDirection);
            child = child.mutate();
            child.setCallback(this.mOwner);
            return child;
        }

        public final int getChildCount() {
            return this.mNumChildren;
        }

        public final Drawable[] getChildren() {
            this.createAllFutures();
            return this.mDrawables;
        }

        @Nullable
        public final Drawable getChild(int index) {
            Drawable result = this.mDrawables[index];
            if (result != null) {
                return result;
            } else {
                if (this.mDrawableFutures != null) {
                    int keyIndex = this.mDrawableFutures.indexOfKey(index);
                    if (keyIndex >= 0) {
                        Drawable.ConstantState cs = this.mDrawableFutures.valueAt(keyIndex);
                        Drawable prepared = this.prepareDrawable(cs.newDrawable());
                        this.mDrawables[index] = prepared;
                        this.mDrawableFutures.removeAt(keyIndex);
                        if (this.mDrawableFutures.size() == 0) {
                            this.mDrawableFutures = null;
                        }
                        return prepared;
                    }
                }
                return null;
            }
        }

        final boolean setLayoutDirection(int layoutDirection, int currentIndex) {
            boolean changed = false;
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < N; i++) {
                if (drawables[i] != null) {
                    boolean childChanged = drawables[i].setLayoutDirection(layoutDirection);
                    if (i == currentIndex) {
                        changed = childChanged;
                    }
                }
            }
            this.mLayoutDirection = layoutDirection;
            return changed;
        }

        private void mutate() {
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < N; i++) {
                if (drawables[i] != null) {
                    drawables[i].mutate();
                }
            }
            this.mMutated = true;
        }

        final void clearMutated() {
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < N; i++) {
                if (drawables[i] != null) {
                    drawables[i].clearMutated();
                }
            }
            this.mMutated = false;
        }

        public final void setVariablePadding(boolean variable) {
            this.mVariablePadding = variable;
        }

        @Nullable
        public final Rect getConstantPadding() {
            if (this.mVariablePadding) {
                return null;
            } else if (this.mConstantPadding == null && !this.mCheckedPadding) {
                this.createAllFutures();
                Rect r = null;
                Rect t = new Rect();
                int N = this.mNumChildren;
                Drawable[] drawables = this.mDrawables;
                for (int i = 0; i < N; i++) {
                    if (drawables[i].getPadding(t)) {
                        if (r == null) {
                            r = new Rect(0, 0, 0, 0);
                        }
                        if (t.left > r.left) {
                            r.left = t.left;
                        }
                        if (t.top > r.top) {
                            r.top = t.top;
                        }
                        if (t.right > r.right) {
                            r.right = t.right;
                        }
                        if (t.bottom > r.bottom) {
                            r.bottom = t.bottom;
                        }
                    }
                }
                this.mCheckedPadding = true;
                return this.mConstantPadding = r;
            } else {
                return this.mConstantPadding;
            }
        }

        public final void setConstantSize(boolean constant) {
            this.mConstantSize = constant;
        }

        public final boolean isConstantSize() {
            return this.mConstantSize;
        }

        public final int getConstantWidth() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantWidth;
        }

        public final int getConstantHeight() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantHeight;
        }

        public final int getConstantMinimumWidth() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantMinimumWidth;
        }

        public final int getConstantMinimumHeight() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantMinimumHeight;
        }

        protected void computeConstantSize() {
            this.mCheckedConstantSize = true;
            this.createAllFutures();
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            this.mConstantWidth = this.mConstantHeight = -1;
            this.mConstantMinimumWidth = this.mConstantMinimumHeight = 0;
            for (int i = 0; i < N; i++) {
                Drawable dr = drawables[i];
                int s = dr.getIntrinsicWidth();
                if (s > this.mConstantWidth) {
                    this.mConstantWidth = s;
                }
                s = dr.getIntrinsicHeight();
                if (s > this.mConstantHeight) {
                    this.mConstantHeight = s;
                }
                s = dr.getMinimumWidth();
                if (s > this.mConstantMinimumWidth) {
                    this.mConstantMinimumWidth = s;
                }
                s = dr.getMinimumHeight();
                if (s > this.mConstantMinimumHeight) {
                    this.mConstantMinimumHeight = s;
                }
            }
        }

        public final void setEnterFadeDuration(int duration) {
            this.mEnterFadeDuration = duration;
        }

        public final int getEnterFadeDuration() {
            return this.mEnterFadeDuration;
        }

        public final void setExitFadeDuration(int duration) {
            this.mExitFadeDuration = duration;
        }

        public final int getExitFadeDuration() {
            return this.mExitFadeDuration;
        }

        public final boolean isStateful() {
            if (this.mCheckedStateful) {
                return this.mStateful;
            } else {
                this.createAllFutures();
                int N = this.mNumChildren;
                Drawable[] drawables = this.mDrawables;
                boolean isStateful = false;
                for (int i = 0; i < N; i++) {
                    if (drawables[i].isStateful()) {
                        isStateful = true;
                        break;
                    }
                }
                this.mStateful = isStateful;
                this.mCheckedStateful = true;
                return isStateful;
            }
        }

        public void growArray(int oldSize, int newSize) {
            Drawable[] newDrawables = new Drawable[newSize];
            System.arraycopy(this.mDrawables, 0, newDrawables, 0, oldSize);
            this.mDrawables = newDrawables;
        }

        public synchronized boolean canConstantState() {
            if (this.mCheckedConstantState) {
                return this.mCanConstantState;
            } else {
                this.createAllFutures();
                this.mCheckedConstantState = true;
                int N = this.mNumChildren;
                Drawable[] drawables = this.mDrawables;
                for (int i = 0; i < N; i++) {
                    if (drawables[i].getConstantState() == null) {
                        this.mCanConstantState = false;
                        return false;
                    }
                }
                this.mCanConstantState = true;
                return true;
            }
        }
    }
}