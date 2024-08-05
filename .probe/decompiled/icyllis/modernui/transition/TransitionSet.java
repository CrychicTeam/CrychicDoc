package icyllis.modernui.transition;

import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TransitionSet extends Transition {

    private static final int FLAG_CHANGE_INTERPOLATOR = 1;

    private static final int FLAG_CHANGE_PROPAGATION = 2;

    private static final int FLAG_CHANGE_PATH_MOTION = 4;

    static final int FLAG_CHANGE_EPICENTER = 8;

    private ArrayList<Transition> mTransitions = new ArrayList();

    private boolean mPlayTogether = true;

    int mCurrentListeners;

    boolean mStarted = false;

    private int mChangeFlags = 0;

    public static final int ORDERING_TOGETHER = 0;

    public static final int ORDERING_SEQUENTIAL = 1;

    @Nonnull
    public TransitionSet setOrdering(int ordering) {
        switch(ordering) {
            case 0:
                this.mPlayTogether = true;
                break;
            case 1:
                this.mPlayTogether = false;
                break;
            default:
                throw new IllegalArgumentException("Invalid parameter for TransitionSet ordering: " + ordering);
        }
        return this;
    }

    public int getOrdering() {
        return this.mPlayTogether ? 0 : 1;
    }

    @Nonnull
    public TransitionSet addTransition(@Nonnull Transition transition) {
        this.addTransitionInternal(transition);
        if (this.mDuration >= 0L) {
            transition.setDuration(this.mDuration);
        }
        if ((this.mChangeFlags & 1) != 0) {
            transition.setInterpolator(this.getInterpolator());
        }
        if ((this.mChangeFlags & 2) != 0) {
            transition.setPropagation(this.getPropagation());
        }
        if ((this.mChangeFlags & 4) != 0) {
        }
        if ((this.mChangeFlags & 8) != 0) {
            transition.setEpicenterCallback(this.getEpicenterCallback());
        }
        return this;
    }

    private void addTransitionInternal(@Nonnull Transition transition) {
        this.mTransitions.add(transition);
        transition.mParent = this;
    }

    public int getTransitionCount() {
        return this.mTransitions.size();
    }

    @Nullable
    public Transition getTransitionAt(int index) {
        return index >= 0 && index < this.mTransitions.size() ? (Transition) this.mTransitions.get(index) : null;
    }

    @Nonnull
    public TransitionSet setDuration(long duration) {
        super.setDuration(duration);
        if (this.mDuration >= 0L && this.mTransitions != null) {
            for (Transition transition : this.mTransitions) {
                transition.setDuration(duration);
            }
        }
        return this;
    }

    @Nonnull
    public TransitionSet setStartDelay(long startDelay) {
        return (TransitionSet) super.setStartDelay(startDelay);
    }

    @Nonnull
    public TransitionSet setInterpolator(@Nullable TimeInterpolator interpolator) {
        this.mChangeFlags |= 1;
        if (this.mTransitions != null) {
            for (Transition transition : this.mTransitions) {
                transition.setInterpolator(interpolator);
            }
        }
        return (TransitionSet) super.setInterpolator(interpolator);
    }

    @Nonnull
    public TransitionSet addTarget(@Nonnull View target) {
        for (Transition transition : this.mTransitions) {
            transition.addTarget(target);
        }
        return (TransitionSet) super.addTarget(target);
    }

    @Nonnull
    public TransitionSet addTarget(int targetId) {
        for (Transition transition : this.mTransitions) {
            transition.addTarget(targetId);
        }
        return (TransitionSet) super.addTarget(targetId);
    }

    @Nonnull
    public TransitionSet addTarget(@Nonnull String targetName) {
        for (Transition transition : this.mTransitions) {
            transition.addTarget(targetName);
        }
        return (TransitionSet) super.addTarget(targetName);
    }

    @Nonnull
    public TransitionSet addTarget(@Nonnull Class<?> targetType) {
        for (Transition transition : this.mTransitions) {
            transition.addTarget(targetType);
        }
        return (TransitionSet) super.addTarget(targetType);
    }

    @Nonnull
    public TransitionSet addListener(@Nonnull TransitionListener listener) {
        return (TransitionSet) super.addListener(listener);
    }

    @Nonnull
    public TransitionSet removeTarget(int targetId) {
        for (Transition transition : this.mTransitions) {
            transition.removeTarget(targetId);
        }
        return (TransitionSet) super.removeTarget(targetId);
    }

    @Nonnull
    public TransitionSet removeTarget(@Nonnull View target) {
        for (Transition transition : this.mTransitions) {
            transition.removeTarget(target);
        }
        return (TransitionSet) super.removeTarget(target);
    }

    @Nonnull
    public TransitionSet removeTarget(@Nonnull Class<?> target) {
        for (Transition transition : this.mTransitions) {
            transition.removeTarget(target);
        }
        return (TransitionSet) super.removeTarget(target);
    }

    @Nonnull
    public TransitionSet removeTarget(@Nonnull String target) {
        for (Transition transition : this.mTransitions) {
            transition.removeTarget(target);
        }
        return (TransitionSet) super.removeTarget(target);
    }

    @Nonnull
    @Override
    public Transition excludeTarget(@Nonnull View target, boolean exclude) {
        for (Transition transition : this.mTransitions) {
            transition.excludeTarget(target, exclude);
        }
        return super.excludeTarget(target, exclude);
    }

    @Nonnull
    @Override
    public Transition excludeTarget(@Nonnull String targetName, boolean exclude) {
        for (Transition transition : this.mTransitions) {
            transition.excludeTarget(targetName, exclude);
        }
        return super.excludeTarget(targetName, exclude);
    }

    @Nonnull
    @Override
    public Transition excludeTarget(int targetId, boolean exclude) {
        for (Transition transition : this.mTransitions) {
            transition.excludeTarget(targetId, exclude);
        }
        return super.excludeTarget(targetId, exclude);
    }

    @Nonnull
    @Override
    public Transition excludeTarget(@Nonnull Class<?> type, boolean exclude) {
        for (Transition transition : this.mTransitions) {
            transition.excludeTarget(type, exclude);
        }
        return super.excludeTarget(type, exclude);
    }

    @Nonnull
    public TransitionSet removeListener(@Nonnull TransitionListener listener) {
        return (TransitionSet) super.removeListener(listener);
    }

    @Nonnull
    public TransitionSet removeTransition(@Nonnull Transition transition) {
        this.mTransitions.remove(transition);
        transition.mParent = null;
        return this;
    }

    private void setupStartEndListeners() {
        TransitionSet.TransitionSetListener listener = new TransitionSet.TransitionSetListener(this);
        for (Transition childTransition : this.mTransitions) {
            childTransition.addListener(listener);
        }
        this.mCurrentListeners = this.mTransitions.size();
    }

    @Override
    protected void createAnimators(@Nonnull ViewGroup sceneRoot, @Nonnull Transition.TransitionValuesMaps startValues, @Nonnull Transition.TransitionValuesMaps endValues, @Nonnull ArrayList<TransitionValues> startValuesList, @Nonnull ArrayList<TransitionValues> endValuesList) {
        long startDelay = this.getStartDelay();
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            Transition childTransition = (Transition) this.mTransitions.get(i);
            if (startDelay > 0L && (this.mPlayTogether || i == 0)) {
                long childStartDelay = childTransition.getStartDelay();
                if (childStartDelay > 0L) {
                    childTransition.setStartDelay(startDelay + childStartDelay);
                } else {
                    childTransition.setStartDelay(startDelay);
                }
            }
            childTransition.createAnimators(sceneRoot, startValues, endValues, startValuesList, endValuesList);
        }
    }

    @Override
    protected void runAnimators() {
        if (this.mTransitions.isEmpty()) {
            this.start();
            this.end();
        } else {
            this.setupStartEndListeners();
            if (!this.mPlayTogether) {
                for (int i = 1; i < this.mTransitions.size(); i++) {
                    Transition previousTransition = (Transition) this.mTransitions.get(i - 1);
                    final Transition nextTransition = (Transition) this.mTransitions.get(i);
                    previousTransition.addListener(new TransitionListener() {

                        @Override
                        public void onTransitionEnd(@Nonnull Transition transition) {
                            nextTransition.runAnimators();
                            transition.removeListener(this);
                        }
                    });
                }
                Transition firstTransition = (Transition) this.mTransitions.get(0);
                if (firstTransition != null) {
                    firstTransition.runAnimators();
                }
            } else {
                for (Transition childTransition : this.mTransitions) {
                    childTransition.runAnimators();
                }
            }
        }
    }

    @Override
    public void captureStartValues(@Nonnull TransitionValues transitionValues) {
        if (this.isValidTarget(transitionValues.view)) {
            for (Transition childTransition : this.mTransitions) {
                if (childTransition.isValidTarget(transitionValues.view)) {
                    childTransition.captureStartValues(transitionValues);
                    transitionValues.mTargetedTransitions.add(childTransition);
                }
            }
        }
    }

    @Override
    public void captureEndValues(@Nonnull TransitionValues transitionValues) {
        if (this.isValidTarget(transitionValues.view)) {
            for (Transition childTransition : this.mTransitions) {
                if (childTransition.isValidTarget(transitionValues.view)) {
                    childTransition.captureEndValues(transitionValues);
                    transitionValues.mTargetedTransitions.add(childTransition);
                }
            }
        }
    }

    @Override
    void capturePropagationValues(@Nonnull TransitionValues transitionValues) {
        super.capturePropagationValues(transitionValues);
        for (Transition transition : this.mTransitions) {
            transition.capturePropagationValues(transitionValues);
        }
    }

    @Override
    public void pause(View sceneRoot) {
        super.pause(sceneRoot);
        for (Transition transition : this.mTransitions) {
            transition.pause(sceneRoot);
        }
    }

    @Override
    public void resume(@Nonnull View sceneRoot) {
        super.resume(sceneRoot);
        for (Transition transition : this.mTransitions) {
            transition.resume(sceneRoot);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        for (Transition transition : this.mTransitions) {
            transition.cancel();
        }
    }

    @Override
    void forceToEnd(@Nonnull ViewGroup sceneRoot) {
        super.forceToEnd(sceneRoot);
        for (Transition transition : this.mTransitions) {
            transition.forceToEnd(sceneRoot);
        }
    }

    @Override
    void setCanRemoveViews(boolean canRemoveViews) {
        super.setCanRemoveViews(canRemoveViews);
        for (Transition transition : this.mTransitions) {
            transition.setCanRemoveViews(canRemoveViews);
        }
    }

    @Override
    public void setPropagation(@Nullable TransitionPropagation propagation) {
        super.setPropagation(propagation);
        this.mChangeFlags |= 2;
        for (Transition transition : this.mTransitions) {
            transition.setPropagation(propagation);
        }
    }

    @Override
    public void setEpicenterCallback(@Nullable Transition.EpicenterCallback epicenterCallback) {
        super.setEpicenterCallback(epicenterCallback);
        this.mChangeFlags |= 8;
        for (Transition transition : this.mTransitions) {
            transition.setEpicenterCallback(epicenterCallback);
        }
    }

    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder(super.toString(indent));
        for (Transition transition : this.mTransitions) {
            sb.append("\n").append(transition.toString(indent + "  "));
        }
        return sb.toString();
    }

    @Override
    public Transition clone() {
        TransitionSet clone = (TransitionSet) super.clone();
        clone.mTransitions = new ArrayList();
        for (Transition transition : this.mTransitions) {
            clone.addTransitionInternal(transition.clone());
        }
        return clone;
    }

    static class TransitionSetListener implements TransitionListener {

        final TransitionSet mTransitionSet;

        TransitionSetListener(@Nonnull TransitionSet transitionSet) {
            this.mTransitionSet = transitionSet;
        }

        @Override
        public void onTransitionStart(@Nonnull Transition transition) {
            if (!this.mTransitionSet.mStarted) {
                this.mTransitionSet.start();
                this.mTransitionSet.mStarted = true;
            }
        }

        @Override
        public void onTransitionEnd(@Nonnull Transition transition) {
            this.mTransitionSet.mCurrentListeners--;
            if (this.mTransitionSet.mCurrentListeners == 0) {
                this.mTransitionSet.mStarted = false;
                this.mTransitionSet.end();
            }
            transition.removeListener(this);
        }
    }
}