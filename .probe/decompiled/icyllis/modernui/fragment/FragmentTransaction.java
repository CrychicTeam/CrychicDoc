package icyllis.modernui.fragment;

import icyllis.modernui.lifecycle.Lifecycle;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class FragmentTransaction {

    static final int OP_NULL = 0;

    static final int OP_ADD = 1;

    static final int OP_REPLACE = 2;

    static final int OP_REMOVE = 3;

    static final int OP_HIDE = 4;

    static final int OP_SHOW = 5;

    static final int OP_DETACH = 6;

    static final int OP_ATTACH = 7;

    static final int OP_SET_PRIMARY_NAV = 8;

    static final int OP_UNSET_PRIMARY_NAV = 9;

    static final int OP_SET_MAX_LIFECYCLE = 10;

    private final FragmentFactory mFragmentFactory;

    ArrayList<FragmentTransaction.Op> mOps = new ArrayList();

    int mEnterAnim;

    int mExitAnim;

    int mPopEnterAnim;

    int mPopExitAnim;

    int mTransition;

    boolean mAddToBackStack;

    boolean mAllowAddToBackStack = true;

    @Nullable
    String mName;

    ArrayList<String> mSharedElementSourceNames;

    ArrayList<String> mSharedElementTargetNames;

    boolean mReorderingAllowed = false;

    ArrayList<Runnable> mCommitRunnables;

    public static final int TRANSIT_ENTER_MASK = 4096;

    public static final int TRANSIT_EXIT_MASK = 8192;

    public static final int TRANSIT_UNSET = -1;

    public static final int TRANSIT_NONE = 0;

    public static final int TRANSIT_FRAGMENT_OPEN = 4097;

    public static final int TRANSIT_FRAGMENT_CLOSE = 8194;

    public static final int TRANSIT_FRAGMENT_FADE = 4099;

    public static final int TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN = 4100;

    public static final int TRANSIT_FRAGMENT_MATCH_ACTIVITY_CLOSE = 8197;

    FragmentTransaction(@Nonnull FragmentFactory fragmentFactory) {
        this.mFragmentFactory = fragmentFactory;
    }

    void addOp(FragmentTransaction.Op op) {
        this.mOps.add(op);
        op.mEnterAnim = this.mEnterAnim;
        op.mExitAnim = this.mExitAnim;
        op.mPopEnterAnim = this.mPopEnterAnim;
        op.mPopExitAnim = this.mPopExitAnim;
    }

    @Nonnull
    private Fragment createFragment(@Nonnull Class<? extends Fragment> fragmentClass, @Nullable DataSet args) {
        Fragment fragment = this.mFragmentFactory.instantiate(fragmentClass);
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nonnull
    public final FragmentTransaction add(@Nonnull Class<? extends Fragment> fragmentClass, @Nullable DataSet args, @Nullable String tag) {
        return this.add(this.createFragment(fragmentClass, args), tag);
    }

    @Nonnull
    public FragmentTransaction add(@Nonnull Fragment fragment, @Nullable String tag) {
        this.doAddOp(0, fragment, tag, 1);
        return this;
    }

    @Nonnull
    public final FragmentTransaction add(int containerViewId, @Nonnull Class<? extends Fragment> fragmentClass, @Nullable DataSet args) {
        return this.add(containerViewId, this.createFragment(fragmentClass, args));
    }

    @Nonnull
    public FragmentTransaction add(int containerViewId, @Nonnull Fragment fragment) {
        this.doAddOp(containerViewId, fragment, null, 1);
        return this;
    }

    @Nonnull
    public final FragmentTransaction add(int containerViewId, @Nonnull Class<? extends Fragment> fragmentClass, @Nullable DataSet args, @Nullable String tag) {
        return this.add(containerViewId, this.createFragment(fragmentClass, args), tag);
    }

    @Nonnull
    public FragmentTransaction add(int containerViewId, @Nonnull Fragment fragment, @Nullable String tag) {
        this.doAddOp(containerViewId, fragment, tag, 1);
        return this;
    }

    FragmentTransaction add(@Nonnull ViewGroup container, @Nonnull Fragment fragment, @Nullable String tag) {
        fragment.mContainer = container;
        return this.add(container.getId(), fragment, tag);
    }

    void doAddOp(int containerViewId, @Nonnull Fragment fragment, @Nullable String tag, int cmd) {
        Class<?> fragmentClass = fragment.getClass();
        int modifiers = fragmentClass.getModifiers();
        if (!fragmentClass.isAnonymousClass() && Modifier.isPublic(modifiers) && (!fragmentClass.isMemberClass() || Modifier.isStatic(modifiers))) {
            if (tag != null) {
                if (fragment.mTag != null && !tag.equals(fragment.mTag)) {
                    throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.mTag + " now " + tag);
                }
                fragment.mTag = tag;
            }
            if (containerViewId != 0) {
                if (containerViewId == -1) {
                    throw new IllegalArgumentException("Can't add fragment " + fragment + " with tag " + tag + " to container view with no id");
                }
                if (fragment.mFragmentId != 0 && fragment.mFragmentId != containerViewId) {
                    throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.mFragmentId + " now " + containerViewId);
                }
                fragment.mContainerId = fragment.mFragmentId = containerViewId;
            }
            this.addOp(new FragmentTransaction.Op(cmd, fragment));
        } else {
            throw new IllegalStateException("Fragment " + fragmentClass.getCanonicalName() + " must be a public static class to be  properly recreated from instance state.");
        }
    }

    @Nonnull
    public final FragmentTransaction replace(int containerViewId, @Nonnull Class<? extends Fragment> fragmentClass, @Nullable DataSet args) {
        return this.replace(containerViewId, fragmentClass, args, null);
    }

    @Nonnull
    public FragmentTransaction replace(int containerViewId, @Nonnull Fragment fragment) {
        return this.replace(containerViewId, fragment, null);
    }

    @Nonnull
    public final FragmentTransaction replace(int containerViewId, @Nonnull Class<? extends Fragment> fragmentClass, @Nullable DataSet args, @Nullable String tag) {
        return this.replace(containerViewId, this.createFragment(fragmentClass, args), tag);
    }

    @Nonnull
    public FragmentTransaction replace(int containerViewId, @Nonnull Fragment fragment, @Nullable String tag) {
        if (containerViewId == 0) {
            throw new IllegalArgumentException("Must use non-zero containerViewId");
        } else {
            this.doAddOp(containerViewId, fragment, tag, 2);
            return this;
        }
    }

    @Nonnull
    public FragmentTransaction remove(@Nonnull Fragment fragment) {
        this.addOp(new FragmentTransaction.Op(3, fragment));
        return this;
    }

    @Nonnull
    public FragmentTransaction hide(@Nonnull Fragment fragment) {
        this.addOp(new FragmentTransaction.Op(4, fragment));
        return this;
    }

    @Nonnull
    public FragmentTransaction show(@Nonnull Fragment fragment) {
        this.addOp(new FragmentTransaction.Op(5, fragment));
        return this;
    }

    @Nonnull
    public FragmentTransaction detach(@Nonnull Fragment fragment) {
        this.addOp(new FragmentTransaction.Op(6, fragment));
        return this;
    }

    @Nonnull
    public FragmentTransaction attach(@Nonnull Fragment fragment) {
        this.addOp(new FragmentTransaction.Op(7, fragment));
        return this;
    }

    @Nonnull
    public FragmentTransaction setPrimaryNavigationFragment(@Nullable Fragment fragment) {
        this.addOp(new FragmentTransaction.Op(8, fragment));
        return this;
    }

    @Nonnull
    public FragmentTransaction setMaxLifecycle(@Nonnull Fragment fragment, @Nonnull Lifecycle.State state) {
        this.addOp(new FragmentTransaction.Op(10, fragment, state));
        return this;
    }

    public boolean isEmpty() {
        return this.mOps.isEmpty();
    }

    @Nonnull
    public FragmentTransaction setCustomAnimations(int enter, int exit) {
        return this.setCustomAnimations(enter, exit, 0, 0);
    }

    @Nonnull
    public FragmentTransaction setCustomAnimations(int enter, int exit, int popEnter, int popExit) {
        this.mEnterAnim = enter;
        this.mExitAnim = exit;
        this.mPopEnterAnim = popEnter;
        this.mPopExitAnim = popExit;
        return this;
    }

    @Nonnull
    public FragmentTransaction addSharedElement(@Nonnull View sharedElement, @Nonnull String name) {
        String transitionName = sharedElement.getTransitionName();
        if (transitionName == null) {
            throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
        } else {
            if (this.mSharedElementSourceNames == null) {
                this.mSharedElementSourceNames = new ArrayList();
                this.mSharedElementTargetNames = new ArrayList();
            } else {
                if (this.mSharedElementTargetNames.contains(name)) {
                    throw new IllegalArgumentException("A shared element with the target name '" + name + "' has already been added to the transaction.");
                }
                if (this.mSharedElementSourceNames.contains(transitionName)) {
                    throw new IllegalArgumentException("A shared element with the source name '" + transitionName + "' has already been added to the transaction.");
                }
            }
            this.mSharedElementSourceNames.add(transitionName);
            this.mSharedElementTargetNames.add(name);
            return this;
        }
    }

    @Nonnull
    public FragmentTransaction setTransition(int transition) {
        this.mTransition = transition;
        return this;
    }

    @Nonnull
    public FragmentTransaction addToBackStack(@Nullable String name) {
        if (!this.mAllowAddToBackStack) {
            throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
        } else {
            this.mAddToBackStack = true;
            this.mName = name;
            return this;
        }
    }

    public boolean isAddToBackStackAllowed() {
        return this.mAllowAddToBackStack;
    }

    @Nonnull
    public FragmentTransaction disallowAddToBackStack() {
        if (this.mAddToBackStack) {
            throw new IllegalStateException("This transaction is already being added to the back stack");
        } else {
            this.mAllowAddToBackStack = false;
            return this;
        }
    }

    @Nonnull
    public FragmentTransaction setReorderingAllowed(boolean reorderingAllowed) {
        this.mReorderingAllowed = reorderingAllowed;
        return this;
    }

    @Nonnull
    public FragmentTransaction runOnCommit(@Nonnull Runnable runnable) {
        this.disallowAddToBackStack();
        if (this.mCommitRunnables == null) {
            this.mCommitRunnables = new ArrayList();
        }
        this.mCommitRunnables.add(runnable);
        return this;
    }

    public abstract int commit();

    public abstract int commitAllowingStateLoss();

    public abstract void commitNow();

    public abstract void commitNowAllowingStateLoss();

    static final class Op {

        int mCmd;

        Fragment mFragment;

        boolean mFromExpandedOp;

        int mEnterAnim;

        int mExitAnim;

        int mPopEnterAnim;

        int mPopExitAnim;

        Lifecycle.State mOldMaxState;

        Lifecycle.State mCurrentMaxState;

        Op() {
        }

        Op(int cmd, Fragment fragment) {
            this.mCmd = cmd;
            this.mFragment = fragment;
            this.mFromExpandedOp = false;
            this.mOldMaxState = Lifecycle.State.RESUMED;
            this.mCurrentMaxState = Lifecycle.State.RESUMED;
        }

        Op(int cmd, Fragment fragment, boolean fromExpandedOp) {
            this.mCmd = cmd;
            this.mFragment = fragment;
            this.mFromExpandedOp = fromExpandedOp;
            this.mOldMaxState = Lifecycle.State.RESUMED;
            this.mCurrentMaxState = Lifecycle.State.RESUMED;
        }

        Op(int cmd, @Nonnull Fragment fragment, Lifecycle.State state) {
            this.mCmd = cmd;
            this.mFragment = fragment;
            this.mFromExpandedOp = false;
            this.mOldMaxState = fragment.mMaxState;
            this.mCurrentMaxState = state;
        }

        Op(@Nonnull FragmentTransaction.Op op) {
            this.mCmd = op.mCmd;
            this.mFragment = op.mFragment;
            this.mFromExpandedOp = op.mFromExpandedOp;
            this.mEnterAnim = op.mEnterAnim;
            this.mExitAnim = op.mExitAnim;
            this.mPopEnterAnim = op.mPopEnterAnim;
            this.mPopExitAnim = op.mPopExitAnim;
            this.mOldMaxState = op.mOldMaxState;
            this.mCurrentMaxState = op.mCurrentMaxState;
        }
    }
}