package icyllis.modernui.graphics.drawable;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.util.StateSet;

public class StateListDrawable extends DrawableContainer {

    private StateListDrawable.StateListState mStateListState;

    private boolean mMutated;

    public StateListDrawable() {
        this(null, null);
    }

    private StateListDrawable(@Nullable StateListDrawable.StateListState state, @Nullable Object res) {
        StateListDrawable.StateListState newState = new StateListDrawable.StateListState(state, this);
        this.setConstantState(newState);
        this.onStateChange(this.getState());
    }

    StateListDrawable(@Nullable StateListDrawable.StateListState state) {
        if (state != null) {
            this.setConstantState(state);
        }
    }

    public void addState(int[] stateSet, Drawable drawable) {
        if (drawable != null) {
            this.mStateListState.addStateSet(stateSet, drawable);
            this.onStateChange(this.getState());
        }
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public boolean hasFocusStateSpecified() {
        return this.mStateListState.hasFocusStateSpecified();
    }

    @Override
    protected boolean onStateChange(@NonNull int[] stateSet) {
        boolean changed = super.onStateChange(stateSet);
        int idx = this.mStateListState.indexOfStateSet(stateSet);
        if (idx < 0) {
            idx = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD);
        }
        return this.selectDrawable(idx) || changed;
    }

    StateListDrawable.StateListState getStateListState() {
        return this.mStateListState;
    }

    public int getStateCount() {
        return this.mStateListState.getChildCount();
    }

    @NonNull
    public int[] getStateSet(int index) {
        return this.mStateListState.mStateSets[index];
    }

    @Nullable
    public Drawable getStateDrawable(int index) {
        return this.mStateListState.getChild(index);
    }

    public int findStateDrawableIndex(@NonNull int[] stateSet) {
        return this.mStateListState.indexOfStateSet(stateSet);
    }

    @NonNull
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mStateListState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    StateListDrawable.StateListState cloneConstantState() {
        return new StateListDrawable.StateListState(this.mStateListState, this);
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    @Override
    protected void setConstantState(@NonNull DrawableContainer.DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof StateListDrawable.StateListState) {
            this.mStateListState = (StateListDrawable.StateListState) state;
        }
    }

    static class StateListState extends DrawableContainer.DrawableContainerState {

        int[][] mStateSets;

        StateListState(@Nullable StateListDrawable.StateListState orig, StateListDrawable owner) {
            super(orig, owner);
            if (orig != null) {
                this.mStateSets = orig.mStateSets;
            } else {
                this.mStateSets = new int[this.getCapacity()][];
            }
        }

        @Override
        void mutate() {
            int[][] stateSets = new int[this.mStateSets.length][];
            for (int i = this.mStateSets.length - 1; i >= 0; i--) {
                stateSets[i] = this.mStateSets[i] != null ? (int[]) this.mStateSets[i].clone() : null;
            }
            this.mStateSets = stateSets;
        }

        int addStateSet(int[] stateSet, Drawable drawable) {
            int pos = this.addChild(drawable);
            this.mStateSets[pos] = stateSet;
            return pos;
        }

        int indexOfStateSet(int[] stateSet) {
            int[][] stateSets = this.mStateSets;
            int N = this.getChildCount();
            for (int i = 0; i < N; i++) {
                if (StateSet.stateSetMatches(stateSets[i], stateSet)) {
                    return i;
                }
            }
            return -1;
        }

        boolean hasFocusStateSpecified() {
            return StateSet.containsAttribute(this.mStateSets, 16844130);
        }

        @NonNull
        @Override
        public Drawable newDrawable() {
            return new StateListDrawable(this, null);
        }

        @Override
        public void growArray(int oldSize, int newSize) {
            super.growArray(oldSize, newSize);
            int[][] newStateSets = new int[newSize][];
            System.arraycopy(this.mStateSets, 0, newStateSets, 0, oldSize);
            this.mStateSets = newStateSets;
        }
    }
}