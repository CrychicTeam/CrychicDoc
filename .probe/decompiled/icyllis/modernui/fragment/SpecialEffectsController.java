package icyllis.modernui.fragment;

import icyllis.modernui.annotation.CallSuper;
import icyllis.modernui.core.CancellationSignal;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

abstract class SpecialEffectsController {

    private final ViewGroup mContainer;

    final ArrayList<SpecialEffectsController.Operation> mPendingOperations = new ArrayList();

    final ArrayList<SpecialEffectsController.Operation> mRunningOperations = new ArrayList();

    boolean mOperationDirectionIsPop = false;

    boolean mIsContainerPostponed = false;

    @Nonnull
    static SpecialEffectsController getOrCreateController(@Nonnull ViewGroup container, @Nonnull FragmentManager fragmentManager) {
        SpecialEffectsControllerFactory factory = fragmentManager.getSpecialEffectsControllerFactory();
        return getOrCreateController(container, factory);
    }

    @Nonnull
    static SpecialEffectsController getOrCreateController(@Nonnull ViewGroup container, @Nonnull SpecialEffectsControllerFactory factory) {
        Object controller = container.getTag(33685507);
        if (controller instanceof SpecialEffectsController) {
            return (SpecialEffectsController) controller;
        } else {
            SpecialEffectsController newController = factory.createController(container);
            container.setTag(33685507, newController);
            return newController;
        }
    }

    SpecialEffectsController(@Nonnull ViewGroup container) {
        this.mContainer = container;
    }

    @Nonnull
    public ViewGroup getContainer() {
        return this.mContainer;
    }

    @Nullable
    SpecialEffectsController.Operation.LifecycleImpact getAwaitingCompletionLifecycleImpact(@Nonnull FragmentStateManager fragmentStateManager) {
        SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact = null;
        SpecialEffectsController.Operation pendingOperation = this.findPendingOperation(fragmentStateManager.getFragment());
        if (pendingOperation != null) {
            lifecycleImpact = pendingOperation.getLifecycleImpact();
        }
        SpecialEffectsController.Operation runningOperation = this.findRunningOperation(fragmentStateManager.getFragment());
        return runningOperation == null || lifecycleImpact != null && lifecycleImpact != SpecialEffectsController.Operation.LifecycleImpact.NONE ? lifecycleImpact : runningOperation.getLifecycleImpact();
    }

    @Nullable
    private SpecialEffectsController.Operation findPendingOperation(@Nonnull Fragment fragment) {
        for (SpecialEffectsController.Operation operation : this.mPendingOperations) {
            if (operation.getFragment().equals(fragment) && !operation.isCanceled()) {
                return operation;
            }
        }
        return null;
    }

    @Nullable
    private SpecialEffectsController.Operation findRunningOperation(@Nonnull Fragment fragment) {
        for (SpecialEffectsController.Operation operation : this.mRunningOperations) {
            if (operation.getFragment().equals(fragment) && !operation.isCanceled()) {
                return operation;
            }
        }
        return null;
    }

    void enqueueAdd(@Nonnull SpecialEffectsController.Operation.State finalState, @Nonnull FragmentStateManager fragmentStateManager) {
        this.enqueue(finalState, SpecialEffectsController.Operation.LifecycleImpact.ADDING, fragmentStateManager);
    }

    void enqueueShow(@Nonnull FragmentStateManager fragmentStateManager) {
        this.enqueue(SpecialEffectsController.Operation.State.VISIBLE, SpecialEffectsController.Operation.LifecycleImpact.NONE, fragmentStateManager);
    }

    void enqueueHide(@Nonnull FragmentStateManager fragmentStateManager) {
        this.enqueue(SpecialEffectsController.Operation.State.GONE, SpecialEffectsController.Operation.LifecycleImpact.NONE, fragmentStateManager);
    }

    void enqueueRemove(@Nonnull FragmentStateManager fragmentStateManager) {
        this.enqueue(SpecialEffectsController.Operation.State.REMOVED, SpecialEffectsController.Operation.LifecycleImpact.REMOVING, fragmentStateManager);
    }

    private void enqueue(@Nonnull SpecialEffectsController.Operation.State finalState, @Nonnull SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact, @Nonnull FragmentStateManager fragmentStateManager) {
        synchronized (this.mPendingOperations) {
            CancellationSignal signal = new CancellationSignal();
            SpecialEffectsController.Operation existingOperation = this.findPendingOperation(fragmentStateManager.getFragment());
            if (existingOperation != null) {
                existingOperation.mergeWith(finalState, lifecycleImpact);
            } else {
                SpecialEffectsController.FragmentStateManagerOperation operation = new SpecialEffectsController.FragmentStateManagerOperation(finalState, lifecycleImpact, fragmentStateManager, signal);
                this.mPendingOperations.add(operation);
                operation.addCompletionListener(() -> {
                    if (this.mPendingOperations.contains(operation)) {
                        operation.getFinalState().applyState(operation.getFragment().mView);
                    }
                });
                operation.addCompletionListener(() -> {
                    this.mPendingOperations.remove(operation);
                    this.mRunningOperations.remove(operation);
                });
            }
        }
    }

