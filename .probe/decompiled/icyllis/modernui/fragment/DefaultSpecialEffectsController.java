package icyllis.modernui.fragment;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.AnimatorListener;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.core.CancellationSignal;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.transition.Transition;
import icyllis.modernui.transition.TransitionManager;
import icyllis.modernui.transition.TransitionSet;
import icyllis.modernui.util.ArrayMap;
import icyllis.modernui.view.OneShotPreDrawListener;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class DefaultSpecialEffectsController extends SpecialEffectsController {

    DefaultSpecialEffectsController(@Nonnull ViewGroup container) {
        super(container);
    }

    @Override
    void executeOperations(@Nonnull List<SpecialEffectsController.Operation> operations, boolean isPop) {
        SpecialEffectsController.Operation firstOut = null;
        SpecialEffectsController.Operation lastIn = null;
        for (SpecialEffectsController.Operation operation : operations) {
            SpecialEffectsController.Operation.State currentState = SpecialEffectsController.Operation.State.from(operation.getFragment().mView);
            switch(operation.getFinalState()) {
                case GONE:
                case INVISIBLE:
                case REMOVED:
                    if (currentState == SpecialEffectsController.Operation.State.VISIBLE && firstOut == null) {
                        firstOut = operation;
                    }
                    break;
                case VISIBLE:
                    if (currentState != SpecialEffectsController.Operation.State.VISIBLE) {
                        lastIn = operation;
                    }
            }
        }
        List<DefaultSpecialEffectsController.AnimationInfo> animations = new ArrayList();
        List<DefaultSpecialEffectsController.TransitionInfo> transitions = new ArrayList();
        List<SpecialEffectsController.Operation> awaitingContainerChanges = new ArrayList(operations);
        for (SpecialEffectsController.Operation operation : operations) {
            CancellationSignal animCancellationSignal = new CancellationSignal();
            operation.markStartedSpecialEffect(animCancellationSignal);
            animations.add(new DefaultSpecialEffectsController.AnimationInfo(operation, animCancellationSignal, isPop));
            CancellationSignal transitionCancellationSignal = new CancellationSignal();
            operation.markStartedSpecialEffect(transitionCancellationSignal);
            transitions.add(new DefaultSpecialEffectsController.TransitionInfo(operation, transitionCancellationSignal, isPop, isPop ? operation == firstOut : operation == lastIn));
            operation.addCompletionListener(() -> {
                if (awaitingContainerChanges.contains(operation)) {
                    awaitingContainerChanges.remove(operation);
                    applyContainerChanges(operation);
                }
            });
        }
        Object2BooleanMap<SpecialEffectsController.Operation> startedTransitions = this.startTransitions(transitions, awaitingContainerChanges, isPop, firstOut, lastIn);
        this.startAnimations(animations, awaitingContainerChanges, startedTransitions);
        for (SpecialEffectsController.Operation operation : awaitingContainerChanges) {
            applyContainerChanges(operation);
        }
        awaitingContainerChanges.clear();
    }

    private void startAnimations(@Nonnull List<DefaultSpecialEffectsController.AnimationInfo> animationInfos, @Nonnull List<SpecialEffectsController.Operation> awaitingContainerChanges, @Nonnull Object2BooleanMap<SpecialEffectsController.Operation> startedTransitions) {
        final ViewGroup container = this.getContainer();
        for (final DefaultSpecialEffectsController.AnimationInfo animationInfo : animationInfos) {
            if (animationInfo.isVisibilityUnchanged()) {
                animationInfo.completeSpecialEffect();
            } else {
                Animator animator = animationInfo.getAnimator();
                if (animator == null) {
                    animationInfo.completeSpecialEffect();
                } else {
                    final SpecialEffectsController.Operation operation = animationInfo.getOperation();
                    Fragment fragment = operation.getFragment();
                    boolean startedTransition = startedTransitions.getBoolean(operation);
                    if (startedTransition) {
                        animationInfo.completeSpecialEffect();
                    } else {
                        final boolean isHideOperation = operation.getFinalState() == SpecialEffectsController.Operation.State.GONE;
                        if (isHideOperation) {
                            awaitingContainerChanges.remove(operation);
                        }
                        final View viewToAnimate = fragment.mView;
                        container.startViewTransition(viewToAnimate);
                        animator.addListener(new AnimatorListener() {

                            @Override
                            public void onAnimationEnd(@Nonnull Animator anim) {
                                container.endViewTransition(viewToAnimate);
                                if (isHideOperation) {
                                    operation.getFinalState().applyState(viewToAnimate);
                                }
                                animationInfo.completeSpecialEffect();
                            }
                        });
                        animator.setTarget(viewToAnimate);
                        animator.start();
                        CancellationSignal signal = animationInfo.getSignal();
                        signal.setOnCancelListener(() -> animator.end());
                    }
                }
            }
        }
    }

    @Nonnull
    private Object2BooleanMap<SpecialEffectsController.Operation> startTransitions(@Nonnull List<DefaultSpecialEffectsController.TransitionInfo> transitionInfos, @Nonnull List<SpecialEffectsController.Operation> awaitingContainerChanges, boolean isPop, @Nullable SpecialEffectsController.Operation firstOut, @Nullable SpecialEffectsController.Operation lastIn) {
        Object2BooleanMap<SpecialEffectsController.Operation> startedTransitions = new Object2BooleanOpenHashMap();
        boolean hasTransition = false;
        for (DefaultSpecialEffectsController.TransitionInfo transitionInfo : transitionInfos) {
            if (!transitionInfo.isVisibilityUnchanged() && (transitionInfo.getTransition() != null || transitionInfo.getSharedElementTransition() != null)) {
                hasTransition = true;
                break;
            }
        }
        if (!hasTransition) {
            for (DefaultSpecialEffectsController.TransitionInfo transitionInfox : transitionInfos) {
                startedTransitions.put(transitionInfox.getOperation(), false);
                transitionInfox.completeSpecialEffect();
            }
            return startedTransitions;
        } else {
            View nonExistentView = new View(this.getContainer().getContext());
            TransitionSet sharedElementTransition = null;
            View firstOutEpicenterView = null;
            boolean hasLastInEpicenter = false;
            Rect lastInEpicenterRect = new Rect();
            ArrayList<View> sharedElementFirstOutViews = new ArrayList();
            ArrayList<View> sharedElementLastInViews = new ArrayList();
            ArrayMap<String, String> sharedElementNameMapping = new ArrayMap<>();
            for (DefaultSpecialEffectsController.TransitionInfo transitionInfox : transitionInfos) {
                Transition transition = transitionInfox.getSharedElementTransition();
                if (transition != null && firstOut != null && lastIn != null) {
                    TransitionSet transitionSet = new TransitionSet();
                    transitionSet.addTransition(transition.clone());
                    sharedElementTransition = transitionSet;
                    ArrayList<String> exitingNames = lastIn.getFragment().getSharedElementSourceNames();
                    ArrayList<String> firstOutSourceNames = firstOut.getFragment().getSharedElementSourceNames();
                    ArrayList<String> firstOutTargetNames = firstOut.getFragment().getSharedElementTargetNames();
                    for (int index = 0; index < firstOutTargetNames.size(); index++) {
                        int nameIndex = exitingNames.indexOf(firstOutTargetNames.get(index));
                        if (nameIndex != -1) {
                            exitingNames.set(nameIndex, (String) firstOutSourceNames.get(index));
                        }
                    }
                    ArrayList<String> enteringNames = lastIn.getFragment().getSharedElementTargetNames();
                    SharedElementCallback enteringCallback;
                    SharedElementCallback exitingCallback;
                    if (!isPop) {
                        exitingCallback = firstOut.getFragment().getExitTransitionCallback();
                        enteringCallback = lastIn.getFragment().getEnterTransitionCallback();
                    } else {
                        exitingCallback = firstOut.getFragment().getEnterTransitionCallback();
                        enteringCallback = lastIn.getFragment().getExitTransitionCallback();
                    }
                    int numSharedElements = exitingNames.size();
                    for (int i = 0; i < numSharedElements; i++) {
                        String exitingName = (String) exitingNames.get(i);
                        String enteringName = (String) enteringNames.get(i);
                        sharedElementNameMapping.put(exitingName, enteringName);
                    }
                    ArrayMap<String, View> firstOutViews = new ArrayMap<>();
                    this.findNamedViews(firstOutViews, firstOut.getFragment().mView);
                    firstOutViews.retainAll(exitingNames);
                    if (exitingCallback != null) {
                        exitingCallback.onMapSharedElements(exitingNames, firstOutViews);
                        for (int i = exitingNames.size() - 1; i >= 0; i--) {
                            String name = (String) exitingNames.get(i);
                            View view = firstOutViews.get(name);
                            if (view == null) {
                                sharedElementNameMapping.remove(name);
                            } else if (!name.equals(view.getTransitionName())) {
                                String targetValue = sharedElementNameMapping.remove(name);
                                sharedElementNameMapping.put(view.getTransitionName(), targetValue);
                            }
                        }
                    } else {
                        sharedElementNameMapping.retainAll(firstOutViews.keySet());
                    }
                    ArrayMap<String, View> lastInViews = new ArrayMap<>();
                    this.findNamedViews(lastInViews, lastIn.getFragment().mView);
                    lastInViews.retainAll(enteringNames);
                    lastInViews.retainAll(sharedElementNameMapping.values());
                    if (enteringCallback != null) {
                        enteringCallback.onMapSharedElements(enteringNames, lastInViews);
                        for (int ix = enteringNames.size() - 1; ix >= 0; ix--) {
                            String name = (String) enteringNames.get(ix);
                            View view = lastInViews.get(name);
                            if (view == null) {
                                String key = FragmentTransition.findKeyForValue(sharedElementNameMapping, name);
                                if (key != null) {
                                    sharedElementNameMapping.remove(key);
                                }
                            } else if (!name.equals(view.getTransitionName())) {
                                String key = FragmentTransition.findKeyForValue(sharedElementNameMapping, name);
                                if (key != null) {
                                    sharedElementNameMapping.put(key, view.getTransitionName());
                                }
                            }
                        }
                    } else {
                        FragmentTransition.retainValues(sharedElementNameMapping, lastInViews);
                    }
                    this.retainMatchingViews(firstOutViews, sharedElementNameMapping.keySet());
                    this.retainMatchingViews(lastInViews, sharedElementNameMapping.values());
                    this.retainMatchingViews(firstOutViews, sharedElementNameMapping.keySet());
                    this.retainMatchingViews(lastInViews, sharedElementNameMapping.values());
                    if (sharedElementNameMapping.isEmpty()) {
                        sharedElementTransition = null;
                        sharedElementFirstOutViews.clear();
                        sharedElementLastInViews.clear();
                    } else {
                        FragmentTransition.callSharedElementStartEnd(lastIn.getFragment(), firstOut.getFragment(), isPop, firstOutViews, true);
                        OneShotPreDrawListener.add(this.getContainer(), () -> FragmentTransition.callSharedElementStartEnd(lastIn.getFragment(), firstOut.getFragment(), isPop, lastInViews, false));
                        sharedElementFirstOutViews.addAll(firstOutViews.values());
                        if (!exitingNames.isEmpty()) {
                            String epicenterViewName = (String) exitingNames.get(0);
                            firstOutEpicenterView = firstOutViews.get(epicenterViewName);
                            FragmentTransition.setEpicenter(transitionSet, firstOutEpicenterView);
                        }
                        sharedElementLastInViews.addAll(lastInViews.values());
                        if (!enteringNames.isEmpty()) {
                            String epicenterViewName = (String) enteringNames.get(0);
                            View lastInEpicenterView = lastInViews.get(epicenterViewName);
                            if (lastInEpicenterView != null) {
                                hasLastInEpicenter = true;
                                OneShotPreDrawListener.add(this.getContainer(), () -> lastInEpicenterView.getBoundsOnScreen(lastInEpicenterRect));
                            }
                        }
                        FragmentTransition.setSharedElementTargets(transitionSet, nonExistentView, sharedElementFirstOutViews);
                        FragmentTransition.scheduleRemoveTargets(transitionSet, null, null, null, null, transitionSet, sharedElementLastInViews);
                        startedTransitions.put(firstOut, true);
                        startedTransitions.put(lastIn, true);
                    }
                }
            }
            ArrayList<View> enteringViews = new ArrayList();
            Transition mergedTransition = null;
            Transition mergedNonOverlappingTransition = null;
            for (DefaultSpecialEffectsController.TransitionInfo transitionInfoxx : transitionInfos) {
                if (transitionInfoxx.isVisibilityUnchanged()) {
                    startedTransitions.put(transitionInfoxx.getOperation(), false);
                    transitionInfoxx.completeSpecialEffect();
                } else {
                    Transition transition = transitionInfoxx.getTransition();
                    if (transition != null) {
                        transition = transition.clone();
                    }
                    SpecialEffectsController.Operation operation = transitionInfoxx.getOperation();
                    boolean involvedInSharedElementTransition = sharedElementTransition != null && (operation == firstOut || operation == lastIn);
                    if (transition == null) {
                        if (!involvedInSharedElementTransition) {
                            startedTransitions.put(operation, false);
                            transitionInfoxx.completeSpecialEffect();
                        }
                    } else {
                        ArrayList<View> transitioningViews = new ArrayList();
                        this.captureTransitioningViews(transitioningViews, operation.getFragment().mView);
                        if (involvedInSharedElementTransition) {
                            if (operation == firstOut) {
                                transitioningViews.removeAll(sharedElementFirstOutViews);
                            } else {
                                transitioningViews.removeAll(sharedElementLastInViews);
                            }
                        }
                        if (transitioningViews.isEmpty()) {
                            transition.addTarget(nonExistentView);
                        } else {
                            FragmentTransition.addTargets(transition, transitioningViews);
                            FragmentTransition.scheduleRemoveTargets(transition, transition, transitioningViews, null, null, null, null);
                            if (operation.getFinalState() == SpecialEffectsController.Operation.State.GONE) {
                                awaitingContainerChanges.remove(operation);
                                ArrayList<View> transitioningViewsToHide = new ArrayList(transitioningViews);
                                transitioningViewsToHide.remove(operation.getFragment().mView);
                                FragmentTransition.scheduleHideFragmentView(transition, operation.getFragment().mView, transitioningViewsToHide);
                                OneShotPreDrawListener.add(this.getContainer(), () -> FragmentTransition.setViewVisibility(transitioningViews, 4));
                            }
                        }
                        if (operation.getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
                            enteringViews.addAll(transitioningViews);
                            if (hasLastInEpicenter) {
                                FragmentTransition.setEpicenter(transition, lastInEpicenterRect);
                            }
                        } else {
                            FragmentTransition.setEpicenter(transition, firstOutEpicenterView);
                        }
                        startedTransitions.put(operation, true);
                        if (transitionInfoxx.isOverlapAllowed()) {
                            mergedTransition = FragmentTransition.mergeTransitionsTogether(mergedTransition, transition, null);
                        } else {
                            mergedNonOverlappingTransition = FragmentTransition.mergeTransitionsTogether(mergedNonOverlappingTransition, transition, null);
                        }
                    }
                }
            }
            mergedTransition = FragmentTransition.mergeTransitionsInSequence(mergedTransition, mergedNonOverlappingTransition, sharedElementTransition);
            if (mergedTransition == null) {
                return startedTransitions;
            } else {
                for (DefaultSpecialEffectsController.TransitionInfo transitionInfoxxx : transitionInfos) {
                    if (!transitionInfoxxx.isVisibilityUnchanged()) {
                        Object transitionx = transitionInfoxxx.getTransition();
                        SpecialEffectsController.Operation operation = transitionInfoxxx.getOperation();
                        boolean involvedInSharedElementTransition = sharedElementTransition != null && (operation == firstOut || operation == lastIn);
                        if (transitionx != null || involvedInSharedElementTransition) {
                            if (!this.getContainer().isLaidOut()) {
                                transitionInfoxxx.completeSpecialEffect();
                            } else {
                                FragmentTransition.setListenerForTransitionEnd(mergedTransition, transitionInfoxxx.getSignal(), () -> transitionInfo.completeSpecialEffect());
                            }
                        }
                    }
                }
                if (!this.getContainer().isLaidOut()) {
                    return startedTransitions;
                } else {
                    FragmentTransition.setViewVisibility(enteringViews, 4);
                    ArrayList<String> inNames = FragmentTransition.prepareSetNameOverridesReordered(sharedElementLastInViews);
                    TransitionManager.beginDelayedTransition(this.getContainer(), mergedTransition);
                    FragmentTransition.setNameOverridesReordered(this.getContainer(), sharedElementFirstOutViews, sharedElementLastInViews, inNames, sharedElementNameMapping);
                    FragmentTransition.setViewVisibility(enteringViews, 0);
                    FragmentTransition.swapSharedElementTargets(sharedElementTransition, sharedElementFirstOutViews, sharedElementLastInViews);
                    return startedTransitions;
                }
            }
        }
    }

    void retainMatchingViews(@Nonnull ArrayMap<String, View> sharedElementViews, @Nonnull Collection<String> transitionNames) {
        sharedElementViews.entrySet().removeIf(entry -> !transitionNames.contains(((View) entry.getValue()).getTransitionName()));
    }

    void captureTransitioningViews(@Nonnull ArrayList<View> transitioningViews, View view) {
        if (view instanceof ViewGroup viewGroup) {
            if (viewGroup.isTransitionGroup()) {
                if (!transitioningViews.contains(view)) {
                    transitioningViews.add(viewGroup);
                }
            } else {
                int count = viewGroup.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = viewGroup.getChildAt(i);
                    if (child.getVisibility() == 0) {
                        this.captureTransitioningViews(transitioningViews, child);
                    }
                }
            }
        } else if (!transitioningViews.contains(view)) {
            transitioningViews.add(view);
        }
    }

    void findNamedViews(@Nonnull Map<String, View> namedViews, @Nonnull View view) {
        String transitionName = view.getTransitionName();
        if (transitionName != null) {
            namedViews.put(transitionName, view);
        }
        if (view instanceof ViewGroup viewGroup) {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                if (child.getVisibility() == 0) {
                    this.findNamedViews(namedViews, child);
                }
            }
        }
    }

    static void applyContainerChanges(@Nonnull SpecialEffectsController.Operation operation) {
        View view = operation.getFragment().mView;
        operation.getFinalState().applyState(view);
    }

    private static class AnimationInfo extends DefaultSpecialEffectsController.SpecialEffectsInfo {

        private static final Animator fragment_open_enter = ObjectAnimator.ofFloat(null, View.ALPHA, 0.0F, 1.0F);

        private static final Animator fragment_open_exit = ObjectAnimator.ofFloat(null, View.ALPHA, 1.0F, 0.0F);

        private static final Animator fragment_close_enter = fragment_open_enter;

        private static final Animator fragment_close_exit = fragment_open_exit;

        private static final Animator fragment_fade_enter = ObjectAnimator.ofFloat(null, View.ALPHA, 0.0F, 1.0F);

        private static final Animator fragment_fade_exit = ObjectAnimator.ofFloat(null, View.ALPHA, 1.0F, 0.0F);

        private final boolean mIsPop;

        private boolean mLoadedAnim = false;

        @Nullable
        private Animator mAnimator;

        AnimationInfo(@Nonnull SpecialEffectsController.Operation operation, @Nonnull CancellationSignal signal, boolean isPop) {
            super(operation, signal);
            this.mIsPop = isPop;
        }

        @Nullable
        Animator getAnimator() {
            if (this.mLoadedAnim) {
                return this.mAnimator;
            } else {
                this.mAnimator = loadAnimator(this.getOperation().getFragment(), this.getOperation().getFinalState() == SpecialEffectsController.Operation.State.VISIBLE, this.mIsPop);
                this.mLoadedAnim = true;
                return this.mAnimator;
            }
        }

        @Nullable
        private static Animator loadAnimator(@Nonnull Fragment fragment, boolean enter, boolean isPop) {
            int transit = fragment.getNextTransition();
            int nextAnim = getNextAnim(fragment, enter, isPop);
            fragment.setAnimations(0, 0, 0, 0);
            if (fragment.mContainer != null && fragment.mContainer.getTag(33685506) != null) {
                fragment.mContainer.setTag(33685506, null);
            }
            if (fragment.mContainer != null && fragment.mContainer.getLayoutTransition() != null) {
                return null;
            } else {
                Animator animator = fragment.onCreateAnimator(transit, enter, nextAnim);
                if (animator != null) {
                    return animator;
                } else {
                    if (nextAnim == 0 && transit != 0) {
                        switch(transit) {
                            case 4097:
                                return enter ? fragment_open_enter.clone() : fragment_open_exit.clone();
                            case 4099:
                                return enter ? fragment_fade_enter.clone() : fragment_fade_exit.clone();
                            case 4100:
                            case 8197:
                            default:
                                break;
                            case 8194:
                                return enter ? fragment_close_enter.clone() : fragment_close_exit.clone();
                        }
                    }
                    return null;
                }
            }
        }

        private static int getNextAnim(Fragment fragment, boolean enter, boolean isPop) {
            if (isPop) {
                return enter ? fragment.getPopEnterAnim() : fragment.getPopExitAnim();
            } else {
                return enter ? fragment.getEnterAnim() : fragment.getExitAnim();
            }
        }

        static {
            fragment_open_enter.setInterpolator(TimeInterpolator.DECELERATE_CUBIC);
            fragment_open_enter.setDuration(300L);
            fragment_open_exit.setInterpolator(TimeInterpolator.DECELERATE_CUBIC);
            fragment_open_exit.setDuration(300L);
            fragment_fade_enter.setInterpolator(TimeInterpolator.DECELERATE_CUBIC);
            fragment_fade_enter.setDuration(220L);
            fragment_fade_exit.setInterpolator(TimeInterpolator.DECELERATE_CUBIC);
            fragment_fade_exit.setDuration(150L);
        }
    }

    private static class SpecialEffectsInfo {

        @Nonnull
        private final SpecialEffectsController.Operation mOperation;

        @Nonnull
        private final CancellationSignal mSignal;

        SpecialEffectsInfo(@Nonnull SpecialEffectsController.Operation operation, @Nonnull CancellationSignal signal) {
            this.mOperation = operation;
            this.mSignal = signal;
        }

        @Nonnull
        SpecialEffectsController.Operation getOperation() {
            return this.mOperation;
        }

        @Nonnull
        CancellationSignal getSignal() {
            return this.mSignal;
        }

        boolean isVisibilityUnchanged() {
            SpecialEffectsController.Operation.State currentState = SpecialEffectsController.Operation.State.from(this.mOperation.getFragment().mView);
            SpecialEffectsController.Operation.State finalState = this.mOperation.getFinalState();
            return currentState == finalState || currentState != SpecialEffectsController.Operation.State.VISIBLE && finalState != SpecialEffectsController.Operation.State.VISIBLE;
        }

        void completeSpecialEffect() {
            this.mOperation.completeSpecialEffect(this.mSignal);
        }
    }

    private static class TransitionInfo extends DefaultSpecialEffectsController.SpecialEffectsInfo {

        @Nullable
        private final Transition mTransition;

        private final boolean mOverlapAllowed;

        @Nullable
        private final Transition mSharedElementTransition;

        TransitionInfo(@Nonnull SpecialEffectsController.Operation operation, @Nonnull CancellationSignal signal, boolean isPop, boolean providesSharedElementTransition) {
            super(operation, signal);
            if (operation.getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
                this.mTransition = isPop ? operation.getFragment().getReenterTransition() : operation.getFragment().getEnterTransition();
                this.mOverlapAllowed = isPop ? operation.getFragment().getAllowReturnTransitionOverlap() : operation.getFragment().getAllowEnterTransitionOverlap();
            } else {
                this.mTransition = isPop ? operation.getFragment().getReturnTransition() : operation.getFragment().getExitTransition();
                this.mOverlapAllowed = true;
            }
            if (providesSharedElementTransition) {
                if (isPop) {
                    this.mSharedElementTransition = operation.getFragment().getSharedElementReturnTransition();
                } else {
                    this.mSharedElementTransition = operation.getFragment().getSharedElementEnterTransition();
                }
            } else {
                this.mSharedElementTransition = null;
            }
        }

        @Nullable
        Transition getTransition() {
            return this.mTransition;
        }

        boolean isOverlapAllowed() {
            return this.mOverlapAllowed;
        }

        @Nullable
        Transition getSharedElementTransition() {
            return this.mSharedElementTransition;
        }
    }
}