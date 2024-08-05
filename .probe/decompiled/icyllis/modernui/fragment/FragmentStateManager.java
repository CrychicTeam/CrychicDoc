package icyllis.modernui.fragment;

import icyllis.modernui.lifecycle.ViewModelStoreOwner;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewParent;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.Marker;

final class FragmentStateManager {

    private static final Marker MARKER = FragmentManager.MARKER;

    private static final LayoutInflater WAITING_FOR_IMPL = new LayoutInflater() {
    };

    private final FragmentLifecycleCallbacksDispatcher mDispatcher;

    private final FragmentStore mFragmentStore;

    @Nonnull
    private final Fragment mFragment;

    private boolean mMovingToState = false;

    private int mFragmentManagerState = -1;

    FragmentStateManager(@Nonnull FragmentLifecycleCallbacksDispatcher dispatcher, @Nonnull FragmentStore fragmentStore, @Nonnull Fragment fragment) {
        this.mDispatcher = dispatcher;
        this.mFragmentStore = fragmentStore;
        this.mFragment = fragment;
    }

    @Nonnull
    Fragment getFragment() {
        return this.mFragment;
    }

    void setFragmentManagerState(int state) {
        this.mFragmentManagerState = state;
    }

    int computeExpectedState() {
        if (this.mFragment.mFragmentManager == null) {
            return this.mFragment.mState;
        } else {
            int maxState = this.mFragmentManagerState;
            maxState = switch(this.mFragment.mMaxState) {
                case RESUMED ->
                    {
                    }
                case STARTED ->
                    Math.min(maxState, 5);
                case CREATED ->
                    Math.min(maxState, 1);
                case INITIALIZED ->
                    Math.min(maxState, 0);
                default ->
                    Math.min(maxState, -1);
            };
            if (this.mFragment.mFromLayout) {
                if (this.mFragment.mInLayout) {
                    maxState = Math.max(this.mFragmentManagerState, 2);
                    if (this.mFragment.mView != null && this.mFragment.mView.getParent() == null) {
                        maxState = 2;
                    }
                } else if (this.mFragmentManagerState < 4) {
                    maxState = Math.min(maxState, this.mFragment.mState);
                } else {
                    maxState = Math.min(maxState, 1);
                }
            }
            if (!this.mFragment.mAdded) {
                maxState = Math.min(maxState, 1);
            }
            SpecialEffectsController.Operation.LifecycleImpact awaitingEffect = null;
            if (this.mFragment.mContainer != null) {
                SpecialEffectsController controller = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager());
                awaitingEffect = controller.getAwaitingCompletionLifecycleImpact(this);
            }
            if (awaitingEffect == SpecialEffectsController.Operation.LifecycleImpact.ADDING) {
                maxState = Math.min(maxState, 6);
            } else if (awaitingEffect == SpecialEffectsController.Operation.LifecycleImpact.REMOVING) {
                maxState = Math.max(maxState, 3);
            } else if (this.mFragment.mRemoving) {
                if (this.mFragment.isInBackStack()) {
                    maxState = Math.min(maxState, 1);
                } else {
                    maxState = Math.min(maxState, -1);
                }
            }
            if (this.mFragment.mDeferStart && this.mFragment.mState < 5) {
                maxState = Math.min(maxState, 4);
            }
            return maxState;
        }
    }

    void moveToExpectedState() {
        if (!this.mMovingToState) {
            try {
                this.mMovingToState = true;
                int newState;
                while ((newState = this.computeExpectedState()) != this.mFragment.mState) {
                    if (newState > this.mFragment.mState) {
                        int nextStep = this.mFragment.mState + 1;
                        switch(nextStep) {
                            case 0:
                                this.attach();
                                break;
                            case 1:
                                this.create();
                                break;
                            case 2:
                                this.ensureInflatedView();
                                this.createView();
                                break;
                            case 3:
                                this.activityCreated();
                                break;
                            case 4:
                                if (this.mFragment.mView != null && this.mFragment.mContainer != null) {
                                    SpecialEffectsController controller = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager());
                                    int visibility = this.mFragment.mView.getVisibility();
                                    SpecialEffectsController.Operation.State finalState = SpecialEffectsController.Operation.State.from(visibility);
                                    controller.enqueueAdd(finalState, this);
                                }
                                this.mFragment.mState = 4;
                                break;
                            case 5:
                                this.start();
                                break;
                            case 6:
                                this.mFragment.mState = 6;
                                break;
                            case 7:
                                this.resume();
                        }
                    } else {
                        int nextStep = this.mFragment.mState - 1;
                        switch(nextStep) {
                            case -1:
                                this.detach();
                                break;
                            case 0:
                                this.destroy();
                                break;
                            case 1:
                                this.destroyFragmentView();
                                this.mFragment.mState = 1;
                                break;
                            case 2:
                                this.mFragment.mInLayout = false;
                                this.mFragment.mState = 2;
                                break;
                            case 3:
                                if (this.mFragment.mView != null && this.mFragment.mContainer != null) {
                                    SpecialEffectsController controller = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager());
                                    controller.enqueueRemove(this);
                                }
                                this.mFragment.mState = 3;
                                break;
                            case 4:
                                this.stop();
                                break;
                            case 5:
                                this.mFragment.mState = 5;
                                break;
                            case 6:
                                this.pause();
                        }
                    }
                }
                if (this.mFragment.mHiddenChanged) {
                    if (this.mFragment.mView != null && this.mFragment.mContainer != null) {
                        SpecialEffectsController controller = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager());
                        if (this.mFragment.mHidden) {
                            controller.enqueueHide(this);
                        } else {
                            controller.enqueueShow(this);
                        }
                    }
                    if (this.mFragment.mFragmentManager != null) {
                        this.mFragment.mFragmentManager.invalidateMenuForFragment(this.mFragment);
                    }
                    this.mFragment.mHiddenChanged = false;
                    this.mFragment.onHiddenChanged(this.mFragment.mHidden);
                }
            } finally {
                this.mMovingToState = false;
            }
        }
    }

    void ensureInflatedView() {
        if (this.mFragment.mFromLayout && this.mFragment.mInLayout && !this.mFragment.mPerformedCreateView) {
            this.mFragment.performCreateView(WAITING_FOR_IMPL, null, this.mFragment.mSavedFragmentState);
            if (this.mFragment.mView != null) {
                this.mFragment.mView.setTag(33685505, this.mFragment);
                if (this.mFragment.mHidden) {
                    this.mFragment.mView.setVisibility(8);
                }
                this.mFragment.performViewCreated();
                this.mDispatcher.dispatchOnFragmentViewCreated(this.mFragment, this.mFragment.mView, this.mFragment.mSavedFragmentState, false);
                this.mFragment.mState = 2;
            }
        }
    }

    void attach() {
        this.mFragment.mHost = this.mFragment.mFragmentManager.getHost();
        this.mFragment.mParentFragment = this.mFragment.mFragmentManager.getParent();
        this.mDispatcher.dispatchOnFragmentPreAttached(this.mFragment, false);
        this.mFragment.performAttach();
        this.mDispatcher.dispatchOnFragmentAttached(this.mFragment, false);
    }

    void create() {
        if (!this.mFragment.mIsCreated) {
            this.mDispatcher.dispatchOnFragmentPreCreated(this.mFragment, this.mFragment.mSavedFragmentState, false);
            this.mFragment.performCreate(this.mFragment.mSavedFragmentState);
            this.mDispatcher.dispatchOnFragmentCreated(this.mFragment, this.mFragment.mSavedFragmentState, false);
        } else {
            this.mFragment.restoreChildFragmentState(this.mFragment.mSavedFragmentState);
            this.mFragment.mState = 1;
        }
    }

    void createView() {
        if (!this.mFragment.mFromLayout) {
            ViewGroup container = null;
            if (this.mFragment.mContainer != null) {
                container = this.mFragment.mContainer;
            } else if (this.mFragment.mContainerId != 0) {
                if (this.mFragment.mContainerId == -1) {
                    throw new IllegalArgumentException("Cannot create fragment " + this.mFragment + " for a container view with no id");
                }
                FragmentContainer fragmentContainer = this.mFragment.mFragmentManager.getContainer();
                container = (ViewGroup) fragmentContainer.onFindViewById(this.mFragment.mContainerId);
                if (container == null && !this.mFragment.mRestored) {
                    throw new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(this.mFragment.mContainerId) + " for fragment " + this.mFragment);
                }
            }
            this.mFragment.mContainer = container;
            this.mFragment.performCreateView(WAITING_FOR_IMPL, container, this.mFragment.mSavedFragmentState);
            if (this.mFragment.mView != null) {
                this.mFragment.mView.setTag(33685505, this.mFragment);
                if (container != null) {
                    this.addViewToContainer();
                }
                if (this.mFragment.mHidden) {
                    this.mFragment.mView.setVisibility(8);
                }
                this.mFragment.performViewCreated();
                this.mDispatcher.dispatchOnFragmentViewCreated(this.mFragment, this.mFragment.mView, this.mFragment.mSavedFragmentState, false);
                int postOnViewCreatedVisibility = this.mFragment.mView.getVisibility();
                float postOnViewCreatedAlpha = this.mFragment.mView.getAlpha();
                this.mFragment.setPostOnViewCreatedAlpha(postOnViewCreatedAlpha);
                if (this.mFragment.mContainer != null && postOnViewCreatedVisibility == 0) {
                    View focusedView = this.mFragment.mView.findFocus();
                    if (focusedView != null) {
                        this.mFragment.setFocusedView(focusedView);
                    }
                    this.mFragment.mView.setAlpha(0.0F);
                }
            }
            this.mFragment.mState = 2;
        }
    }

    void activityCreated() {
        this.mFragment.performActivityCreated();
    }

    void start() {
        this.mFragment.performStart();
        this.mDispatcher.dispatchOnFragmentStarted(this.mFragment, false);
    }

    void resume() {
        View focusedView = this.mFragment.getFocusedView();
        if (focusedView != null && this.isFragmentViewChild(focusedView)) {
            boolean var2 = focusedView.requestFocus();
        }
        this.mFragment.setFocusedView(null);
        this.mFragment.performResume();
        this.mDispatcher.dispatchOnFragmentResumed(this.mFragment, false);
        this.mFragment.mSavedFragmentState = null;
    }

    private boolean isFragmentViewChild(@Nonnull View view) {
        if (view == this.mFragment.mView) {
            return true;
        } else {
            for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
                if (parent == this.mFragment.mView) {
                    return true;
                }
            }
            return false;
        }
    }

    void pause() {
        this.mFragment.performPause();
        this.mDispatcher.dispatchOnFragmentPaused(this.mFragment, false);
    }

    void stop() {
        this.mFragment.performStop();
        this.mDispatcher.dispatchOnFragmentStopped(this.mFragment, false);
    }

    void destroyFragmentView() {
        if (this.mFragment.mContainer != null && this.mFragment.mView != null) {
            this.mFragment.mContainer.removeView(this.mFragment.mView);
        }
        this.mFragment.performDestroyView();
        this.mDispatcher.dispatchOnFragmentViewDestroyed(this.mFragment, false);
        this.mFragment.mContainer = null;
        this.mFragment.mView = null;
        this.mFragment.mViewLifecycleOwner = null;
        this.mFragment.mViewLifecycleOwnerLiveData.setValue(null);
        this.mFragment.mInLayout = false;
    }

    void destroy() {
        boolean beingRemoved = this.mFragment.mRemoving && !this.mFragment.isInBackStack();
        boolean shouldDestroy = beingRemoved || this.mFragmentStore.getViewModel().shouldDestroy(this.mFragment);
        if (shouldDestroy) {
            FragmentHostCallback<?> host = this.mFragment.mHost;
            boolean shouldClear;
            if (host instanceof ViewModelStoreOwner) {
                shouldClear = this.mFragmentStore.getViewModel().isCleared();
            } else {
                shouldClear = true;
            }
            if (beingRemoved || shouldClear) {
                this.mFragmentStore.getViewModel().clearViewModelState(this.mFragment);
            }
            this.mFragment.performDestroy();
            this.mDispatcher.dispatchOnFragmentDestroyed(this.mFragment, false);
            this.mFragmentStore.makeInactive(this);
        } else {
            this.mFragment.mState = 0;
        }
    }

    void detach() {
        this.mFragment.performDetach();
        this.mDispatcher.dispatchOnFragmentDetached(this.mFragment, false);
        this.mFragment.mState = -1;
        this.mFragment.mHost = null;
        this.mFragment.mParentFragment = null;
        this.mFragment.mFragmentManager = null;
        boolean beingRemoved = this.mFragment.mRemoving && !this.mFragment.isInBackStack();
        if (beingRemoved || this.mFragmentStore.getViewModel().shouldDestroy(this.mFragment)) {
            this.mFragment.initState();
        }
    }

    void addViewToContainer() {
        int index = this.mFragmentStore.findFragmentIndexInContainer(this.mFragment);
        this.mFragment.mContainer.addView(this.mFragment.mView, index);
    }
}