    void updateOperationDirection(boolean isPop) {
        this.mOperationDirectionIsPop = isPop;
    }

    void markPostponedState() {
        synchronized (this.mPendingOperations) {
            this.updateFinalState();
            this.mIsContainerPostponed = false;
            for (int index = this.mPendingOperations.size() - 1; index >= 0; index--) {
                SpecialEffectsController.Operation operation = (SpecialEffectsController.Operation) this.mPendingOperations.get(index);
                SpecialEffectsController.Operation.State currentState = SpecialEffectsController.Operation.State.from(operation.getFragment().mView);
                if (operation.getFinalState() == SpecialEffectsController.Operation.State.VISIBLE && currentState != SpecialEffectsController.Operation.State.VISIBLE) {
                    Fragment fragment = operation.getFragment();
                    this.mIsContainerPostponed = fragment.isPostponed();
                    break;
                }
            }
        }
    }

    void forcePostponedExecutePendingOperations() {
        if (this.mIsContainerPostponed) {
            this.mIsContainerPostponed = false;
            this.executePendingOperations();
        }
    }

    void executePendingOperations() {
        if (!this.mIsContainerPostponed) {
            if (!this.mContainer.isAttachedToWindow()) {
                this.forceCompleteAllOperations();
                this.mOperationDirectionIsPop = false;
            } else {
                synchronized (this.mPendingOperations) {
                    if (!this.mPendingOperations.isEmpty()) {
                        ArrayList<SpecialEffectsController.Operation> currentlyRunningOperations = new ArrayList(this.mRunningOperations);
                        this.mRunningOperations.clear();
                        for (SpecialEffectsController.Operation operation : currentlyRunningOperations) {
                            operation.cancel();
                            if (!operation.isComplete()) {
                                this.mRunningOperations.add(operation);
                            }
                        }
                        this.updateFinalState();
                        ArrayList<SpecialEffectsController.Operation> newPendingOperations = new ArrayList(this.mPendingOperations);
                        this.mPendingOperations.clear();
                        this.mRunningOperations.addAll(newPendingOperations);
                        for (SpecialEffectsController.Operation operationx : newPendingOperations) {
                            operationx.onStart();
                        }
                        this.executeOperations(newPendingOperations, this.mOperationDirectionIsPop);
                        this.mOperationDirectionIsPop = false;
                    }
                }
            }
        }
    }

    void forceCompleteAllOperations() {
        boolean attachedToWindow = this.mContainer.isAttachedToWindow();
        synchronized (this.mPendingOperations) {
            this.updateFinalState();
            for (SpecialEffectsController.Operation operation : this.mPendingOperations) {
                operation.onStart();
            }
            for (SpecialEffectsController.Operation operation : new ArrayList(this.mRunningOperations)) {
                operation.cancel();
            }
            for (SpecialEffectsController.Operation operation : new ArrayList(this.mPendingOperations)) {
                operation.cancel();
            }
        }
    }

    private void updateFinalState() {
        for (SpecialEffectsController.Operation operation : this.mPendingOperations) {
            if (operation.getLifecycleImpact() == SpecialEffectsController.Operation.LifecycleImpact.ADDING) {
                Fragment fragment = operation.getFragment();
                View view = fragment.requireView();
                SpecialEffectsController.Operation.State finalState = SpecialEffectsController.Operation.State.from(view.getVisibility());
                operation.mergeWith(finalState, SpecialEffectsController.Operation.LifecycleImpact.NONE);
            }
        }
    }

    abstract void executeOperations(@Nonnull List<SpecialEffectsController.Operation> var1, boolean var2);

    private static class FragmentStateManagerOperation extends SpecialEffectsController.Operation {

        @Nonnull
        private final FragmentStateManager mFragmentStateManager;

        FragmentStateManagerOperation(@Nonnull SpecialEffectsController.Operation.State finalState, @Nonnull SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact, @Nonnull FragmentStateManager fragmentStateManager, @Nonnull CancellationSignal cancellationSignal) {
            super(finalState, lifecycleImpact, fragmentStateManager.getFragment(), cancellationSignal);
            this.mFragmentStateManager = fragmentStateManager;
        }

        @Override
        void onStart() {
            if (this.getLifecycleImpact() == SpecialEffectsController.Operation.LifecycleImpact.ADDING) {
                Fragment fragment = this.mFragmentStateManager.getFragment();
                View focusedView = fragment.mView.findFocus();
                if (focusedView != null) {
                    fragment.setFocusedView(focusedView);
                }
                View view = this.getFragment().requireView();
                if (view.getParent() == null) {
                    this.mFragmentStateManager.addViewToContainer();
                    view.setAlpha(0.0F);
                }
                if (view.getAlpha() == 0.0F && view.getVisibility() == 0) {
                    view.setVisibility(4);
                }
                view.setAlpha(fragment.getPostOnViewCreatedAlpha());
            } else if (this.getLifecycleImpact() == SpecialEffectsController.Operation.LifecycleImpact.REMOVING) {
                Fragment fragmentx = this.mFragmentStateManager.getFragment();
                View viewx = fragmentx.requireView();
                viewx.clearFocus();
            }
        }

