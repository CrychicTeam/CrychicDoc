package icyllis.modernui.fragment;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.annotation.CallSuper;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Handler;
import icyllis.modernui.lifecycle.Lifecycle;
import icyllis.modernui.lifecycle.LifecycleObserver;
import icyllis.modernui.lifecycle.LifecycleOwner;
import icyllis.modernui.lifecycle.LifecycleRegistry;
import icyllis.modernui.lifecycle.LiveData;
import icyllis.modernui.lifecycle.MutableLiveData;
import icyllis.modernui.lifecycle.ViewModelProvider;
import icyllis.modernui.lifecycle.ViewModelStore;
import icyllis.modernui.lifecycle.ViewModelStoreOwner;
import icyllis.modernui.lifecycle.ViewTreeLifecycleOwner;
import icyllis.modernui.lifecycle.ViewTreeViewModelStoreOwner;
import icyllis.modernui.transition.AutoTransition;
import icyllis.modernui.transition.Transition;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Fragment implements LifecycleOwner, ViewModelStoreOwner, View.OnCreateContextMenuListener {

    static final Transition USE_DEFAULT_TRANSITION = new AutoTransition();

    static final int INITIALIZING = -1;

    static final int ATTACHED = 0;

    static final int CREATED = 1;

    static final int VIEW_CREATED = 2;

    static final int AWAITING_EXIT_EFFECTS = 3;

    static final int ACTIVITY_CREATED = 4;

    static final int STARTED = 5;

    static final int AWAITING_ENTER_EFFECTS = 6;

    static final int RESUMED = 7;

    int mState = -1;

    DataSet mSavedFragmentState;

    @NonNull
    String mWho = UUID.randomUUID().toString();

    DataSet mArguments;

    private Boolean mIsPrimaryNavigationFragment = null;

    boolean mAdded;

    boolean mRemoving;

    boolean mBeingSaved;

    boolean mFromLayout;

    boolean mInLayout;

    boolean mRestored;

    boolean mPerformedCreateView;

    int mBackStackNesting;

    FragmentManager mFragmentManager;

    FragmentHostCallback<?> mHost;

    @NonNull
    FragmentManager mChildFragmentManager = new FragmentManager();

    Fragment mParentFragment;

    int mFragmentId;

    int mContainerId;

    @Nullable
    String mTag;

    boolean mHidden;

    boolean mDetached;

    boolean mRetainInstance;

    boolean mRetainInstanceChangedWhileDetached;

    boolean mHasMenu;

    boolean mMenuVisible = true;

    private boolean mCalled;

    ViewGroup mContainer;

    View mView;

    boolean mDeferStart;

    boolean mUserVisibleHint = true;

    Fragment.AnimationInfo mAnimationInfo;

    Runnable mPostponedDurationRunnable = this::startPostponedEnterTransition;

    boolean mHiddenChanged;

    boolean mIsCreated;

    Lifecycle.State mMaxState = Lifecycle.State.RESUMED;

    LifecycleRegistry mLifecycleRegistry;

    @Nullable
    FragmentViewLifecycleOwner mViewLifecycleOwner;

    MutableLiveData<LifecycleOwner> mViewLifecycleOwnerLiveData = new MutableLiveData<>();

    ViewModelProvider.Factory mDefaultFactory;

    public Fragment() {
        this.initLifecycle();
    }

    private void initLifecycle() {
        this.mLifecycleRegistry = new LifecycleRegistry(this);
        this.mDefaultFactory = null;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @UiThread
    @NonNull
    public LifecycleOwner getViewLifecycleOwner() {
        if (this.mViewLifecycleOwner == null) {
            throw new IllegalStateException("Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()");
        } else {
            return this.mViewLifecycleOwner;
        }
    }

    @NonNull
    public LiveData<LifecycleOwner> getViewLifecycleOwnerLiveData() {
        return this.mViewLifecycleOwnerLiveData;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        if (this.mFragmentManager == null) {
            throw new IllegalStateException("Can't access ViewModels from detached fragment");
        } else if (this.getMinimumMaxLifecycleState() == Lifecycle.State.INITIALIZED.ordinal()) {
            throw new IllegalStateException("Calling getViewModelStore() before a Fragment reaches onCreate() when using setMaxLifecycle(INITIALIZED) is not supported");
        } else {
            return this.mFragmentManager.getViewModelStore(this);
        }
    }

    private int getMinimumMaxLifecycleState() {
        return this.mMaxState != Lifecycle.State.INITIALIZED && this.mParentFragment != null ? Math.min(this.mMaxState.ordinal(), this.mParentFragment.getMinimumMaxLifecycleState()) : this.mMaxState.ordinal();
    }

    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        if (this.mFragmentManager == null) {
            throw new IllegalStateException("Can't access ViewModels from detached fragment");
        } else {
            return this.mDefaultFactory;
        }
    }

    final boolean isInBackStack() {
        return this.mBackStackNesting > 0;
    }

    public final boolean equals(@Nullable Object o) {
        return super.equals(o);
    }

    public final int hashCode() {
        return super.hashCode();
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append(this.getClass().getSimpleName());
        sb.append("{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append("}");
        sb.append(" (");
        sb.append(this.mWho);
        if (this.mFragmentId != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.mFragmentId));
        }
        if (this.mTag != null) {
            sb.append(" tag=");
            sb.append(this.mTag);
        }
        sb.append(")");
        return sb.toString();
    }

    public final int getId() {
        return this.mFragmentId;
    }

    @Nullable
    public final String getTag() {
        return this.mTag;
    }

    public void setArguments(@Nullable DataSet args) {
        if (this.mFragmentManager != null && this.isStateSaved()) {
            throw new IllegalStateException("Fragment already added and state has been saved");
        } else {
            this.mArguments = args;
        }
    }

    @Nullable
    public final DataSet getArguments() {
        return this.mArguments;
    }

    @NonNull
    public final DataSet requireArguments() {
        DataSet arguments = this.getArguments();
        if (arguments == null) {
            throw new IllegalStateException("Fragment " + this + " does not have any arguments.");
        } else {
            return arguments;
        }
    }

    public final boolean isStateSaved() {
        return this.mFragmentManager == null ? false : this.mFragmentManager.isStateSaved();
    }

    public void setInitialSavedState(@Nullable DataSet state) {
        if (this.mFragmentManager != null) {
            throw new IllegalStateException("Fragment already added");
        } else {
            this.mSavedFragmentState = state;
        }
    }

    @Nullable
    public Context getContext() {
        return this.mHost == null ? null : this.mHost.mContext;
    }

    @NonNull
    public final Context requireContext() {
        Context context = this.getContext();
        if (context == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to a context.");
        } else {
            return context;
        }
    }

    @Nullable
    public final Object getHost() {
        return this.mHost == null ? null : this.mHost.onGetHost();
    }

    @NonNull
    public final Object requireHost() {
        Object host = this.getHost();
        if (host == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to a host.");
        } else {
            return host;
        }
    }

    @NonNull
    public final FragmentManager getParentFragmentManager() {
        FragmentManager fragmentManager = this.mFragmentManager;
        if (fragmentManager == null) {
            throw new IllegalStateException("Fragment " + this + " not associated with a fragment manager.");
        } else {
            return fragmentManager;
        }
    }

    @NonNull
    public final FragmentManager getChildFragmentManager() {
        if (this.mHost == null) {
            throw new IllegalStateException("Fragment " + this + " has not been attached yet.");
        } else {
            return this.mChildFragmentManager;
        }
    }

    @Nullable
    public final Fragment getParentFragment() {
        return this.mParentFragment;
    }

    @NonNull
    public final Fragment requireParentFragment() {
        Fragment parentFragment = this.getParentFragment();
        if (parentFragment == null) {
            Object host = this.getHost();
            if (host == null) {
                throw new IllegalStateException("Fragment " + this + " is not attached to any Fragment or host");
            } else {
                throw new IllegalStateException("Fragment " + this + " is not a child Fragment, it is directly attached to " + host);
            }
        } else {
            return parentFragment;
        }
    }

    public final boolean isAdded() {
        return this.mHost != null && this.mAdded;
    }

    public final boolean isDetached() {
        return this.mDetached;
    }

    public final boolean isRemoving() {
        return this.mRemoving;
    }

    public final boolean isInLayout() {
        return this.mInLayout;
    }

    public final boolean isResumed() {
        return this.mState >= 7;
    }

    public final boolean isVisible() {
        return this.isAdded() && !this.isHidden() && this.mView != null && this.mView.isAttachedToWindow() && this.mView.getVisibility() == 0;
    }

    public final boolean isHidden() {
        return this.mHidden || this.mFragmentManager != null && this.mFragmentManager.isParentHidden(this.mParentFragment);
    }

    final boolean hasOptionsMenu() {
        return this.mHasMenu;
    }

    final boolean isMenuVisible() {
        return this.mMenuVisible && (this.mFragmentManager == null || this.mFragmentManager.isParentMenuVisible(this.mParentFragment));
    }

    @UiThread
    public void onHiddenChanged(boolean hidden) {
    }

    public void setHasOptionsMenu(boolean hasMenu) {
        if (this.mHasMenu != hasMenu) {
            this.mHasMenu = hasMenu;
        }
    }

    public void setMenuVisibility(boolean menuVisible) {
        if (this.mMenuVisible != menuVisible) {
            this.mMenuVisible = menuVisible;
        }
    }

    @UiThread
    @CallSuper
    public void onAttach(@NonNull Context context) {
        this.mCalled = true;
    }

    @UiThread
    @Nullable
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return null;
    }

    @UiThread
    @CallSuper
    public void onCreate(@Nullable DataSet savedInstanceState) {
        this.mCalled = true;
        this.restoreChildFragmentState(savedInstanceState);
        if (!this.mChildFragmentManager.isStateAtLeast(1)) {
            this.mChildFragmentManager.dispatchCreate();
        }
    }

    void restoreChildFragmentState(@Nullable DataSet savedInstanceState) {
    }

    @UiThread
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        return null;
    }

    @UiThread
    public void onViewCreated(@NonNull View view, @Nullable DataSet savedInstanceState) {
    }

    @Nullable
    public View getView() {
        return this.mView;
    }

    @NonNull
    public final View requireView() {
        View view = this.getView();
        if (view == null) {
            throw new IllegalStateException("Fragment " + this + " did not return a View from onCreateView() or this was called before onCreateView().");
        } else {
            return view;
        }
    }

    @UiThread
    @CallSuper
    public void onViewStateRestored(@Nullable DataSet savedInstanceState) {
        this.mCalled = true;
    }

    @UiThread
    @CallSuper
    public void onStart() {
        this.mCalled = true;
    }

    @UiThread
    @CallSuper
    public void onResume() {
        this.mCalled = true;
    }

    @UiThread
    public void onSaveInstanceState(@NonNull DataSet outState) {
    }

    @UiThread
    public void onPrimaryNavigationFragmentChanged(boolean isPrimaryNavigationFragment) {
    }

    @UiThread
    @CallSuper
    public void onPause() {
        this.mCalled = true;
    }

    @UiThread
    @CallSuper
    public void onStop() {
        this.mCalled = true;
    }

    @UiThread
    @CallSuper
    public void onDestroyView() {
        this.mCalled = true;
    }

    @UiThread
    @CallSuper
    public void onDestroy() {
        this.mCalled = true;
    }

    void initState() {
        this.initLifecycle();
        this.mWho = UUID.randomUUID().toString();
        this.mAdded = false;
        this.mRemoving = false;
        this.mFromLayout = false;
        this.mInLayout = false;
        this.mRestored = false;
        this.mBackStackNesting = 0;
        this.mFragmentManager = null;
        this.mChildFragmentManager = new FragmentManager();
        this.mHost = null;
        this.mFragmentId = 0;
        this.mContainerId = 0;
        this.mTag = null;
        this.mHidden = false;
        this.mDetached = false;
    }

    @UiThread
    @CallSuper
    public void onDetach() {
        this.mCalled = true;
    }

    @UiThread
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
    }

    public void registerForContextMenu(@NonNull View view) {
        view.setOnCreateContextMenuListener(this);
    }

    public void unregisterForContextMenu(@NonNull View view) {
        view.setOnCreateContextMenuListener(null);
    }

    public void setEnterSharedElementCallback(@Nullable SharedElementCallback callback) {
        this.ensureAnimationInfo().mEnterTransitionCallback = callback;
    }

    public void setExitSharedElementCallback(@Nullable SharedElementCallback callback) {
        this.ensureAnimationInfo().mExitTransitionCallback = callback;
    }

    public void setEnterTransition(@Nullable Transition transition) {
        this.ensureAnimationInfo().mEnterTransition = transition;
    }

    @Nullable
    public Transition getEnterTransition() {
        return this.mAnimationInfo == null ? null : this.mAnimationInfo.mEnterTransition;
    }

    public void setReturnTransition(@Nullable Transition transition) {
        this.ensureAnimationInfo().mReturnTransition = transition;
    }

    @Nullable
    public Transition getReturnTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        } else {
            return this.mAnimationInfo.mReturnTransition == USE_DEFAULT_TRANSITION ? this.getEnterTransition() : this.mAnimationInfo.mReturnTransition;
        }
    }

    public void setExitTransition(@Nullable Transition transition) {
        this.ensureAnimationInfo().mExitTransition = transition;
    }

    @Nullable
    public Transition getExitTransition() {
        return this.mAnimationInfo == null ? null : this.mAnimationInfo.mExitTransition;
    }

    public void setReenterTransition(@Nullable Transition transition) {
        this.ensureAnimationInfo().mReenterTransition = transition;
    }

    @Nullable
    public Transition getReenterTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        } else {
            return this.mAnimationInfo.mReenterTransition == USE_DEFAULT_TRANSITION ? this.getExitTransition() : this.mAnimationInfo.mReenterTransition;
        }
    }

    public void setSharedElementEnterTransition(@Nullable Transition transition) {
        this.ensureAnimationInfo().mSharedElementEnterTransition = transition;
    }

    @Nullable
    public Transition getSharedElementEnterTransition() {
        return this.mAnimationInfo == null ? null : this.mAnimationInfo.mSharedElementEnterTransition;
    }

    public void setSharedElementReturnTransition(@Nullable Transition transition) {
        this.ensureAnimationInfo().mSharedElementReturnTransition = transition;
    }

    @Nullable
    public Transition getSharedElementReturnTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        } else {
            return this.mAnimationInfo.mSharedElementReturnTransition == USE_DEFAULT_TRANSITION ? this.getSharedElementEnterTransition() : this.mAnimationInfo.mSharedElementReturnTransition;
        }
    }

    public void setAllowEnterTransitionOverlap(boolean allow) {
        this.ensureAnimationInfo().mAllowEnterTransitionOverlap = allow;
    }

    public boolean getAllowEnterTransitionOverlap() {
        return this.mAnimationInfo == null || this.mAnimationInfo.mAllowEnterTransitionOverlap == null || this.mAnimationInfo.mAllowEnterTransitionOverlap;
    }

    public void setAllowReturnTransitionOverlap(boolean allow) {
        this.ensureAnimationInfo().mAllowReturnTransitionOverlap = allow;
    }

    public boolean getAllowReturnTransitionOverlap() {
        return this.mAnimationInfo == null || this.mAnimationInfo.mAllowReturnTransitionOverlap == null || this.mAnimationInfo.mAllowReturnTransitionOverlap;
    }

    public void postponeEnterTransition() {
        this.ensureAnimationInfo().mEnterTransitionPostponed = true;
    }

    public final void postponeEnterTransition(long duration, @NonNull TimeUnit timeUnit) {
        this.ensureAnimationInfo().mEnterTransitionPostponed = true;
        if (this.mFragmentManager != null) {
            Handler handler = this.mFragmentManager.getHost().mHandler;
            handler.removeCallbacks(this.mPostponedDurationRunnable);
            handler.postDelayed(this.mPostponedDurationRunnable, timeUnit.toMillis(duration));
        }
    }

    public void startPostponedEnterTransition() {
        if (this.mAnimationInfo != null && this.ensureAnimationInfo().mEnterTransitionPostponed) {
            if (this.mHost == null) {
                this.ensureAnimationInfo().mEnterTransitionPostponed = false;
            } else if (!this.mHost.mHandler.isCurrentThread()) {
                this.mHost.mHandler.postAtFrontOfQueue(() -> this.callStartTransitionListener(false));
            } else {
                this.callStartTransitionListener(true);
            }
        }
    }

    void callStartTransitionListener(boolean calledDirectly) {
        if (this.mAnimationInfo != null) {
            this.mAnimationInfo.mEnterTransitionPostponed = false;
        }
        if (this.mView != null && this.mContainer != null && this.mFragmentManager != null) {
            SpecialEffectsController controller = SpecialEffectsController.getOrCreateController(this.mContainer, this.mFragmentManager);
            controller.markPostponedState();
            if (calledDirectly) {
                this.mHost.mHandler.post(controller::executePendingOperations);
            } else {
                controller.executePendingOperations();
            }
        }
    }

    public void dump(@NonNull String prefix, @Nullable FileDescriptor fd, @NonNull PrintWriter writer, @Nullable String... args) {
        writer.print(prefix);
        writer.print("mFragmentId=#");
        writer.print(Integer.toHexString(this.mFragmentId));
        writer.print(" mContainerId=#");
        writer.print(Integer.toHexString(this.mContainerId));
        writer.print(" mTag=");
        writer.println(this.mTag);
        writer.print(prefix);
        writer.print("mState=");
        writer.print(this.mState);
        writer.print(" mWho=");
        writer.print(this.mWho);
        writer.print(" mBackStackNesting=");
        writer.println(this.mBackStackNesting);
        writer.print(prefix);
        writer.print("mAdded=");
        writer.print(this.mAdded);
        writer.print(" mRemoving=");
        writer.print(this.mRemoving);
        writer.print(" mFromLayout=");
        writer.print(this.mFromLayout);
        writer.print(" mInLayout=");
        writer.println(this.mInLayout);
        writer.print(prefix);
        writer.print("mHidden=");
        writer.print(this.mHidden);
        writer.print(" mDetached=");
        writer.print(this.mDetached);
        writer.print(" mMenuVisible=");
        writer.print(this.mMenuVisible);
        writer.print(" mHasMenu=");
        writer.println(this.mHasMenu);
        writer.print(prefix);
        writer.print("mRetainInstance=");
        writer.print(this.mRetainInstance);
        writer.print(" mUserVisibleHint=");
        writer.println(this.mUserVisibleHint);
        if (this.mFragmentManager != null) {
            writer.print(prefix);
            writer.print("mFragmentManager=");
            writer.println(this.mFragmentManager);
        }
        if (this.mHost != null) {
            writer.print(prefix);
            writer.print("mHost=");
            writer.println(this.mHost);
        }
        if (this.mParentFragment != null) {
            writer.print(prefix);
            writer.print("mParentFragment=");
            writer.println(this.mParentFragment);
        }
        if (this.mArguments != null) {
            writer.print(prefix);
            writer.print("mArguments=");
            writer.println(this.mArguments);
        }
        if (this.mSavedFragmentState != null) {
            writer.print(prefix);
            writer.print("mSavedFragmentState=");
            writer.println(this.mSavedFragmentState);
        }
        writer.print(prefix);
        writer.print("mPopDirection=");
        writer.println(this.getPopDirection());
        if (this.getEnterAnim() != 0) {
            writer.print(prefix);
            writer.print("getEnterAnim=");
            writer.println(this.getEnterAnim());
        }
        if (this.getExitAnim() != 0) {
            writer.print(prefix);
            writer.print("getExitAnim=");
            writer.println(this.getExitAnim());
        }
        if (this.getPopEnterAnim() != 0) {
            writer.print(prefix);
            writer.print("getPopEnterAnim=");
            writer.println(this.getPopEnterAnim());
        }
        if (this.getPopExitAnim() != 0) {
            writer.print(prefix);
            writer.print("getPopExitAnim=");
            writer.println(this.getPopExitAnim());
        }
        if (this.mContainer != null) {
            writer.print(prefix);
            writer.print("mContainer=");
            writer.println(this.mContainer);
        }
        if (this.mView != null) {
            writer.print(prefix);
            writer.print("mView=");
            writer.println(this.mView);
        }
        if (this.getAnimatingAway() != null) {
            writer.print(prefix);
            writer.print("mAnimatingAway=");
            writer.println(this.getAnimatingAway());
        }
        writer.print(prefix);
        writer.println("Child " + this.mChildFragmentManager + ":");
        this.mChildFragmentManager.dump(prefix + "  ", fd, writer, args);
    }

    @Nullable
    Fragment findFragmentByWho(@NonNull String who) {
        return who.equals(this.mWho) ? this : this.mChildFragmentManager.findFragmentByWho(who);
    }

    @NonNull
    FragmentContainer createFragmentContainer() {
        return new FragmentContainer() {

            @Nullable
            @Override
            public View onFindViewById(int id) {
                if (Fragment.this.mView == null) {
                    throw new IllegalStateException("Fragment " + Fragment.this + " does not have a view");
                } else {
                    return Fragment.this.mView.findViewById(id);
                }
            }

            @Override
            public boolean onHasView() {
                return Fragment.this.mView != null;
            }
        };
    }

    void performAttach() {
        this.mChildFragmentManager.attachController(this.mHost, this.createFragmentContainer(), this);
        this.mState = 0;
        this.mCalled = false;
        this.onAttach(this.mHost.mContext);
        if (!this.mCalled) {
            throw new IllegalStateException("Fragment " + this + " did not call through to super.onAttach()");
        } else {
            this.mFragmentManager.dispatchOnAttachFragment(this);
            this.mChildFragmentManager.dispatchAttach();
        }
    }

    void performCreate(DataSet savedInstanceState) {
        this.mChildFragmentManager.noteStateNotSaved();
        this.mState = 1;
        this.mCalled = false;
        this.mLifecycleRegistry.addObserver(new LifecycleObserver() {

            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_STOP && Fragment.this.mView != null) {
                    Fragment.this.mView.cancelPendingInputEvents();
                }
            }
        });
        this.onCreate(savedInstanceState);
        this.mIsCreated = true;
        if (!this.mCalled) {
            throw new IllegalStateException("Fragment " + this + " did not call through to super.onCreate()");
        } else {
            this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        }
    }

    void performCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        this.mChildFragmentManager.noteStateNotSaved();
        this.mPerformedCreateView = true;
        this.mViewLifecycleOwner = new FragmentViewLifecycleOwner(this, this.getViewModelStore());
        this.mView = this.onCreateView(inflater, container, savedInstanceState);
        if (this.mView != null) {
            this.mViewLifecycleOwner.initialize();
            ViewTreeLifecycleOwner.set(this.mView, this.mViewLifecycleOwner);
            ViewTreeViewModelStoreOwner.set(this.mView, this.mViewLifecycleOwner);
            this.mViewLifecycleOwnerLiveData.setValue(this.mViewLifecycleOwner);
        } else {
            if (this.mViewLifecycleOwner.isInitialized()) {
                throw new IllegalStateException("Called getViewLifecycleOwner() but onCreateView() returned null");
            }
            this.mViewLifecycleOwner = null;
        }
    }

    void performViewCreated() {
        this.onViewCreated(this.mView, this.mSavedFragmentState);
        this.mChildFragmentManager.dispatchViewCreated();
    }

    void performActivityCreated() {
        this.mChildFragmentManager.noteStateNotSaved();
        this.mState = 3;
        this.mChildFragmentManager.dispatchActivityCreated();
    }

    void performStart() {
        this.mChildFragmentManager.noteStateNotSaved();
        this.mChildFragmentManager.execPendingActions(true);
        this.mState = 5;
        this.mCalled = false;
        this.onStart();
        if (!this.mCalled) {
            throw new IllegalStateException("Fragment " + this + " did not call through to super.onStart()");
        } else {
            this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
            if (this.mView != null) {
                this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START);
            }
            this.mChildFragmentManager.dispatchStart();
        }
    }

    void performResume() {
        this.mChildFragmentManager.noteStateNotSaved();
        this.mChildFragmentManager.execPendingActions(true);
        this.mState = 7;
        this.mCalled = false;
        this.onResume();
        if (!this.mCalled) {
            throw new IllegalStateException("Fragment " + this + " did not call through to super.onResume()");
        } else {
            this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            if (this.mView != null) {
                this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            }
            this.mChildFragmentManager.dispatchResume();
        }
    }

    void noteStateNotSaved() {
        this.mChildFragmentManager.noteStateNotSaved();
    }

    void performPrimaryNavigationFragmentChanged() {
        boolean isPrimaryNavigationFragment = this.mFragmentManager.isPrimaryNavigation(this);
        if (this.mIsPrimaryNavigationFragment == null || this.mIsPrimaryNavigationFragment != isPrimaryNavigationFragment) {
            this.mIsPrimaryNavigationFragment = isPrimaryNavigationFragment;
            this.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment);
            this.mChildFragmentManager.dispatchPrimaryNavigationFragmentChanged();
        }
    }

    void performPause() {
        this.mChildFragmentManager.dispatchPause();
        if (this.mView != null) {
            this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        this.mState = 6;
        this.mCalled = false;
        this.onPause();
        if (!this.mCalled) {
            throw new IllegalStateException("Fragment " + this + " did not call through to super.onPause()");
        }
    }

    void performStop() {
        this.mChildFragmentManager.dispatchStop();
        if (this.mView != null) {
            this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        this.mState = 4;
        this.mCalled = false;
        this.onStop();
        if (!this.mCalled) {
            throw new IllegalStateException("Fragment " + this + " did not call through to super.onStop()");
        }
    }

    void performDestroyView() {
        this.mChildFragmentManager.dispatchDestroyView();
        if (this.mView != null && this.mViewLifecycleOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
            this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        }
        this.mState = 1;
        this.mCalled = false;
        this.onDestroyView();
        if (!this.mCalled) {
            throw new IllegalStateException("Fragment " + this + " did not call through to super.onDestroyView()");
        } else {
            this.mPerformedCreateView = false;
        }
    }

    void performDestroy() {
        this.mChildFragmentManager.dispatchDestroy();
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        this.mState = 0;
        this.mCalled = false;
        this.mIsCreated = false;
        this.onDestroy();
        if (!this.mCalled) {
            throw new IllegalStateException("Fragment " + this + " did not call through to super.onDestroy()");
        }
    }

    void performDetach() {
        this.mState = -1;
        this.mCalled = false;
        this.onDetach();
        if (!this.mCalled) {
            throw new IllegalStateException("Fragment " + this + " did not call through to super.onDetach()");
        } else {
            if (!this.mChildFragmentManager.isDestroyed()) {
                this.mChildFragmentManager.dispatchDestroy();
                this.mChildFragmentManager = new FragmentManager();
            }
        }
    }

    private Fragment.AnimationInfo ensureAnimationInfo() {
        if (this.mAnimationInfo == null) {
            this.mAnimationInfo = new Fragment.AnimationInfo();
        }
        return this.mAnimationInfo;
    }

    void setAnimations(int enter, int exit, int popEnter, int popExit) {
        if (this.mAnimationInfo != null || enter != 0 || exit != 0 || popEnter != 0 || popExit != 0) {
            this.ensureAnimationInfo().mEnterAnim = enter;
            this.ensureAnimationInfo().mExitAnim = exit;
            this.ensureAnimationInfo().mPopEnterAnim = popEnter;
            this.ensureAnimationInfo().mPopExitAnim = popExit;
        }
    }

    int getEnterAnim() {
        return this.mAnimationInfo == null ? 0 : this.mAnimationInfo.mEnterAnim;
    }

    int getExitAnim() {
        return this.mAnimationInfo == null ? 0 : this.mAnimationInfo.mExitAnim;
    }

    int getPopEnterAnim() {
        return this.mAnimationInfo == null ? 0 : this.mAnimationInfo.mPopEnterAnim;
    }

    int getPopExitAnim() {
        return this.mAnimationInfo == null ? 0 : this.mAnimationInfo.mPopExitAnim;
    }

    boolean getPopDirection() {
        return this.mAnimationInfo == null ? false : this.mAnimationInfo.mIsPop;
    }

    void setPopDirection(boolean isPop) {
        if (this.mAnimationInfo != null) {
            this.ensureAnimationInfo().mIsPop = isPop;
        }
    }

    int getNextTransition() {
        return this.mAnimationInfo == null ? 0 : this.mAnimationInfo.mNextTransition;
    }

    void setNextTransition(int nextTransition) {
        if (this.mAnimationInfo != null || nextTransition != 0) {
            this.ensureAnimationInfo();
            this.mAnimationInfo.mNextTransition = nextTransition;
        }
    }

    @NonNull
    ArrayList<String> getSharedElementSourceNames() {
        return this.mAnimationInfo != null && this.mAnimationInfo.mSharedElementSourceNames != null ? this.mAnimationInfo.mSharedElementSourceNames : new ArrayList();
    }

    @NonNull
    ArrayList<String> getSharedElementTargetNames() {
        return this.mAnimationInfo != null && this.mAnimationInfo.mSharedElementTargetNames != null ? this.mAnimationInfo.mSharedElementTargetNames : new ArrayList();
    }

    void setSharedElementNames(@Nullable ArrayList<String> sharedElementSourceNames, @Nullable ArrayList<String> sharedElementTargetNames) {
        this.ensureAnimationInfo();
        this.mAnimationInfo.mSharedElementSourceNames = sharedElementSourceNames;
        this.mAnimationInfo.mSharedElementTargetNames = sharedElementTargetNames;
    }

    @Nullable
    SharedElementCallback getEnterTransitionCallback() {
        return this.mAnimationInfo == null ? null : this.mAnimationInfo.mEnterTransitionCallback;
    }

    @Nullable
    SharedElementCallback getExitTransitionCallback() {
        return this.mAnimationInfo == null ? null : this.mAnimationInfo.mExitTransitionCallback;
    }

    @Nullable
    View getAnimatingAway() {
        return this.mAnimationInfo == null ? null : this.mAnimationInfo.mAnimatingAway;
    }

    void setPostOnViewCreatedAlpha(float alpha) {
        this.ensureAnimationInfo().mPostOnViewCreatedAlpha = alpha;
    }

    float getPostOnViewCreatedAlpha() {
        return this.mAnimationInfo == null ? 1.0F : this.mAnimationInfo.mPostOnViewCreatedAlpha;
    }

    void setFocusedView(View view) {
        this.ensureAnimationInfo().mFocusedView = view;
    }

    View getFocusedView() {
        return this.mAnimationInfo == null ? null : this.mAnimationInfo.mFocusedView;
    }

    boolean isPostponed() {
        return this.mAnimationInfo == null ? false : this.mAnimationInfo.mEnterTransitionPostponed;
    }

    static class AnimationInfo {

        View mAnimatingAway;

        boolean mIsPop;

        int mEnterAnim;

        int mExitAnim;

        int mPopEnterAnim;

        int mPopExitAnim;

        int mNextTransition;

        ArrayList<String> mSharedElementSourceNames;

        ArrayList<String> mSharedElementTargetNames;

        Transition mEnterTransition = null;

        Transition mReturnTransition = Fragment.USE_DEFAULT_TRANSITION;

        Transition mExitTransition = null;

        Transition mReenterTransition = Fragment.USE_DEFAULT_TRANSITION;

        Transition mSharedElementEnterTransition = null;

        Transition mSharedElementReturnTransition = Fragment.USE_DEFAULT_TRANSITION;

        Boolean mAllowReturnTransitionOverlap;

        Boolean mAllowEnterTransitionOverlap;

        SharedElementCallback mEnterTransitionCallback = null;

        SharedElementCallback mExitTransitionCallback = null;

        float mPostOnViewCreatedAlpha = 1.0F;

        View mFocusedView = null;

        boolean mEnterTransitionPostponed;
    }
}