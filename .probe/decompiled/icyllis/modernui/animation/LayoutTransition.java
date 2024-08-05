package icyllis.modernui.animation;

import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewParent;
import icyllis.modernui.view.ViewTreeObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LayoutTransition {

    public static final int CHANGE_APPEARING = 0;

    public static final int CHANGE_DISAPPEARING = 1;

    public static final int APPEARING = 2;

    public static final int DISAPPEARING = 3;

    public static final int CHANGING = 4;

    private static final int FLAG_APPEARING = 1;

    private static final int FLAG_DISAPPEARING = 2;

    private static final int FLAG_CHANGE_APPEARING = 4;

    private static final int FLAG_CHANGE_DISAPPEARING = 8;

    private static final int FLAG_CHANGING = 16;

    private static ObjectAnimator defaultChange;

    private static volatile ObjectAnimator defaultChangeIn;

    private static ObjectAnimator defaultChangeOut;

    private static ObjectAnimator defaultFadeIn;

    private static ObjectAnimator defaultFadeOut;

    private static final long DEFAULT_DURATION = 300L;

    private static final TimeInterpolator sAppearingInterpolator = TimeInterpolator.ACCELERATE_DECELERATE;

    private static final TimeInterpolator sDisappearingInterpolator = TimeInterpolator.ACCELERATE_DECELERATE;

    private static final TimeInterpolator sChangingAppearingInterpolator = TimeInterpolator.DECELERATE;

    private static final TimeInterpolator sChangingDisappearingInterpolator = TimeInterpolator.DECELERATE;

    private static final TimeInterpolator sChangingInterpolator = TimeInterpolator.DECELERATE;

    private Animator mDisappearingAnim;

    private Animator mAppearingAnim;

    private Animator mChangingAppearingAnim;

    private Animator mChangingDisappearingAnim;

    private Animator mChangingAnim;

    private long mChangingAppearingDuration = 300L;

    private long mChangingDisappearingDuration = 300L;

    private long mChangingDuration = 300L;

    private long mAppearingDuration = 300L;

    private long mDisappearingDuration = 300L;

    private long mAppearingDelay = 300L;

    private long mDisappearingDelay = 0L;

    private long mChangingAppearingDelay = 0L;

    private long mChangingDisappearingDelay = 300L;

    private long mChangingDelay = 0L;

    private long mChangingAppearingStagger = 0L;

    private long mChangingDisappearingStagger = 0L;

    private long mChangingStagger = 0L;

    private TimeInterpolator mAppearingInterpolator = sAppearingInterpolator;

    private TimeInterpolator mDisappearingInterpolator = sDisappearingInterpolator;

    private TimeInterpolator mChangingAppearingInterpolator = sChangingAppearingInterpolator;

    private TimeInterpolator mChangingDisappearingInterpolator = sChangingDisappearingInterpolator;

    private TimeInterpolator mChangingInterpolator = sChangingInterpolator;

    private final HashMap<View, Animator> pendingAnimations = new HashMap();

    private final LinkedHashMap<View, Animator> currentChangingAnimations = new LinkedHashMap();

    private final LinkedHashMap<View, Animator> currentAppearingAnimations = new LinkedHashMap();

    private final LinkedHashMap<View, Animator> currentDisappearingAnimations = new LinkedHashMap();

    private final HashMap<View, View.OnLayoutChangeListener> layoutChangeListenerMap = new HashMap();

    private long staggerDelay;

    private int mTransitionTypes = 15;

    private ArrayList<LayoutTransition.TransitionListener> mListeners;

    private boolean mAnimateParentHierarchy = true;

    public LayoutTransition() {
        if (defaultChangeIn == null) {
            synchronized (LayoutTransition.class) {
                if (defaultChangeIn == null) {
                    this.initDefaultAnimators();
                }
            }
        }
        this.mChangingAppearingAnim = defaultChangeIn;
        this.mChangingDisappearingAnim = defaultChangeOut;
        this.mChangingAnim = defaultChange;
        this.mAppearingAnim = defaultFadeIn;
        this.mDisappearingAnim = defaultFadeOut;
    }

    private void initDefaultAnimators() {
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt(View.LEFT, 0, 1);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt(View.TOP, 0, 1);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt(View.RIGHT, 0, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt(View.BOTTOM, 0, 1);
        PropertyValuesHolder pvhScrollX = PropertyValuesHolder.ofInt(View.SCROLL_X, 0, 1);
        PropertyValuesHolder pvhScrollY = PropertyValuesHolder.ofInt(View.SCROLL_Y, 0, 1);
        defaultChangeIn = ObjectAnimator.ofPropertyValuesHolder(null, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScrollX, pvhScrollY);
        defaultChangeIn.setDuration(300L);
        defaultChangeIn.setStartDelay(this.mChangingAppearingDelay);
        defaultChangeIn.setInterpolator(this.mChangingAppearingInterpolator);
        defaultChangeOut = defaultChangeIn.clone();
        defaultChangeOut.setStartDelay(this.mChangingDisappearingDelay);
        defaultChangeOut.setInterpolator(this.mChangingDisappearingInterpolator);
        defaultChange = defaultChangeIn.clone();
        defaultChange.setStartDelay(this.mChangingDelay);
        defaultChange.setInterpolator(this.mChangingInterpolator);
        defaultFadeIn = ObjectAnimator.ofFloat(null, View.ALPHA, 0.0F, 1.0F);
        defaultFadeIn.setDuration(300L);
        defaultFadeIn.setStartDelay(this.mAppearingDelay);
        defaultFadeIn.setInterpolator(this.mAppearingInterpolator);
        defaultFadeOut = ObjectAnimator.ofFloat(null, View.ALPHA, 1.0F, 0.0F);
        defaultFadeOut.setDuration(300L);
        defaultFadeOut.setStartDelay(this.mDisappearingDelay);
        defaultFadeOut.setInterpolator(this.mDisappearingInterpolator);
    }

    public void setDuration(long duration) {
        this.mChangingAppearingDuration = duration;
        this.mChangingDisappearingDuration = duration;
        this.mChangingDuration = duration;
        this.mAppearingDuration = duration;
        this.mDisappearingDuration = duration;
    }

    public void enableTransitionType(int transitionType) {
        switch(transitionType) {
            case 0:
                this.mTransitionTypes |= 4;
                break;
            case 1:
                this.mTransitionTypes |= 8;
                break;
            case 2:
                this.mTransitionTypes |= 1;
                break;
            case 3:
                this.mTransitionTypes |= 2;
                break;
            case 4:
                this.mTransitionTypes |= 16;
        }
    }

    public void disableTransitionType(int transitionType) {
        switch(transitionType) {
            case 0:
                this.mTransitionTypes &= -5;
                break;
            case 1:
                this.mTransitionTypes &= -9;
                break;
            case 2:
                this.mTransitionTypes &= -2;
                break;
            case 3:
                this.mTransitionTypes &= -3;
                break;
            case 4:
                this.mTransitionTypes &= -17;
        }
    }

    public boolean isTransitionTypeEnabled(int transitionType) {
        return switch(transitionType) {
            case 0 ->
                (this.mTransitionTypes & 4) == 4;
            case 1 ->
                (this.mTransitionTypes & 8) == 8;
            case 2 ->
                (this.mTransitionTypes & 1) == 1;
            case 3 ->
                (this.mTransitionTypes & 2) == 2;
            case 4 ->
                (this.mTransitionTypes & 16) == 16;
            default ->
                false;
        };
    }

    public void setStartDelay(int transitionType, long delay) {
        switch(transitionType) {
            case 0:
                this.mChangingAppearingDelay = delay;
                break;
            case 1:
                this.mChangingDisappearingDelay = delay;
                break;
            case 2:
                this.mAppearingDelay = delay;
                break;
            case 3:
                this.mDisappearingDelay = delay;
                break;
            case 4:
                this.mChangingDelay = delay;
        }
    }

    public long getStartDelay(int transitionType) {
        return switch(transitionType) {
            case 0 ->
                this.mChangingAppearingDelay;
            case 1 ->
                this.mChangingDisappearingDelay;
            case 2 ->
                this.mAppearingDelay;
            case 3 ->
                this.mDisappearingDelay;
            case 4 ->
                this.mChangingDelay;
            default ->
                0L;
        };
    }

    public void setDuration(int transitionType, long duration) {
        switch(transitionType) {
            case 0:
                this.mChangingAppearingDuration = duration;
                break;
            case 1:
                this.mChangingDisappearingDuration = duration;
                break;
            case 2:
                this.mAppearingDuration = duration;
                break;
            case 3:
                this.mDisappearingDuration = duration;
                break;
            case 4:
                this.mChangingDuration = duration;
        }
    }

    public long getDuration(int transitionType) {
        return switch(transitionType) {
            case 0 ->
                this.mChangingAppearingDuration;
            case 1 ->
                this.mChangingDisappearingDuration;
            case 2 ->
                this.mAppearingDuration;
            case 3 ->
                this.mDisappearingDuration;
            case 4 ->
                this.mChangingDuration;
            default ->
                0L;
        };
    }

    public void setStagger(int transitionType, long duration) {
        switch(transitionType) {
            case 0:
                this.mChangingAppearingStagger = duration;
                break;
            case 1:
                this.mChangingDisappearingStagger = duration;
            case 2:
            case 3:
            default:
                break;
            case 4:
                this.mChangingStagger = duration;
        }
    }

    public long getStagger(int transitionType) {
        return switch(transitionType) {
            case 0 ->
                this.mChangingAppearingStagger;
            case 1 ->
                this.mChangingDisappearingStagger;
            default ->
                0L;
            case 4 ->
                this.mChangingStagger;
        };
    }

    public void setInterpolator(int transitionType, TimeInterpolator interpolator) {
        switch(transitionType) {
            case 0:
                this.mChangingAppearingInterpolator = interpolator;
                break;
            case 1:
                this.mChangingDisappearingInterpolator = interpolator;
                break;
            case 2:
                this.mAppearingInterpolator = interpolator;
                break;
            case 3:
                this.mDisappearingInterpolator = interpolator;
                break;
            case 4:
                this.mChangingInterpolator = interpolator;
        }
    }

    public TimeInterpolator getInterpolator(int transitionType) {
        return switch(transitionType) {
            case 0 ->
                this.mChangingAppearingInterpolator;
            case 1 ->
                this.mChangingDisappearingInterpolator;
            case 2 ->
                this.mAppearingInterpolator;
            case 3 ->
                this.mDisappearingInterpolator;
            case 4 ->
                this.mChangingInterpolator;
            default ->
                null;
        };
    }

    public void setAnimator(int transitionType, Animator animator) {
        switch(transitionType) {
            case 0:
                this.mChangingAppearingAnim = animator;
                break;
            case 1:
                this.mChangingDisappearingAnim = animator;
                break;
            case 2:
                this.mAppearingAnim = animator;
                break;
            case 3:
                this.mDisappearingAnim = animator;
                break;
            case 4:
                this.mChangingAnim = animator;
        }
    }

    public Animator getAnimator(int transitionType) {
        return switch(transitionType) {
            case 0 ->
                this.mChangingAppearingAnim;
            case 1 ->
                this.mChangingDisappearingAnim;
            case 2 ->
                this.mAppearingAnim;
            case 3 ->
                this.mDisappearingAnim;
            case 4 ->
                this.mChangingAnim;
            default ->
                null;
        };
    }

    private void runChangeTransition(ViewGroup parent, View newView, int changeReason) {
        Animator baseAnimator = null;
        Animator parentAnimator = null;
        long duration;
        switch(changeReason) {
            case 2:
                baseAnimator = this.mChangingAppearingAnim;
                duration = this.mChangingAppearingDuration;
                parentAnimator = defaultChangeIn;
                break;
            case 3:
                baseAnimator = this.mChangingDisappearingAnim;
                duration = this.mChangingDisappearingDuration;
                parentAnimator = defaultChangeOut;
                break;
            case 4:
                baseAnimator = this.mChangingAnim;
                duration = this.mChangingDuration;
                parentAnimator = defaultChange;
                break;
            default:
                duration = 0L;
        }
        if (baseAnimator != null) {
            this.staggerDelay = 0L;
            ViewTreeObserver observer = parent.getViewTreeObserver();
            if (observer.isAlive()) {
                int numChildren = parent.getChildCount();
                for (int i = 0; i < numChildren; i++) {
                    View child = parent.getChildAt(i);
                    if (child != newView) {
                        this.setupChangeAnimation(parent, changeReason, baseAnimator, duration, child);
                    }
                }
                if (this.mAnimateParentHierarchy) {
                    ViewGroup tempParent = parent;
                    while (tempParent != null) {
                        ViewParent parentParent = tempParent.getParent();
                        if (parentParent instanceof ViewGroup) {
                            this.setupChangeAnimation((ViewGroup) parentParent, changeReason, parentAnimator, duration, tempParent);
                            tempParent = (ViewGroup) parentParent;
                        } else {
                            tempParent = null;
                        }
                    }
                }
                LayoutTransition.CleanupCallback callback = new LayoutTransition.CleanupCallback(this.layoutChangeListenerMap, parent);
                observer.addOnPreDrawListener(callback);
                parent.addOnAttachStateChangeListener(callback);
            }
        }
    }

    public void setAnimateParentHierarchy(boolean animateParentHierarchy) {
        this.mAnimateParentHierarchy = animateParentHierarchy;
    }

    private void setupChangeAnimation(ViewGroup parent, int changeReason, Animator baseAnimator, long duration, View child) {
        if (this.layoutChangeListenerMap.get(child) == null) {
            if (child.getWidth() != 0 || child.getHeight() != 0) {
                final Animator anim = baseAnimator.clone();
                anim.setTarget(child);
                anim.setupStartValues();
                Animator currentAnimation = (Animator) this.pendingAnimations.get(child);
                if (currentAnimation != null) {
                    currentAnimation.cancel();
                    this.pendingAnimations.remove(child);
                }
                this.pendingAnimations.put(child, anim);
                child.postOnAnimationDelayed(() -> this.pendingAnimations.remove(child), duration + 100L);
                final View.OnLayoutChangeListener listener = new View.OnLayoutChangeListener() {

                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        anim.setupEndValues();
                        if (anim instanceof ObjectAnimator anim) {
                            boolean valuesDiffer = false;
                            for (PropertyValuesHolder pvh : anim.getValues()) {
                                if (pvh.mKeyframes instanceof KeyframeSet) {
                                    Keyframe[] keyframes = ((KeyframeSet) pvh.mKeyframes).mKeyframes;
                                    if (!keyframes[0].getValue().equals(keyframes[keyframes.length - 1].getValue())) {
                                        valuesDiffer = true;
                                    }
                                } else if (!pvh.mKeyframes.getValue(0.0F).equals(pvh.mKeyframes.getValue(1.0F))) {
                                    valuesDiffer = true;
                                }
                            }
                            if (!valuesDiffer) {
                                return;
                            }
                        }
                        long startDelay = 0L;
                        switch(changeReason) {
                            case 2:
                                startDelay = LayoutTransition.this.mChangingAppearingDelay + LayoutTransition.this.staggerDelay;
                                LayoutTransition.this.staggerDelay = LayoutTransition.this.staggerDelay + LayoutTransition.this.mChangingAppearingStagger;
                                if (LayoutTransition.this.mChangingAppearingInterpolator != LayoutTransition.sChangingAppearingInterpolator) {
                                    anim.setInterpolator(LayoutTransition.this.mChangingAppearingInterpolator);
                                }
                                break;
                            case 3:
                                startDelay = LayoutTransition.this.mChangingDisappearingDelay + LayoutTransition.this.staggerDelay;
                                LayoutTransition.this.staggerDelay = LayoutTransition.this.staggerDelay + LayoutTransition.this.mChangingDisappearingStagger;
                                if (LayoutTransition.this.mChangingDisappearingInterpolator != LayoutTransition.sChangingDisappearingInterpolator) {
                                    anim.setInterpolator(LayoutTransition.this.mChangingDisappearingInterpolator);
                                }
                                break;
                            case 4:
                                startDelay = LayoutTransition.this.mChangingDelay + LayoutTransition.this.staggerDelay;
                                LayoutTransition.this.staggerDelay = LayoutTransition.this.staggerDelay + LayoutTransition.this.mChangingStagger;
                                if (LayoutTransition.this.mChangingInterpolator != LayoutTransition.sChangingInterpolator) {
                                    anim.setInterpolator(LayoutTransition.this.mChangingInterpolator);
                                }
                        }
                        anim.setStartDelay(startDelay);
                        anim.setDuration(duration);
                        Animator prevAnimation = (Animator) LayoutTransition.this.currentChangingAnimations.get(child);
                        if (prevAnimation != null) {
                            prevAnimation.cancel();
                        }
                        Animator pendingAnimation = (Animator) LayoutTransition.this.pendingAnimations.get(child);
                        if (pendingAnimation != null) {
                            LayoutTransition.this.pendingAnimations.remove(child);
                        }
                        LayoutTransition.this.currentChangingAnimations.put(child, anim);
                        parent.requestTransitionStart(LayoutTransition.this);
                        child.removeOnLayoutChangeListener(this);
                        LayoutTransition.this.layoutChangeListenerMap.remove(child);
                    }
                };
                anim.addListener(new AnimatorListener() {

                    @Override
                    public void onAnimationStart(@Nonnull Animator animator) {
                        if (LayoutTransition.this.hasListeners()) {
                            for (LayoutTransition.TransitionListener listener : (ArrayList) LayoutTransition.this.mListeners.clone()) {
                                listener.startTransition(LayoutTransition.this, parent, child, changeReason == 2 ? 0 : (changeReason == 3 ? 1 : 4));
                            }
                        }
                    }

                    @Override
                    public void onAnimationEnd(@Nonnull Animator animator) {
                        LayoutTransition.this.currentChangingAnimations.remove(child);
                        if (LayoutTransition.this.hasListeners()) {
                            for (LayoutTransition.TransitionListener listener : (ArrayList) LayoutTransition.this.mListeners.clone()) {
                                listener.endTransition(LayoutTransition.this, parent, child, changeReason == 2 ? 0 : (changeReason == 3 ? 1 : 4));
                            }
                        }
                    }

                    @Override
                    public void onAnimationCancel(@Nonnull Animator animator) {
                        child.removeOnLayoutChangeListener(listener);
                        LayoutTransition.this.layoutChangeListenerMap.remove(child);
                    }
                });
                child.addOnLayoutChangeListener(listener);
                this.layoutChangeListenerMap.put(child, listener);
            }
        }
    }

    public void startChangingAnimations() {
        LinkedHashMap<View, Animator> currentAnimCopy = (LinkedHashMap<View, Animator>) this.currentChangingAnimations.clone();
        for (Animator anim : currentAnimCopy.values()) {
            if (anim instanceof ObjectAnimator) {
                ((ObjectAnimator) anim).setCurrentPlayTime(0L);
            }
            anim.start();
        }
    }

    public void endChangingAnimations() {
        LinkedHashMap<View, Animator> currentAnimCopy = (LinkedHashMap<View, Animator>) this.currentChangingAnimations.clone();
        for (Animator anim : currentAnimCopy.values()) {
            anim.start();
            anim.end();
        }
        this.currentChangingAnimations.clear();
    }

    public boolean isChangingLayout() {
        return this.currentChangingAnimations.size() > 0;
    }

    public boolean isRunning() {
        return this.currentChangingAnimations.size() > 0 || this.currentAppearingAnimations.size() > 0 || this.currentDisappearingAnimations.size() > 0;
    }

    public void cancel() {
        if (this.currentChangingAnimations.size() > 0) {
            LinkedHashMap<View, Animator> currentAnimCopy = (LinkedHashMap<View, Animator>) this.currentChangingAnimations.clone();
            for (Animator anim : currentAnimCopy.values()) {
                anim.cancel();
            }
            this.currentChangingAnimations.clear();
        }
        if (this.currentAppearingAnimations.size() > 0) {
            LinkedHashMap<View, Animator> currentAnimCopy = (LinkedHashMap<View, Animator>) this.currentAppearingAnimations.clone();
            for (Animator anim : currentAnimCopy.values()) {
                anim.end();
            }
            this.currentAppearingAnimations.clear();
        }
        if (this.currentDisappearingAnimations.size() > 0) {
            LinkedHashMap<View, Animator> currentAnimCopy = (LinkedHashMap<View, Animator>) this.currentDisappearingAnimations.clone();
            for (Animator anim : currentAnimCopy.values()) {
                anim.end();
            }
            this.currentDisappearingAnimations.clear();
        }
    }

    public void cancel(int transitionType) {
        switch(transitionType) {
            case 0:
            case 1:
            case 4:
                if (this.currentChangingAnimations.size() > 0) {
                    LinkedHashMap<View, Animator> currentAnimCopy = (LinkedHashMap<View, Animator>) this.currentChangingAnimations.clone();
                    for (Animator anim : currentAnimCopy.values()) {
                        anim.cancel();
                    }
                    this.currentChangingAnimations.clear();
                }
                break;
            case 2:
                if (this.currentAppearingAnimations.size() > 0) {
                    LinkedHashMap<View, Animator> currentAnimCopy = (LinkedHashMap<View, Animator>) this.currentAppearingAnimations.clone();
                    for (Animator anim : currentAnimCopy.values()) {
                        anim.end();
                    }
                    this.currentAppearingAnimations.clear();
                }
                break;
            case 3:
                if (this.currentDisappearingAnimations.size() > 0) {
                    LinkedHashMap<View, Animator> currentAnimCopy = (LinkedHashMap<View, Animator>) this.currentDisappearingAnimations.clone();
                    for (Animator anim : currentAnimCopy.values()) {
                        anim.end();
                    }
                    this.currentDisappearingAnimations.clear();
                }
        }
    }

    private void runAppearingTransition(ViewGroup parent, View child) {
        Animator currentAnimation = (Animator) this.currentDisappearingAnimations.get(child);
        if (currentAnimation != null) {
            currentAnimation.cancel();
        }
        if (this.mAppearingAnim != null) {
            Animator anim = this.mAppearingAnim.clone();
            anim.setTarget(child);
            anim.setStartDelay(this.mAppearingDelay);
            anim.setDuration(this.mAppearingDuration);
            if (this.mAppearingInterpolator != sAppearingInterpolator) {
                anim.setInterpolator(this.mAppearingInterpolator);
            }
            if (anim instanceof ObjectAnimator) {
                ((ObjectAnimator) anim).setCurrentPlayTime(0L);
            }
            anim.addListener(new AnimatorListener() {

                @Override
                public void onAnimationEnd(@Nonnull Animator anim) {
                    LayoutTransition.this.currentAppearingAnimations.remove(child);
                    if (LayoutTransition.this.hasListeners()) {
                        for (LayoutTransition.TransitionListener listener : (ArrayList) LayoutTransition.this.mListeners.clone()) {
                            listener.endTransition(LayoutTransition.this, parent, child, 2);
                        }
                    }
                }
            });
            this.currentAppearingAnimations.put(child, anim);
            anim.start();
        } else {
            if (this.hasListeners()) {
                for (LayoutTransition.TransitionListener listener : (ArrayList) this.mListeners.clone()) {
                    listener.endTransition(this, parent, child, 2);
                }
            }
        }
    }

    private void runDisappearingTransition(ViewGroup parent, View child) {
        Animator currentAnimation = (Animator) this.currentAppearingAnimations.get(child);
        if (currentAnimation != null) {
            currentAnimation.cancel();
        }
        if (this.mDisappearingAnim != null) {
            Animator anim = this.mDisappearingAnim.clone();
            anim.setStartDelay(this.mDisappearingDelay);
            anim.setDuration(this.mDisappearingDuration);
            if (this.mDisappearingInterpolator != sDisappearingInterpolator) {
                anim.setInterpolator(this.mDisappearingInterpolator);
            }
            anim.setTarget(child);
            final float preAnimAlpha = child.getTransitionAlpha();
            anim.addListener(new AnimatorListener() {

                @Override
                public void onAnimationEnd(@Nonnull Animator anim) {
                    LayoutTransition.this.currentDisappearingAnimations.remove(child);
                    child.setTransitionAlpha(preAnimAlpha);
                    if (LayoutTransition.this.hasListeners()) {
                        for (LayoutTransition.TransitionListener listener : (ArrayList) LayoutTransition.this.mListeners.clone()) {
                            listener.endTransition(LayoutTransition.this, parent, child, 3);
                        }
                    }
                }
            });
            if (anim instanceof ObjectAnimator) {
                ((ObjectAnimator) anim).setCurrentPlayTime(0L);
            }
            this.currentDisappearingAnimations.put(child, anim);
            anim.start();
        } else {
            if (this.hasListeners()) {
                for (LayoutTransition.TransitionListener listener : (ArrayList) this.mListeners.clone()) {
                    listener.endTransition(this, parent, child, 3);
                }
            }
        }
    }

    private void addChild(@Nonnull ViewGroup parent, View child, boolean changesLayout) {
        if (parent.getWindowVisibility() == 0) {
            if ((this.mTransitionTypes & 1) == 1) {
                this.cancel(3);
            }
            if (changesLayout && (this.mTransitionTypes & 4) == 4) {
                this.cancel(0);
                this.cancel(4);
            }
            if (this.hasListeners() && (this.mTransitionTypes & 1) == 1) {
                for (LayoutTransition.TransitionListener listener : (ArrayList) this.mListeners.clone()) {
                    listener.startTransition(this, parent, child, 2);
                }
            }
            if (changesLayout && (this.mTransitionTypes & 4) == 4) {
                this.runChangeTransition(parent, child, 2);
            }
            if ((this.mTransitionTypes & 1) == 1) {
                this.runAppearingTransition(parent, child);
            }
        }
    }

    private boolean hasListeners() {
        return this.mListeners != null && this.mListeners.size() > 0;
    }

    public void layoutChange(@Nonnull ViewGroup parent) {
        if (parent.getWindowVisibility() == 0) {
            if ((this.mTransitionTypes & 16) == 16 && !this.isRunning()) {
                this.runChangeTransition(parent, null, 4);
            }
        }
    }

    public void addChild(ViewGroup parent, View child) {
        this.addChild(parent, child, true);
    }

    public void showChild(ViewGroup parent, View child, int oldVisibility) {
        this.addChild(parent, child, oldVisibility == 8);
    }

    private void removeChild(@Nonnull ViewGroup parent, View child, boolean changesLayout) {
        if (parent.getWindowVisibility() == 0) {
            if ((this.mTransitionTypes & 2) == 2) {
                this.cancel(2);
            }
            if (changesLayout && (this.mTransitionTypes & 8) == 8) {
                this.cancel(1);
                this.cancel(4);
            }
            if (this.hasListeners() && (this.mTransitionTypes & 2) == 2) {
                for (LayoutTransition.TransitionListener listener : (ArrayList) this.mListeners.clone()) {
                    listener.startTransition(this, parent, child, 3);
                }
            }
            if (changesLayout && (this.mTransitionTypes & 8) == 8) {
                this.runChangeTransition(parent, child, 3);
            }
            if ((this.mTransitionTypes & 2) == 2) {
                this.runDisappearingTransition(parent, child);
            }
        }
    }

    public void removeChild(ViewGroup parent, View child) {
        this.removeChild(parent, child, true);
    }

    public void hideChild(ViewGroup parent, View child, int newVisibility) {
        this.removeChild(parent, child, newVisibility == 8);
    }

    public void addTransitionListener(LayoutTransition.TransitionListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(listener);
    }

    public void removeTransitionListener(LayoutTransition.TransitionListener listener) {
        if (this.mListeners != null) {
            this.mListeners.remove(listener);
        }
    }

    @Nullable
    public List<LayoutTransition.TransitionListener> getTransitionListeners() {
        return this.mListeners;
    }

    private static final class CleanupCallback implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {

        final Map<View, View.OnLayoutChangeListener> layoutChangeListenerMap;

        final ViewGroup parent;

        CleanupCallback(Map<View, View.OnLayoutChangeListener> listenerMap, ViewGroup parent) {
            this.layoutChangeListenerMap = listenerMap;
            this.parent = parent;
        }

        private void cleanup() {
            this.parent.getViewTreeObserver().removeOnPreDrawListener(this);
            this.parent.removeOnAttachStateChangeListener(this);
            int count = this.layoutChangeListenerMap.size();
            if (count > 0) {
                for (View view : this.layoutChangeListenerMap.keySet()) {
                    View.OnLayoutChangeListener listener = (View.OnLayoutChangeListener) this.layoutChangeListenerMap.get(view);
                    view.removeOnLayoutChangeListener(listener);
                }
                this.layoutChangeListenerMap.clear();
            }
        }

        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            this.cleanup();
        }

        @Override
        public boolean onPreDraw() {
            this.cleanup();
            return true;
        }
    }

    public interface TransitionListener {

        void startTransition(LayoutTransition var1, ViewGroup var2, View var3, int var4);

        void endTransition(LayoutTransition var1, ViewGroup var2, View var3, int var4);
    }
}