        @Override
        public void complete() {
            super.complete();
            this.mFragmentStateManager.moveToExpectedState();
        }
    }

    static class Operation {

        @Nonnull
        private SpecialEffectsController.Operation.State mFinalState;

        @Nonnull
        private SpecialEffectsController.Operation.LifecycleImpact mLifecycleImpact;

        @Nonnull
        private final Fragment mFragment;

        @Nonnull
        private final List<Runnable> mCompletionListeners = new ArrayList();

        @Nonnull
        private final HashSet<CancellationSignal> mSpecialEffectsSignals = new HashSet();

        private boolean mIsCanceled = false;

        private boolean mIsComplete = false;

        Operation(@Nonnull SpecialEffectsController.Operation.State finalState, @Nonnull SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact, @Nonnull Fragment fragment, @Nonnull CancellationSignal cancellationSignal) {
            this.mFinalState = finalState;
            this.mLifecycleImpact = lifecycleImpact;
            this.mFragment = fragment;
            cancellationSignal.setOnCancelListener(this::cancel);
        }

        @Nonnull
        public SpecialEffectsController.Operation.State getFinalState() {
            return this.mFinalState;
        }

        @Nonnull
        SpecialEffectsController.Operation.LifecycleImpact getLifecycleImpact() {
            return this.mLifecycleImpact;
        }

        @Nonnull
        public final Fragment getFragment() {
            return this.mFragment;
        }

        final boolean isCanceled() {
            return this.mIsCanceled;
        }

        @Nonnull
        public String toString() {
            return "Operation {" + Integer.toHexString(System.identityHashCode(this)) + "} {mFinalState = " + this.mFinalState + "} {mLifecycleImpact = " + this.mLifecycleImpact + "} {mFragment = " + this.mFragment + "}";
        }

        final void cancel() {
            if (!this.isCanceled()) {
                this.mIsCanceled = true;
                if (this.mSpecialEffectsSignals.isEmpty()) {
                    this.complete();
                } else {
                    for (CancellationSignal signal : new ArrayList(this.mSpecialEffectsSignals)) {
                        signal.cancel();
                    }
                }
            }
        }

        final void mergeWith(@Nonnull SpecialEffectsController.Operation.State finalState, @Nonnull SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact) {
            switch(lifecycleImpact) {
                case ADDING:
                    if (this.mFinalState == SpecialEffectsController.Operation.State.REMOVED) {
                        this.mFinalState = SpecialEffectsController.Operation.State.VISIBLE;
                        this.mLifecycleImpact = SpecialEffectsController.Operation.LifecycleImpact.ADDING;
                    }
                    break;
                case REMOVING:
                    this.mFinalState = SpecialEffectsController.Operation.State.REMOVED;
                    this.mLifecycleImpact = SpecialEffectsController.Operation.LifecycleImpact.REMOVING;
                    break;
                case NONE:
                    if (this.mFinalState != SpecialEffectsController.Operation.State.REMOVED) {
                        this.mFinalState = finalState;
                    }
            }
        }

        final void addCompletionListener(@Nonnull Runnable listener) {
            this.mCompletionListeners.add(listener);
        }

        void onStart() {
        }

        public final void markStartedSpecialEffect(@Nonnull CancellationSignal signal) {
            this.onStart();
            this.mSpecialEffectsSignals.add(signal);
        }

        public final void completeSpecialEffect(@Nonnull CancellationSignal signal) {
            if (this.mSpecialEffectsSignals.remove(signal) && this.mSpecialEffectsSignals.isEmpty()) {
                this.complete();
            }
        }

        final boolean isComplete() {
            return this.mIsComplete;
        }

        @CallSuper
        public void complete() {
            if (!this.mIsComplete) {
                this.mIsComplete = true;
                for (Runnable listener : this.mCompletionListeners) {
                    listener.run();
                }
            }
        }

        static enum LifecycleImpact {

            NONE, ADDING, REMOVING
        }

        static enum State {

            REMOVED, VISIBLE, GONE, INVISIBLE;

            @Nonnull
            static SpecialEffectsController.Operation.State from(@Nonnull View view) {
                return view.getAlpha() == 0.0F && view.getVisibility() == 0 ? INVISIBLE : from(view.getVisibility());
            }

            @Nonnull
            static SpecialEffectsController.Operation.State from(int visibility) {
                return switch(visibility) {
                    case 0 ->
                        VISIBLE;
                    case 4 ->
                        INVISIBLE;
                    case 8 ->
                        GONE;
                    default ->
                        throw new IllegalArgumentException("Unknown visibility " + visibility);
                };
            }

            void applyState(@Nonnull View view) {
                switch(this) {
                    case REMOVED:
                        ViewGroup parent = (ViewGroup) view.getParent();
                        if (parent != null) {
                            parent.removeView(view);
                        }
                        break;
                    case VISIBLE:
                        view.setVisibility(0);
                        break;
                    case GONE:
                        view.setVisibility(8);
                        break;
                    case INVISIBLE:
                        view.setVisibility(4);
                }
            }
        }
    }
}