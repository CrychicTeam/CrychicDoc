package icyllis.modernui.fragment;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.core.LogWriter;
import icyllis.modernui.lifecycle.Lifecycle;
import icyllis.modernui.lifecycle.LifecycleObserver;
import icyllis.modernui.lifecycle.LifecycleOwner;
import icyllis.modernui.lifecycle.ViewModelStore;
import icyllis.modernui.lifecycle.ViewModelStoreOwner;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public final class FragmentManager implements FragmentResultOwner {

    static final boolean DEBUG = false;

    static final boolean TRACE = false;

    static final Marker MARKER = MarkerManager.getMarker("FragmentManager");

    public static final int POP_BACK_STACK_INCLUSIVE = 1;

    private final ArrayList<FragmentManager.OpGenerator> mPendingActions = new ArrayList();

    private boolean mExecutingActions;

    private final FragmentStore mFragmentStore = new FragmentStore();

    ArrayList<BackStackRecord> mBackStack;

    private ArrayList<Fragment> mCreatedMenus;

    private OnBackPressedDispatcher mOnBackPressedDispatcher;

    private final OnBackPressedCallback mOnBackPressedCallback = new OnBackPressedCallback(false) {

        @Override
        public void handleOnBackPressed() {
            FragmentManager.this.handleOnBackPressed();
        }
    };

    private final AtomicInteger mBackStackIndex = new AtomicInteger();

    private final Map<String, DataSet> mResults = Collections.synchronizedMap(new HashMap());

    private final Map<String, FragmentManager.LifecycleAwareResultListener> mResultListeners = Collections.synchronizedMap(new HashMap());

    private ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;

    private final FragmentLifecycleCallbacksDispatcher mLifecycleCallbacksDispatcher = new FragmentLifecycleCallbacksDispatcher(this);

    private final CopyOnWriteArrayList<FragmentOnAttachListener> mOnAttachListeners = new CopyOnWriteArrayList();

    int mCurState = -1;

    private FragmentHostCallback<?> mHost;

    private FragmentContainer mContainer;

    private Fragment mParent;

    @Nullable
    Fragment mPrimaryNav;

    private FragmentFactory mFragmentFactory = null;

    private static final FragmentFactory sHostFragmentFactory = new FragmentFactory();

    private SpecialEffectsControllerFactory mSpecialEffectsControllerFactory = null;

    private static final SpecialEffectsControllerFactory sDefaultSpecialEffectsControllerFactory = DefaultSpecialEffectsController::new;

    private boolean mNeedMenuInvalidate;

    private boolean mStateSaved;

    private boolean mStopped;

    private boolean mDestroyed;

    private boolean mHavePendingDeferredStart;

    private ArrayList<BackStackRecord> mTmpRecords;

    private BooleanArrayList mTmpIsPop;

    private ArrayList<Fragment> mTmpAddedFragments;

    private FragmentManagerViewModel mViewModel;

    private final Runnable mExecCommit = () -> this.execPendingActions(true);

    FragmentManager() {
    }

    private void throwException(@Nonnull RuntimeException ex) {
        ModernUI.LOGGER.error(MARKER, "FragmentManager throws an exception", ex);
        PrintWriter w = new PrintWriter(new LogWriter(ModernUI.LOGGER, Level.DEBUG, MARKER), true);
        PrintWriter var3 = w;
        try {
            if (this.mHost != null) {
                try {
                    this.mHost.onDump("  ", null, w);
                } catch (Exception var8) {
                    ModernUI.LOGGER.error(MARKER, "Failed dumping state", var8);
                }
            } else {
                try {
                    this.dump("  ", null, w);
                } catch (Exception var7) {
                    ModernUI.LOGGER.error(MARKER, "Failed dumping state", var7);
                }
            }
        } catch (Throwable var9) {
            if (w != null) {
                try {
                    var3.close();
                } catch (Throwable var6) {
                    var9.addSuppressed(var6);
                }
            }
            throw var9;
        }
        if (w != null) {
            w.close();
        }
        throw ex;
    }

    @Nonnull
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public boolean executePendingTransactions() {
        boolean updates = this.execPendingActions(true);
        this.forcePostponedTransactions();
        return updates;
    }

    private void updateOnBackPressedCallbackEnabled() {
        synchronized (this.mPendingActions) {
            if (!this.mPendingActions.isEmpty()) {
                this.mOnBackPressedCallback.setEnabled(true);
                return;
            }
        }
        this.mOnBackPressedCallback.setEnabled(this.getBackStackEntryCount() > 0 && this.isPrimaryNavigation(this.mParent));
    }

    boolean isPrimaryNavigation(@Nullable Fragment parent) {
        if (parent == null) {
            return true;
        } else {
            FragmentManager parentFragmentManager = parent.mFragmentManager;
            Fragment primaryNavigationFragment = parentFragmentManager.getPrimaryNavigationFragment();
            return parent.equals(primaryNavigationFragment) && this.isPrimaryNavigation(parentFragmentManager.mParent);
        }
    }

    boolean isParentMenuVisible(@Nullable Fragment parent) {
        return parent == null ? true : parent.isMenuVisible();
    }

    boolean isParentHidden(@Nullable Fragment parent) {
        return parent == null ? false : parent.isHidden();
    }

    void handleOnBackPressed() {
        this.execPendingActions(true);
        if (this.mOnBackPressedCallback.isEnabled()) {
            this.popBackStackImmediate();
        } else {
            this.mOnBackPressedDispatcher.onBackPressed();
        }
    }

    public void popBackStack() {
        this.enqueueAction(new FragmentManager.PopBackStackState(null, -1, 0), false);
    }

    public boolean popBackStackImmediate() {
        return this.popBackStackImmediate(null, -1, 0);
    }

    public void popBackStack(@Nullable String name, int flags) {
        this.enqueueAction(new FragmentManager.PopBackStackState(name, -1, flags), false);
    }

    public boolean popBackStackImmediate(@Nullable String name, int flags) {
        return this.popBackStackImmediate(name, -1, flags);
    }

    public void popBackStack(int id, int flags) {
        if (id < 0) {
            throw new IllegalArgumentException("Bad id: " + id);
        } else {
            this.enqueueAction(new FragmentManager.PopBackStackState(null, id, flags), false);
        }
    }

    public boolean popBackStackImmediate(int id, int flags) {
        if (id < 0) {
            throw new IllegalArgumentException("Bad id: " + id);
        } else {
            return this.popBackStackImmediate(null, id, flags);
        }
    }

    private boolean popBackStackImmediate(@Nullable String name, int id, int flags) {
        this.execPendingActions(false);
        this.ensureExecReady(true);
        if (this.mPrimaryNav != null && id < 0 && name == null) {
            FragmentManager childManager = this.mPrimaryNav.getChildFragmentManager();
            if (childManager.popBackStackImmediate()) {
                return true;
            }
        }
        boolean executePop = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, name, id, flags);
        if (executePop) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                this.cleanupExec();
            }
        }
        this.updateOnBackPressedCallbackEnabled();
        this.doPendingDeferredStart();
        this.mFragmentStore.burpActive();
        return executePop;
    }

    public int getBackStackEntryCount() {
        return this.mBackStack != null ? this.mBackStack.size() : 0;
    }

    @Nonnull
    public FragmentManager.BackStackEntry getBackStackEntryAt(int index) {
        return (FragmentManager.BackStackEntry) this.mBackStack.get(index);
    }

    public void addOnBackStackChangedListener(@Nonnull FragmentManager.OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(listener);
    }

    public void removeOnBackStackChangedListener(@Nonnull FragmentManager.OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(listener);
        }
    }

    @Override
    public void setFragmentResult(@Nonnull String requestKey, @Nonnull DataSet result) {
        FragmentManager.LifecycleAwareResultListener resultListener = (FragmentManager.LifecycleAwareResultListener) this.mResultListeners.get(requestKey);
        if (resultListener != null && resultListener.isAtLeast(Lifecycle.State.STARTED)) {
            resultListener.onFragmentResult(requestKey, result);
        } else {
            this.mResults.put(requestKey, result);
        }
    }

    @Override
    public void clearFragmentResult(@Nonnull String requestKey) {
        this.mResults.remove(requestKey);
    }

    @Override
    public void setFragmentResultListener(@Nonnull String requestKey, @Nonnull LifecycleOwner lifecycleOwner, @Nonnull FragmentResultListener listener) {
        final Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        if (lifecycle.getCurrentState() != Lifecycle.State.DESTROYED) {
            LifecycleObserver observer = new LifecycleObserver() {

                @Override
                public void onStateChanged(@Nonnull LifecycleOwner source, @Nonnull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_START) {
                        DataSet storedResult = (DataSet) FragmentManager.this.mResults.get(requestKey);
                        if (storedResult != null) {
                            listener.onFragmentResult(requestKey, storedResult);
                            FragmentManager.this.clearFragmentResult(requestKey);
                        }
                    }
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        lifecycle.removeObserver(this);
                        FragmentManager.this.mResultListeners.remove(requestKey);
                    }
                }
            };
            lifecycle.addObserver(observer);
            FragmentManager.LifecycleAwareResultListener storedListener = (FragmentManager.LifecycleAwareResultListener) this.mResultListeners.put(requestKey, new FragmentManager.LifecycleAwareResultListener(lifecycle, listener, observer));
            if (storedListener != null) {
                storedListener.removeObserver();
            }
        }
    }

    @Override
    public void clearFragmentResultListener(@Nonnull String requestKey) {
        FragmentManager.LifecycleAwareResultListener listener = (FragmentManager.LifecycleAwareResultListener) this.mResultListeners.remove(requestKey);
        if (listener != null) {
            listener.removeObserver();
        }
    }

    public void putFragment(@Nonnull DataSet bundle, @Nonnull String key, @Nonnull Fragment fragment) {
        if (fragment.mFragmentManager != this) {
            this.throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putString(key, fragment.mWho);
    }

    @Nullable
    public Fragment getFragment(@Nonnull DataSet bundle, @Nonnull String key) {
        String who = bundle.getString(key);
        if (who == null) {
            return null;
        } else {
            Fragment f = this.findActiveFragment(who);
            if (f == null) {
                this.throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": unique id " + who));
            }
            return f;
        }
    }

    @Nonnull
    public static <F extends Fragment> F findFragment(@Nonnull View view) {
        Fragment fragment = findViewFragment(view);
        if (fragment == null) {
            throw new IllegalStateException("View " + view + " does not have a Fragment set");
        } else {
            return (F) fragment;
        }
    }

    @Nullable
    private static Fragment findViewFragment(@Nonnull View viewx) {
        do {
            Fragment fragment = getViewFragment(viewx);
            if (fragment != null) {
                return fragment;
            }
        } while (viewx.getParent() instanceof View viewx);
        return null;
    }

    @Nullable
    static Fragment getViewFragment(@Nonnull View view) {
        Object tag = view.getTag(33685505);
        return tag instanceof Fragment ? (Fragment) tag : null;
    }

    void onContainerAvailable(@Nonnull FragmentContainerView container) {
        for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
            Fragment fragment = fragmentStateManager.getFragment();
            if (fragment.mContainerId == container.getId() && fragment.mView != null && fragment.mView.getParent() == null) {
                fragment.mContainer = container;
                fragmentStateManager.addViewToContainer();
            }
        }
    }

    @Nonnull
    static FragmentManager findFragmentManager(@Nonnull View view) {
        Fragment fragment = findViewFragment(view);
        if (fragment != null) {
            if (!fragment.isAdded()) {
                throw new IllegalStateException("The Fragment " + fragment + " that owns View " + view + " has already been destroyed. Nested fragments should always use the child FragmentManager.");
            } else {
                return fragment.getChildFragmentManager();
            }
        } else {
            throw new IllegalStateException("View " + view + " is not associated with a Fragment");
        }
    }

    @Nonnull
    public List<Fragment> getFragments() {
        return this.mFragmentStore.getFragments();
    }

    @Nonnull
    ViewModelStore getViewModelStore(@Nonnull Fragment f) {
        return this.mViewModel.getViewModelStore(f);
    }

    @Nonnull
    private FragmentManagerViewModel getChildViewModel(@Nonnull Fragment f) {
        return this.mViewModel.getChildViewModel(f);
    }

    void addRetainedFragment(@Nonnull Fragment f) {
        this.mViewModel.addRetainedFragment(f);
    }

    void removeRetainedFragment(@Nonnull Fragment f) {
        this.mViewModel.removeRetainedFragment(f);
    }

    @Nonnull
    List<Fragment> getActiveFragments() {
        return this.mFragmentStore.getActiveFragments();
    }

    int getActiveFragmentCount() {
        return this.mFragmentStore.getActiveFragmentCount();
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    @Nonnull
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            Class<?> cls = this.mParent.getClass();
            sb.append(cls.getSimpleName());
            sb.append("{");
            sb.append(Integer.toHexString(System.identityHashCode(this.mParent)));
            sb.append("}");
        } else if (this.mHost != null) {
            Class<?> cls = this.mHost.getClass();
            sb.append(cls.getSimpleName());
            sb.append("{");
            sb.append(Integer.toHexString(System.identityHashCode(this.mHost)));
            sb.append("}");
        } else {
            sb.append("null");
        }
        sb.append("}}");
        return sb.toString();
    }

    public void dump(@Nonnull String prefix, @Nullable FileDescriptor fd, @Nonnull PrintWriter writer, @Nullable String... args) {
        String innerPrefix = prefix + "    ";
        this.mFragmentStore.dump(prefix, fd, writer, args);
        if (this.mCreatedMenus != null) {
            int count = this.mCreatedMenus.size();
            if (count > 0) {
                writer.print(prefix);
                writer.println("Fragments Created Menus:");
                for (int i = 0; i < count; i++) {
                    Fragment f = (Fragment) this.mCreatedMenus.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            int count = this.mBackStack.size();
            if (count > 0) {
                writer.print(prefix);
                writer.println("Back Stack:");
                for (int i = 0; i < count; i++) {
                    BackStackRecord bs = (BackStackRecord) this.mBackStack.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(bs.toString());
                    bs.dump(innerPrefix, writer);
                }
            }
        }
        writer.print(prefix);
        writer.println("Back Stack Index: " + this.mBackStackIndex.get());
        synchronized (this.mPendingActions) {
            int count = this.mPendingActions.size();
            if (count > 0) {
                writer.print(prefix);
                writer.println("Pending Actions:");
                for (int i = 0; i < count; i++) {
                    FragmentManager.OpGenerator r = (FragmentManager.OpGenerator) this.mPendingActions.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(r);
                }
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mHost=");
        writer.println(this.mHost);
        writer.print(prefix);
        writer.print("  mContainer=");
        writer.println(this.mContainer);
        if (this.mParent != null) {
            writer.print(prefix);
            writer.print("  mParent=");
            writer.println(this.mParent);
        }
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(this.mCurState);
        writer.print(" mStateSaved=");
        writer.print(this.mStateSaved);
        writer.print(" mStopped=");
        writer.print(this.mStopped);
        writer.print(" mDestroyed=");
        writer.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(this.mNeedMenuInvalidate);
        }
    }

    void performPendingDeferredStart(@Nonnull FragmentStateManager fragmentStateManager) {
        Fragment f = fragmentStateManager.getFragment();
        if (f.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            f.mDeferStart = false;
            fragmentStateManager.moveToExpectedState();
        }
    }

    boolean isStateAtLeast(int state) {
        return this.mCurState >= state;
    }

    void setExitAnimationOrder(@Nonnull Fragment f, boolean isPop) {
        ViewGroup container = this.getFragmentContainer(f);
        if (container != null && container instanceof FragmentContainerView) {
            ((FragmentContainerView) container).setDrawDisappearingViewsLast(!isPop);
        }
    }

    void moveToState(int newState, boolean always) {
        if (this.mHost == null && newState != -1) {
            throw new IllegalStateException("No activity");
        } else if (always || newState != this.mCurState) {
            this.mCurState = newState;
            this.mFragmentStore.moveToExpectedState();
            this.startPendingDeferredFragments();
            if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 7) {
                this.mNeedMenuInvalidate = false;
            }
        }
    }

    private void startPendingDeferredFragments() {
        for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
            this.performPendingDeferredStart(fragmentStateManager);
        }
    }

    @Nonnull
    FragmentStateManager createOrGetFragmentStateManager(@Nonnull Fragment f) {
        FragmentStateManager existing = this.mFragmentStore.getFragmentStateManager(f.mWho);
        if (existing != null) {
            return existing;
        } else {
            FragmentStateManager fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, f);
            fragmentStateManager.setFragmentManagerState(this.mCurState);
            return fragmentStateManager;
        }
    }

    @Nonnull
    FragmentStateManager addFragment(@Nonnull Fragment fragment) {
        FragmentStateManager fragmentStateManager = this.createOrGetFragmentStateManager(fragment);
        fragment.mFragmentManager = this;
        this.mFragmentStore.makeActive(fragmentStateManager);
        if (!fragment.mDetached) {
            this.mFragmentStore.addFragment(fragment);
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (this.isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
        }
        return fragmentStateManager;
    }

    void removeFragment(@Nonnull Fragment fragment) {
        boolean inactive = !fragment.isInBackStack();
        if (!fragment.mDetached || inactive) {
            this.mFragmentStore.removeFragment(fragment);
            if (this.isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mRemoving = true;
            this.setVisibleRemovingFragment(fragment);
        }
    }

    void hideFragment(@Nonnull Fragment fragment) {
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
            this.setVisibleRemovingFragment(fragment);
        }
    }

    void showFragment(@Nonnull Fragment fragment) {
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    void detachFragment(@Nonnull Fragment fragment) {
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                this.mFragmentStore.removeFragment(fragment);
                if (this.isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
                this.setVisibleRemovingFragment(fragment);
            }
        }
    }

    void attachFragment(@Nonnull Fragment fragment) {
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                this.mFragmentStore.addFragment(fragment);
                if (this.isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
            }
        }
    }

    @Nullable
    public Fragment findFragmentById(int id) {
        return this.mFragmentStore.findFragmentById(id);
    }

    @Nullable
    public Fragment findFragmentByTag(@Nullable String tag) {
        return this.mFragmentStore.findFragmentByTag(tag);
    }

    @Nullable
    Fragment findFragmentByWho(@Nonnull String who) {
        return this.mFragmentStore.findFragmentByWho(who);
    }

    @Nullable
    Fragment findActiveFragment(@Nonnull String who) {
        return this.mFragmentStore.findActiveFragment(who);
    }

    private void checkStateLoss() {
        if (this.isStateSaved()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
    }

    public boolean isStateSaved() {
        return this.mStateSaved || this.mStopped;
    }

    void enqueueAction(@Nonnull FragmentManager.OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            if (this.mHost == null) {
                if (this.mDestroyed) {
                    throw new IllegalStateException("FragmentManager has been destroyed");
                }
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
            this.checkStateLoss();
        }
        synchronized (this.mPendingActions) {
            if (this.mHost == null) {
                if (!allowStateLoss) {
                    throw new IllegalStateException("Activity has been destroyed");
                }
            } else {
                this.mPendingActions.add(action);
                this.scheduleCommit();
            }
        }
    }

    void scheduleCommit() {
        synchronized (this.mPendingActions) {
            boolean pendingReady = this.mPendingActions.size() == 1;
            if (pendingReady) {
                this.mHost.mHandler.removeCallbacks(this.mExecCommit);
                this.mHost.mHandler.post(this.mExecCommit);
                this.updateOnBackPressedCallbackEnabled();
            }
        }
    }

    int allocBackStackIndex() {
        return this.mBackStackIndex.getAndIncrement();
    }

    private void ensureExecReady(boolean allowStateLoss) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (this.mHost == null) {
            if (this.mDestroyed) {
                throw new IllegalStateException("FragmentManager has been destroyed");
            } else {
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
        } else if (!this.mHost.mHandler.isCurrentThread()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        } else {
            if (!allowStateLoss) {
                this.checkStateLoss();
            }
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new ArrayList();
                this.mTmpIsPop = new BooleanArrayList();
            }
        }
    }

    void execSingleAction(@Nonnull FragmentManager.OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss || this.mHost != null && !this.mDestroyed) {
            this.ensureExecReady(allowStateLoss);
            if (action.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
                this.mExecutingActions = true;
                try {
                    this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                } finally {
                    this.cleanupExec();
                }
            }
            this.updateOnBackPressedCallbackEnabled();
            this.doPendingDeferredStart();
            this.mFragmentStore.burpActive();
        }
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    boolean execPendingActions(boolean allowStateLoss) {
        this.ensureExecReady(allowStateLoss);
        boolean didSomething;
        for (didSomething = false; this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop); didSomething = true) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                this.cleanupExec();
            }
        }
        this.updateOnBackPressedCallbackEnabled();
        this.doPendingDeferredStart();
        this.mFragmentStore.burpActive();
        return didSomething;
    }

    private void removeRedundantOperationsAndExecute(@Nonnull ArrayList<BackStackRecord> records, @Nonnull BooleanArrayList isRecordPop) {
        if (!records.isEmpty()) {
            if (records.size() != isRecordPop.size()) {
                throw new IllegalStateException("Internal error with the back stack records");
            } else {
                int numRecords = records.size();
                int startIndex = 0;
                for (int recordNum = 0; recordNum < numRecords; recordNum++) {
                    boolean canReorder = ((BackStackRecord) records.get(recordNum)).mReorderingAllowed;
                    if (!canReorder) {
                        if (startIndex != recordNum) {
                            this.executeOpsTogether(records, isRecordPop, startIndex, recordNum);
                        }
                        int reorderingEnd = recordNum + 1;
                        if (isRecordPop.getBoolean(recordNum)) {
                            while (reorderingEnd < numRecords && isRecordPop.getBoolean(reorderingEnd) && !((BackStackRecord) records.get(reorderingEnd)).mReorderingAllowed) {
                                reorderingEnd++;
                            }
                        }
                        this.executeOpsTogether(records, isRecordPop, recordNum, reorderingEnd);
                        startIndex = reorderingEnd;
                        recordNum = reorderingEnd - 1;
                    }
                }
                if (startIndex != numRecords) {
                    this.executeOpsTogether(records, isRecordPop, startIndex, numRecords);
                }
            }
        }
    }

    private void executeOpsTogether(@Nonnull ArrayList<BackStackRecord> records, @Nonnull BooleanArrayList isRecordPop, int startIndex, int endIndex) {
        boolean allowReordering = ((BackStackRecord) records.get(startIndex)).mReorderingAllowed;
        boolean addToBackStack = false;
        if (this.mTmpAddedFragments == null) {
            this.mTmpAddedFragments = new ArrayList();
        } else {
            this.mTmpAddedFragments.clear();
        }
        this.mTmpAddedFragments.addAll(this.mFragmentStore.getFragments());
        Fragment oldPrimaryNav = this.getPrimaryNavigationFragment();
        for (int recordNum = startIndex; recordNum < endIndex; recordNum++) {
            BackStackRecord record = (BackStackRecord) records.get(recordNum);
            boolean isPop = isRecordPop.getBoolean(recordNum);
            if (!isPop) {
                oldPrimaryNav = record.expandOps(this.mTmpAddedFragments, oldPrimaryNav);
            } else {
                oldPrimaryNav = record.trackAddedFragmentsInPop(this.mTmpAddedFragments, oldPrimaryNav);
            }
            addToBackStack = addToBackStack || record.mAddToBackStack;
        }
        this.mTmpAddedFragments.clear();
        if (!allowReordering && this.mCurState >= 1) {
            for (int index = startIndex; index < endIndex; index++) {
                BackStackRecord record = (BackStackRecord) records.get(index);
                for (FragmentTransaction.Op op : record.mOps) {
                    Fragment fragment = op.mFragment;
                    if (fragment != null && fragment.mFragmentManager != null) {
                        FragmentStateManager fragmentStateManager = this.createOrGetFragmentStateManager(fragment);
                        this.mFragmentStore.makeActive(fragmentStateManager);
                    }
                }
            }
        }
        executeOps(records, isRecordPop, startIndex, endIndex);
        boolean isPop = isRecordPop.getBoolean(endIndex - 1);
        for (int index = startIndex; index < endIndex; index++) {
            BackStackRecord record = (BackStackRecord) records.get(index);
            if (isPop) {
                for (int opIndex = record.mOps.size() - 1; opIndex >= 0; opIndex--) {
                    FragmentTransaction.Op opx = (FragmentTransaction.Op) record.mOps.get(opIndex);
                    Fragment fragment = opx.mFragment;
                    if (fragment != null) {
                        FragmentStateManager fragmentStateManager = this.createOrGetFragmentStateManager(fragment);
                        fragmentStateManager.moveToExpectedState();
                    }
                }
            } else {
                for (FragmentTransaction.Op opx : record.mOps) {
                    Fragment fragment = opx.mFragment;
                    if (fragment != null) {
                        FragmentStateManager fragmentStateManager = this.createOrGetFragmentStateManager(fragment);
                        fragmentStateManager.moveToExpectedState();
                    }
                }
            }
        }
        this.moveToState(this.mCurState, true);
        for (SpecialEffectsController controller : this.collectChangedControllers(records, startIndex, endIndex)) {
            controller.updateOperationDirection(isPop);
            controller.markPostponedState();
            controller.executePendingOperations();
        }
        for (int recordNum = startIndex; recordNum < endIndex; recordNum++) {
            BackStackRecord record = (BackStackRecord) records.get(recordNum);
            isPop = isRecordPop.getBoolean(recordNum);
            if (isPop && record.mIndex >= 0) {
                record.mIndex = -1;
            }
            record.runOnCommitRunnables();
        }
        if (addToBackStack) {
            this.reportBackStackChanged();
        }
    }

    @Nonnull
    private Set<SpecialEffectsController> collectChangedControllers(@Nonnull ArrayList<BackStackRecord> records, int startIndex, int endIndex) {
        Set<SpecialEffectsController> controllers = new HashSet();
        for (int index = startIndex; index < endIndex; index++) {
            BackStackRecord record = (BackStackRecord) records.get(index);
            for (FragmentTransaction.Op op : record.mOps) {
                Fragment fragment = op.mFragment;
                if (fragment != null) {
                    ViewGroup container = fragment.mContainer;
                    if (container != null) {
                        controllers.add(SpecialEffectsController.getOrCreateController(container, this));
                    }
                }
            }
        }
        return controllers;
    }

    private static void executeOps(@Nonnull ArrayList<BackStackRecord> records, @Nonnull BooleanArrayList isRecordPop, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            BackStackRecord record = (BackStackRecord) records.get(i);
            boolean isPop = isRecordPop.getBoolean(i);
            if (isPop) {
                record.bumpBackStackNesting(-1);
                record.executePopOps();
            } else {
                record.bumpBackStackNesting(1);
                record.executeOps();
            }
        }
    }

    private void setVisibleRemovingFragment(@Nonnull Fragment f) {
        ViewGroup container = this.getFragmentContainer(f);
        if (container != null && f.getEnterAnim() + f.getExitAnim() + f.getPopEnterAnim() + f.getPopExitAnim() > 0) {
            if (container.getTag(33685506) == null) {
                container.setTag(33685506, f);
            }
            f.setPopDirection(f.getPopDirection());
        }
    }

    @Nullable
    private ViewGroup getFragmentContainer(@Nonnull Fragment f) {
        if (f.mContainer != null) {
            return f.mContainer;
        } else if (f.mContainerId <= 0) {
            return null;
        } else {
            if (this.mContainer.onHasView()) {
                View view = this.mContainer.onFindViewById(f.mContainerId);
                if (view instanceof ViewGroup) {
                    return (ViewGroup) view;
                }
            }
            return null;
        }
    }

    private void forcePostponedTransactions() {
        for (SpecialEffectsController controller : this.collectAllSpecialEffectsController()) {
            controller.forcePostponedExecutePendingOperations();
        }
    }

    private void endAnimatingAwayFragments() {
        for (SpecialEffectsController controller : this.collectAllSpecialEffectsController()) {
            controller.forceCompleteAllOperations();
        }
    }

    @Nonnull
    private Set<SpecialEffectsController> collectAllSpecialEffectsController() {
        Set<SpecialEffectsController> controllers = new HashSet();
        for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
            ViewGroup container = fragmentStateManager.getFragment().mContainer;
            if (container != null) {
                controllers.add(SpecialEffectsController.getOrCreateController(container, this.getSpecialEffectsControllerFactory()));
            }
        }
        return controllers;
    }

    private boolean generateOpsForPendingActions(@Nonnull ArrayList<BackStackRecord> records, @Nonnull BooleanArrayList isPop) {
        boolean didSomething = false;
        synchronized (this.mPendingActions) {
            if (this.mPendingActions.isEmpty()) {
                return false;
            } else {
                try {
                    for (FragmentManager.OpGenerator generator : this.mPendingActions) {
                        didSomething |= generator.generateOps(records, isPop);
                    }
                } finally {
                    this.mPendingActions.clear();
                    this.mHost.mHandler.removeCallbacks(this.mExecCommit);
                }
                return didSomething;
            }
        }
    }

    private void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            this.startPendingDeferredFragments();
        }
    }

    private void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                ((FragmentManager.OnBackStackChangedListener) this.mBackStackChangeListeners.get(i)).onBackStackChanged();
            }
        }
    }

    void addBackStackState(@Nonnull BackStackRecord state) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(state);
    }

    boolean popBackStackState(@Nonnull ArrayList<BackStackRecord> records, @Nonnull BooleanArrayList isRecordPop, @Nullable String name, int id, int flags) {
        int index = this.findBackStackIndex(name, id, (flags & 1) != 0);
        if (index < 0) {
            return false;
        } else {
            for (int i = this.mBackStack.size() - 1; i >= index; i--) {
                records.add((BackStackRecord) this.mBackStack.remove(i));
                isRecordPop.add(true);
            }
            return true;
        }
    }

    private int findBackStackIndex(@Nullable String name, int id, boolean inclusive) {
        if (this.mBackStack == null || this.mBackStack.isEmpty()) {
            return -1;
        } else if (name != null || id >= 0) {
            int index;
            for (index = this.mBackStack.size() - 1; index >= 0; index--) {
                BackStackRecord bss = (BackStackRecord) this.mBackStack.get(index);
                if (name != null && name.equals(bss.getName()) || id >= 0 && id == bss.mIndex) {
                    break;
                }
            }
            if (index < 0) {
                return index;
            } else {
                if (!inclusive) {
                    if (index == this.mBackStack.size() - 1) {
                        return -1;
                    }
                    index++;
                } else {
                    while (index > 0) {
                        BackStackRecord bss = (BackStackRecord) this.mBackStack.get(index - 1);
                        if ((name == null || !name.equals(bss.getName())) && (id < 0 || id != bss.mIndex)) {
                            break;
                        }
                        index--;
                    }
                }
                return index;
            }
        } else {
            return inclusive ? 0 : this.mBackStack.size() - 1;
        }
    }

    @Nonnull
    FragmentHostCallback<?> getHost() {
        return this.mHost;
    }

    @Nullable
    Fragment getParent() {
        return this.mParent;
    }

    @Nonnull
    FragmentContainer getContainer() {
        return this.mContainer;
    }

    @Nonnull
    FragmentStore getFragmentStore() {
        return this.mFragmentStore;
    }

    void attachController(@Nonnull FragmentHostCallback<?> host, @Nonnull FragmentContainer container, @Nullable Fragment parent) {
        if (this.mHost != null) {
            throw new IllegalStateException("Already attached");
        } else {
            this.mHost = host;
            this.mContainer = container;
            this.mParent = parent;
            if (host instanceof FragmentOnAttachListener) {
                this.addFragmentOnAttachListener((FragmentOnAttachListener) host);
            }
            if (this.mParent != null) {
                this.updateOnBackPressedCallbackEnabled();
            }
            if (host instanceof OnBackPressedDispatcherOwner dispatcherOwner) {
                this.mOnBackPressedDispatcher = dispatcherOwner.getOnBackPressedDispatcher();
                LifecycleOwner owner = (LifecycleOwner) (parent != null ? parent : dispatcherOwner);
                this.mOnBackPressedDispatcher.addCallback(owner, this.mOnBackPressedCallback);
            }
            if (parent != null) {
                this.mViewModel = parent.mFragmentManager.getChildViewModel(parent);
            } else {
                if (!(host instanceof ViewModelStoreOwner)) {
                    throw new IllegalStateException();
                }
                ViewModelStore viewModelStore = ((ViewModelStoreOwner) host).getViewModelStore();
                this.mViewModel = FragmentManagerViewModel.getInstance(viewModelStore);
            }
            this.mViewModel.setIsStateSaved(this.isStateSaved());
            this.mFragmentStore.setViewModel(this.mViewModel);
        }
    }

    void noteStateNotSaved() {
        if (this.mHost != null) {
            this.mStateSaved = false;
            this.mStopped = false;
            this.mViewModel.setIsStateSaved(false);
            for (Fragment fragment : this.mFragmentStore.getFragments()) {
                if (fragment != null) {
                    fragment.noteStateNotSaved();
                }
            }
        }
    }

    void dispatchAttach() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mViewModel.setIsStateSaved(false);
        this.dispatchStateChange(0);
    }

    void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mViewModel.setIsStateSaved(false);
        this.dispatchStateChange(1);
    }

    void dispatchViewCreated() {
        this.dispatchStateChange(2);
    }

    void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mViewModel.setIsStateSaved(false);
        this.dispatchStateChange(4);
    }

    void dispatchStart() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mViewModel.setIsStateSaved(false);
        this.dispatchStateChange(5);
    }

    void dispatchResume() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.mViewModel.setIsStateSaved(false);
        this.dispatchStateChange(7);
    }

    void dispatchPause() {
        this.dispatchStateChange(5);
    }

    void dispatchStop() {
        this.mStopped = true;
        this.mViewModel.setIsStateSaved(true);
        this.dispatchStateChange(4);
    }

    void dispatchDestroyView() {
        this.dispatchStateChange(1);
    }

    void dispatchDestroy() {
        this.mDestroyed = true;
        this.execPendingActions(true);
        this.endAnimatingAwayFragments();
        this.dispatchStateChange(-1);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
        if (this.mOnBackPressedDispatcher != null) {
            this.mOnBackPressedCallback.remove();
            this.mOnBackPressedDispatcher = null;
        }
    }

    private void dispatchStateChange(int nextState) {
        try {
            this.mExecutingActions = true;
            this.mFragmentStore.dispatchStateChange(nextState);
            this.moveToState(nextState, false);
            for (SpecialEffectsController controller : this.collectAllSpecialEffectsController()) {
                controller.forceCompleteAllOperations();
            }
        } finally {
            this.mExecutingActions = false;
        }
        this.execPendingActions(true);
    }

    void setPrimaryNavigationFragment(@Nullable Fragment f) {
        if (f == null || f.equals(this.findActiveFragment(f.mWho)) && (f.mHost == null || f.mFragmentManager == this)) {
            Fragment previousPrimaryNav = this.mPrimaryNav;
            this.mPrimaryNav = f;
            this.dispatchParentPrimaryNavigationFragmentChanged(previousPrimaryNav);
            this.dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
        } else {
            throw new IllegalArgumentException("Fragment " + f + " is not an active fragment of FragmentManager " + this);
        }
    }

    private void dispatchParentPrimaryNavigationFragmentChanged(@Nullable Fragment f) {
        if (f != null && f.equals(this.findActiveFragment(f.mWho))) {
            f.performPrimaryNavigationFragmentChanged();
        }
    }

    void dispatchPrimaryNavigationFragmentChanged() {
        this.updateOnBackPressedCallbackEnabled();
        this.dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
    }

    @Nullable
    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    void setMaxLifecycle(@Nonnull Fragment f, @Nonnull Lifecycle.State state) {
        if (f.equals(this.findActiveFragment(f.mWho)) && (f.mHost == null || f.mFragmentManager == this)) {
            f.mMaxState = state;
        } else {
            throw new IllegalArgumentException("Fragment " + f + " is not an active fragment of FragmentManager " + this);
        }
    }

    public void setFragmentFactory(@Nonnull FragmentFactory fragmentFactory) {
        this.mFragmentFactory = fragmentFactory;
    }

    @Nonnull
    public FragmentFactory getFragmentFactory() {
        if (this.mFragmentFactory != null) {
            return this.mFragmentFactory;
        } else {
            return this.mParent != null ? this.mParent.mFragmentManager.getFragmentFactory() : sHostFragmentFactory;
        }
    }

    void setSpecialEffectsControllerFactory(@Nonnull SpecialEffectsControllerFactory specialEffectsControllerFactory) {
        this.mSpecialEffectsControllerFactory = specialEffectsControllerFactory;
    }

    @Nonnull
    SpecialEffectsControllerFactory getSpecialEffectsControllerFactory() {
        if (this.mSpecialEffectsControllerFactory != null) {
            return this.mSpecialEffectsControllerFactory;
        } else {
            return this.mParent != null ? this.mParent.mFragmentManager.getSpecialEffectsControllerFactory() : sDefaultSpecialEffectsControllerFactory;
        }
    }

    @Nonnull
    FragmentLifecycleCallbacksDispatcher getLifecycleCallbacksDispatcher() {
        return this.mLifecycleCallbacksDispatcher;
    }

    public void registerFragmentLifecycleCallbacks(@Nonnull FragmentLifecycleCallbacks cb, boolean recursive) {
        this.mLifecycleCallbacksDispatcher.registerFragmentLifecycleCallbacks(cb, recursive);
    }

    public void unregisterFragmentLifecycleCallbacks(@Nonnull FragmentLifecycleCallbacks cb) {
        this.mLifecycleCallbacksDispatcher.unregisterFragmentLifecycleCallbacks(cb);
    }

    public void addFragmentOnAttachListener(@Nonnull FragmentOnAttachListener listener) {
        this.mOnAttachListeners.add(listener);
    }

    void dispatchOnAttachFragment(@Nonnull Fragment fragment) {
        for (FragmentOnAttachListener listener : this.mOnAttachListeners) {
            listener.onAttachFragment(this, fragment);
        }
    }

    public void removeFragmentOnAttachListener(@Nonnull FragmentOnAttachListener listener) {
        this.mOnAttachListeners.remove(listener);
    }

    void dispatchOnHiddenChanged() {
        for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
            if (fragment != null) {
                fragment.onHiddenChanged(fragment.isHidden());
                fragment.mChildFragmentManager.dispatchOnHiddenChanged();
            }
        }
    }

    boolean checkForMenus() {
        boolean hasMenu = false;
        for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
            if (fragment != null) {
                hasMenu = this.isMenuAvailable(fragment);
            }
            if (hasMenu) {
                return true;
            }
        }
        return false;
    }

    private boolean isMenuAvailable(@Nonnull Fragment f) {
        return f.mHasMenu && f.mMenuVisible || f.mChildFragmentManager.checkForMenus();
    }

    void invalidateMenuForFragment(@Nonnull Fragment f) {
        if (f.mAdded && this.isMenuAvailable(f)) {
            this.mNeedMenuInvalidate = true;
        }
    }

    static int reverseTransit(int transit) {
        return switch(transit) {
            case 4097 ->
                8194;
            case 4099 ->
                4099;
            case 4100 ->
                8197;
            case 8194 ->
                4097;
            case 8197 ->
                4100;
            default ->
                0;
        };
    }

    public interface BackStackEntry {

        int getId();

        @Nullable
        String getName();
    }

    private static class LifecycleAwareResultListener implements FragmentResultListener {

        private final Lifecycle mLifecycle;

        private final FragmentResultListener mListener;

        private final LifecycleObserver mObserver;

        LifecycleAwareResultListener(@Nonnull Lifecycle lifecycle, @Nonnull FragmentResultListener listener, @Nonnull LifecycleObserver observer) {
            this.mLifecycle = lifecycle;
            this.mListener = listener;
            this.mObserver = observer;
        }

        public boolean isAtLeast(Lifecycle.State state) {
            return this.mLifecycle.getCurrentState().isAtLeast(state);
        }

        @Override
        public void onFragmentResult(@Nonnull String requestKey, @Nonnull DataSet result) {
            this.mListener.onFragmentResult(requestKey, result);
        }

        public void removeObserver() {
            this.mLifecycle.removeObserver(this.mObserver);
        }
    }

    @FunctionalInterface
    public interface OnBackStackChangedListener {

        @UiThread
        void onBackStackChanged();
    }

    interface OpGenerator {

        boolean generateOps(@Nonnull ArrayList<BackStackRecord> var1, @Nonnull BooleanArrayList var2);
    }

    private class PopBackStackState implements FragmentManager.OpGenerator {

        final String mName;

        final int mId;

        final int mFlags;

        PopBackStackState(@Nullable String name, int id, int flags) {
            this.mName = name;
            this.mId = id;
            this.mFlags = flags;
        }

        @Override
        public boolean generateOps(@Nonnull ArrayList<BackStackRecord> records, @Nonnull BooleanArrayList isRecordPop) {
            if (FragmentManager.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
                FragmentManager childManager = FragmentManager.this.mPrimaryNav.getChildFragmentManager();
                if (childManager.popBackStackImmediate()) {
                    return false;
                }
            }
            return FragmentManager.this.popBackStackState(records, isRecordPop, this.mName, this.mId, this.mFlags);
        }
    }
}