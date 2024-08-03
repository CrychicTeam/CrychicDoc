package icyllis.modernui.graphics.drawable;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;

public class LevelListDrawable extends DrawableContainer {

    private LevelListDrawable.LevelListState mLevelListState;

    private boolean mMutated;

    public LevelListDrawable() {
        this(null, null);
    }

    private LevelListDrawable(@Nullable LevelListDrawable.LevelListState state, @Nullable Object res) {
        LevelListDrawable.LevelListState as = new LevelListDrawable.LevelListState(state, this);
        this.setConstantState(as);
        this.onLevelChange(this.getLevel());
    }

    public void addLevel(int low, int high, Drawable drawable) {
        if (drawable != null) {
            this.mLevelListState.addLevel(low, high, drawable);
            this.onLevelChange(this.getLevel());
        }
    }

    @Override
    protected boolean onLevelChange(int level) {
        int idx = this.mLevelListState.indexOfLevel(level);
        return this.selectDrawable(idx) ? true : super.onLevelChange(level);
    }

    @NonNull
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mLevelListState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    LevelListDrawable.LevelListState cloneConstantState() {
        return new LevelListDrawable.LevelListState(this.mLevelListState, this);
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    @Override
    protected void setConstantState(@NonNull DrawableContainer.DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof LevelListDrawable.LevelListState) {
            this.mLevelListState = (LevelListDrawable.LevelListState) state;
        }
    }

    private static final class LevelListState extends DrawableContainer.DrawableContainerState {

        private int[] mLows;

        private int[] mHighs;

        LevelListState(LevelListDrawable.LevelListState orig, LevelListDrawable owner) {
            super(orig, owner);
            if (orig != null) {
                this.mLows = orig.mLows;
                this.mHighs = orig.mHighs;
            } else {
                this.mLows = new int[this.getCapacity()];
                this.mHighs = new int[this.getCapacity()];
            }
        }

        private void mutate() {
            this.mLows = (int[]) this.mLows.clone();
            this.mHighs = (int[]) this.mHighs.clone();
        }

        public void addLevel(int low, int high, Drawable drawable) {
            int pos = this.addChild(drawable);
            this.mLows[pos] = low;
            this.mHighs[pos] = high;
        }

        public int indexOfLevel(int level) {
            int[] lows = this.mLows;
            int[] highs = this.mHighs;
            int N = this.getChildCount();
            for (int i = 0; i < N; i++) {
                if (level >= lows[i] && level <= highs[i]) {
                    return i;
                }
            }
            return -1;
        }

        @NonNull
        @Override
        public Drawable newDrawable() {
            return new LevelListDrawable(this, null);
        }

        @Override
        public void growArray(int oldSize, int newSize) {
            super.growArray(oldSize, newSize);
            int[] newInts = new int[newSize];
            System.arraycopy(this.mLows, 0, newInts, 0, oldSize);
            this.mLows = newInts;
            newInts = new int[newSize];
            System.arraycopy(this.mHighs, 0, newInts, 0, oldSize);
            this.mHighs = newInts;
        }
    }
}