package icyllis.modernui.animation;

import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.View;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class StateListAnimator implements Cloneable {

    private ArrayList<int[]> mSpecs = new ArrayList();

    private ArrayList<Animator> mAnimators = new ArrayList();

    private int mLastMatch = -1;

    private Animator mRunningAnimator;

    private WeakReference<View> mViewRef;

    private AnimatorListener mAnimatorListener;

    public StateListAnimator() {
        this.initAnimatorListener();
    }

    private void initAnimatorListener() {
        this.mAnimatorListener = new AnimatorListener() {

            @Override
            public void onAnimationEnd(@Nonnull Animator animation) {
                animation.setTarget(null);
                if (StateListAnimator.this.mRunningAnimator == animation) {
                    StateListAnimator.this.mRunningAnimator = null;
                }
            }
        };
    }

    public void addState(@Nonnull int[] spec, @Nonnull Animator animator) {
        animator.addListener(this.mAnimatorListener);
        this.mSpecs.add(spec);
        this.mAnimators.add(animator);
    }

    @Nullable
    private View getTarget() {
        return this.mViewRef == null ? null : (View) this.mViewRef.get();
    }

    @Internal
    public void setTarget(@Nullable View view) {
        View current = this.getTarget();
        if (current != view) {
            if (current != null) {
                this.clearTarget();
            }
            if (view != null) {
                this.mViewRef = new WeakReference(view);
            }
        }
    }

    private void clearTarget() {
        for (Animator animator : this.mAnimators) {
            animator.setTarget(null);
        }
        this.mViewRef = null;
        this.mLastMatch = -1;
        this.mRunningAnimator = null;
    }

    @Internal
    public void setState(@Nonnull int[] state) {
        int match = -1;
        int size = this.mSpecs.size();
        for (int i = 0; i < size; i++) {
            int[] spec = (int[]) this.mSpecs.get(i);
            if (StateSet.stateSetMatches(spec, state)) {
                match = i;
                break;
            }
        }
        if (match != this.mLastMatch) {
            if (this.mLastMatch != -1) {
                this.cancel();
            }
            this.mLastMatch = match;
            if (match != -1) {
                this.start((Animator) this.mAnimators.get(match));
            }
        }
    }

    private void start(@Nonnull Animator animator) {
        animator.setTarget(this.getTarget());
        this.mRunningAnimator = animator;
        this.mRunningAnimator.start();
    }

    private void cancel() {
        if (this.mRunningAnimator != null) {
            this.mRunningAnimator.cancel();
            this.mRunningAnimator = null;
        }
    }

    public void jumpToCurrentState() {
        if (this.mRunningAnimator != null) {
            this.mRunningAnimator.end();
        }
    }

    public StateListAnimator clone() {
        try {
            StateListAnimator clone = (StateListAnimator) super.clone();
            int size = this.mSpecs.size();
            clone.mSpecs = new ArrayList(size);
            clone.mAnimators = new ArrayList(size);
            clone.mLastMatch = -1;
            clone.mRunningAnimator = null;
            clone.mViewRef = null;
            clone.initAnimatorListener();
            for (int i = 0; i < size; i++) {
                Animator animatorClone = ((Animator) this.mAnimators.get(i)).clone();
                animatorClone.removeListener(this.mAnimatorListener);
                clone.addState((int[]) this.mSpecs.get(i), animatorClone);
            }
            return clone;
        } catch (CloneNotSupportedException var5) {
            throw new InternalError(var5);
        }
    }
}