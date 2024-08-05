package icyllis.modernui.fragment;

import icyllis.modernui.lifecycle.Lifecycle;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class BackStackRecord extends FragmentTransaction implements FragmentManager.BackStackEntry, FragmentManager.OpGenerator {

    final FragmentManager mManager;

    boolean mCommitted;

    int mIndex = -1;

    boolean mBeingSaved = false;

    BackStackRecord(@Nonnull FragmentManager manager) {
        super(manager.getFragmentFactory());
        this.mManager = manager;
    }

    @Nonnull
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("BackStackEntry{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mName != null) {
            sb.append(" ");
            sb.append(this.mName);
        }
        sb.append("}");
        return sb.toString();
    }

    public void dump(String prefix, PrintWriter writer) {
        this.dump(prefix, writer, true);
    }

    public void dump(String prefix, PrintWriter writer, boolean full) {
        if (full) {
            writer.print(prefix);
            writer.print("mName=");
            writer.print(this.mName);
            writer.print(" mIndex=");
            writer.print(this.mIndex);
            writer.print(" mCommitted=");
            writer.println(this.mCommitted);
            if (this.mTransition != 0) {
                writer.print(prefix);
                writer.print("mTransition=#");
                writer.print(Integer.toHexString(this.mTransition));
            }
            if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
                writer.print(prefix);
                writer.print("mEnterAnim=#");
                writer.print(Integer.toHexString(this.mEnterAnim));
                writer.print(" mExitAnim=#");
                writer.println(Integer.toHexString(this.mExitAnim));
            }
            if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
                writer.print(prefix);
                writer.print("mPopEnterAnim=#");
                writer.print(Integer.toHexString(this.mPopEnterAnim));
                writer.print(" mPopExitAnim=#");
                writer.println(Integer.toHexString(this.mPopExitAnim));
            }
        }
        if (!this.mOps.isEmpty()) {
            writer.print(prefix);
            writer.println("Operations:");
            int numOps = this.mOps.size();
            for (int opNum = 0; opNum < numOps; opNum++) {
                FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(opNum);
                String cmdStr = switch(op.mCmd) {
                    case 0 ->
                        "NULL";
                    case 1 ->
                        "ADD";
                    case 2 ->
                        "REPLACE";
                    case 3 ->
                        "REMOVE";
                    case 4 ->
                        "HIDE";
                    case 5 ->
                        "SHOW";
                    case 6 ->
                        "DETACH";
                    case 7 ->
                        "ATTACH";
                    case 8 ->
                        "SET_PRIMARY_NAV";
                    case 9 ->
                        "UNSET_PRIMARY_NAV";
                    case 10 ->
                        "OP_SET_MAX_LIFECYCLE";
                    default ->
                        "cmd=" + op.mCmd;
                };
                writer.print(prefix);
                writer.print("  Op #");
                writer.print(opNum);
                writer.print(": ");
                writer.print(cmdStr);
                writer.print(" ");
                writer.println(op.mFragment);
                if (full) {
                    if (op.mEnterAnim != 0 || op.mExitAnim != 0) {
                        writer.print(prefix);
                        writer.print("enterAnim=#");
                        writer.print(Integer.toHexString(op.mEnterAnim));
                        writer.print(" exitAnim=#");
                        writer.println(Integer.toHexString(op.mExitAnim));
                    }
                    if (op.mPopEnterAnim != 0 || op.mPopExitAnim != 0) {
                        writer.print(prefix);
                        writer.print("popEnterAnim=#");
                        writer.print(Integer.toHexString(op.mPopEnterAnim));
                        writer.print(" popExitAnim=#");
                        writer.println(Integer.toHexString(op.mPopExitAnim));
                    }
                }
            }
        }
    }

    @Override
    public int getId() {
        return this.mIndex;
    }

    @Nullable
    @Override
    public String getName() {
        return this.mName;
    }

    @Override
    void doAddOp(int containerViewId, @Nonnull Fragment fragment, @Nullable String tag, int cmd) {
        super.doAddOp(containerViewId, fragment, tag, cmd);
        fragment.mFragmentManager = this.mManager;
    }

    @Nonnull
    @Override
    public FragmentTransaction remove(@Nonnull Fragment fragment) {
        if (fragment.mFragmentManager != null && fragment.mFragmentManager != this.mManager) {
            throw new IllegalStateException("Cannot remove Fragment attached to a different FragmentManager. Fragment " + fragment + " is already attached to a FragmentManager.");
        } else {
            return super.remove(fragment);
        }
    }

    @Nonnull
    @Override
    public FragmentTransaction hide(@Nonnull Fragment fragment) {
        if (fragment.mFragmentManager != null && fragment.mFragmentManager != this.mManager) {
            throw new IllegalStateException("Cannot hide Fragment attached to a different FragmentManager. Fragment " + fragment + " is already attached to a FragmentManager.");
        } else {
            return super.hide(fragment);
        }
    }

    @Nonnull
    @Override
    public FragmentTransaction show(@Nonnull Fragment fragment) {
        if (fragment.mFragmentManager != null && fragment.mFragmentManager != this.mManager) {
            throw new IllegalStateException("Cannot show Fragment attached to a different FragmentManager. Fragment " + fragment + " is already attached to a FragmentManager.");
        } else {
            return super.show(fragment);
        }
    }

    @Nonnull
    @Override
    public FragmentTransaction detach(@Nonnull Fragment fragment) {
        if (fragment.mFragmentManager != null && fragment.mFragmentManager != this.mManager) {
            throw new IllegalStateException("Cannot detach Fragment attached to a different FragmentManager. Fragment " + fragment + " is already attached to a FragmentManager.");
        } else {
            return super.detach(fragment);
        }
    }

    @Nonnull
    @Override
    public FragmentTransaction setPrimaryNavigationFragment(@Nullable Fragment fragment) {
        if (fragment != null && fragment.mFragmentManager != null && fragment.mFragmentManager != this.mManager) {
            throw new IllegalStateException("Cannot setPrimaryNavigation for Fragment attached to a different FragmentManager. Fragment " + fragment + " is already attached to a FragmentManager.");
        } else {
            return super.setPrimaryNavigationFragment(fragment);
        }
    }

    @Nonnull
    @Override
    public FragmentTransaction setMaxLifecycle(@Nonnull Fragment fragment, @Nonnull Lifecycle.State state) {
        if (fragment.mFragmentManager != this.mManager) {
            throw new IllegalArgumentException("Cannot setMaxLifecycle for Fragment not attached to FragmentManager " + this.mManager);
        } else if (state == Lifecycle.State.INITIALIZED && fragment.mState > -1) {
            throw new IllegalArgumentException("Cannot set maximum Lifecycle to " + state + " after the Fragment has been created");
        } else if (state == Lifecycle.State.DESTROYED) {
            throw new IllegalArgumentException("Cannot set maximum Lifecycle to " + state + ". Use remove() to remove the fragment from the FragmentManager and trigger its destruction.");
        } else {
            return super.setMaxLifecycle(fragment, state);
        }
    }

    void bumpBackStackNesting(int amt) {
        if (this.mAddToBackStack) {
            for (FragmentTransaction.Op op : this.mOps) {
                if (op.mFragment != null) {
                    op.mFragment.mBackStackNesting += amt;
                }
            }
        }
    }

    public void runOnCommitRunnables() {
        if (this.mCommitRunnables != null) {
            for (Runnable runnable : this.mCommitRunnables) {
                runnable.run();
            }
            this.mCommitRunnables = null;
        }
    }

    @Override
    public int commit() {
        return this.commitInternal(false);
    }

    @Override
    public int commitAllowingStateLoss() {
        return this.commitInternal(true);
    }

    @Override
    public void commitNow() {
        this.disallowAddToBackStack();
        this.mManager.execSingleAction(this, false);
    }

    @Override
    public void commitNowAllowingStateLoss() {
        this.disallowAddToBackStack();
        this.mManager.execSingleAction(this, true);
    }

    int commitInternal(boolean allowStateLoss) {
        if (this.mCommitted) {
            throw new IllegalStateException("commit already called");
        } else {
            this.mCommitted = true;
            if (this.mAddToBackStack) {
                this.mIndex = this.mManager.allocBackStackIndex();
            } else {
                this.mIndex = -1;
            }
            this.mManager.enqueueAction(this, allowStateLoss);
            return this.mIndex;
        }
    }

    @Override
    public boolean generateOps(@Nonnull ArrayList<BackStackRecord> records, @Nonnull BooleanArrayList isRecordPop) {
        records.add(this);
        isRecordPop.add(false);
        if (this.mAddToBackStack) {
            this.mManager.addBackStackState(this);
        }
        return true;
    }

    void executeOps() {
        for (FragmentTransaction.Op op : this.mOps) {
            Fragment f = op.mFragment;
            if (f != null) {
                f.mBeingSaved = this.mBeingSaved;
                f.setPopDirection(false);
                f.setNextTransition(this.mTransition);
                f.setSharedElementNames(this.mSharedElementSourceNames, this.mSharedElementTargetNames);
            }
            switch(op.mCmd) {
                case 1:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(f, false);
                    this.mManager.addFragment(f);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + op.mCmd);
                case 3:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.removeFragment(f);
                    break;
                case 4:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.hideFragment(f);
                    break;
                case 5:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(f, false);
                    this.mManager.showFragment(f);
                    break;
                case 6:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.detachFragment(f);
                    break;
                case 7:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(f, false);
                    this.mManager.attachFragment(f);
                    break;
                case 8:
                    this.mManager.setPrimaryNavigationFragment(f);
                    break;
                case 9:
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
                case 10:
                    assert f != null;
                    this.mManager.setMaxLifecycle(f, op.mCurrentMaxState);
            }
        }
    }

    void executePopOps() {
        for (int opNum = this.mOps.size() - 1; opNum >= 0; opNum--) {
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(opNum);
            Fragment f = op.mFragment;
            if (f != null) {
                f.mBeingSaved = this.mBeingSaved;
                f.setPopDirection(true);
                f.setNextTransition(FragmentManager.reverseTransit(this.mTransition));
                f.setSharedElementNames(this.mSharedElementTargetNames, this.mSharedElementSourceNames);
            }
            switch(op.mCmd) {
                case 1:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(f, true);
                    this.mManager.removeFragment(f);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + op.mCmd);
                case 3:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.addFragment(f);
                    break;
                case 4:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.showFragment(f);
                    break;
                case 5:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(f, true);
                    this.mManager.hideFragment(f);
                    break;
                case 6:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.attachFragment(f);
                    break;
                case 7:
                    assert f != null;
                    f.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(f, true);
                    this.mManager.detachFragment(f);
                    break;
                case 8:
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
                case 9:
                    this.mManager.setPrimaryNavigationFragment(f);
                    break;
                case 10:
                    assert f != null;
                    this.mManager.setMaxLifecycle(f, op.mOldMaxState);
            }
        }
    }

    Fragment expandOps(ArrayList<Fragment> added, Fragment oldPrimaryNav) {
        for (int opNum = 0; opNum < this.mOps.size(); opNum++) {
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(opNum);
            switch(op.mCmd) {
                case 1:
                case 7:
                    added.add(op.mFragment);
                    break;
                case 2:
                    Fragment f = op.mFragment;
                    int containerId = f.mContainerId;
                    boolean alreadyAdded = false;
                    int i = added.size() - 1;
                    for (; i >= 0; i--) {
                        Fragment old = (Fragment) added.get(i);
                        if (old.mContainerId == containerId) {
                            if (old == f) {
                                alreadyAdded = true;
                            } else {
                                if (old == oldPrimaryNav) {
                                    this.mOps.add(opNum, new FragmentTransaction.Op(9, old, true));
                                    opNum++;
                                    oldPrimaryNav = null;
                                }
                                FragmentTransaction.Op removeOp = new FragmentTransaction.Op(3, old, true);
                                removeOp.mEnterAnim = op.mEnterAnim;
                                removeOp.mPopEnterAnim = op.mPopEnterAnim;
                                removeOp.mExitAnim = op.mExitAnim;
                                removeOp.mPopExitAnim = op.mPopExitAnim;
                                this.mOps.add(opNum, removeOp);
                                added.remove(old);
                                opNum++;
                            }
                        }
                    }
                    if (alreadyAdded) {
                        this.mOps.remove(opNum);
                        opNum--;
                    } else {
                        op.mCmd = 1;
                        op.mFromExpandedOp = true;
                        added.add(f);
                    }
                    break;
                case 3:
                case 6:
                    added.remove(op.mFragment);
                    if (op.mFragment == oldPrimaryNav) {
                        this.mOps.add(opNum, new FragmentTransaction.Op(9, op.mFragment));
                        opNum++;
                        oldPrimaryNav = null;
                    }
                case 4:
                case 5:
                default:
                    break;
                case 8:
                    this.mOps.add(opNum, new FragmentTransaction.Op(9, oldPrimaryNav, true));
                    op.mFromExpandedOp = true;
                    opNum++;
                    oldPrimaryNav = op.mFragment;
            }
        }
        return oldPrimaryNav;
    }

    Fragment trackAddedFragmentsInPop(ArrayList<Fragment> added, Fragment oldPrimaryNav) {
        for (int opNum = this.mOps.size() - 1; opNum >= 0; opNum--) {
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(opNum);
            switch(op.mCmd) {
                case 1:
                case 7:
                    added.remove(op.mFragment);
                case 2:
                case 4:
                case 5:
                default:
                    break;
                case 3:
                case 6:
                    added.add(op.mFragment);
                    break;
                case 8:
                    oldPrimaryNav = null;
                    break;
                case 9:
                    oldPrimaryNav = op.mFragment;
                    break;
                case 10:
                    op.mCurrentMaxState = op.mOldMaxState;
            }
        }
        return oldPrimaryNav;
    }

    void collapseOps() {
        for (int opNum = this.mOps.size() - 1; opNum >= 0; opNum--) {
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(opNum);
            if (op.mFromExpandedOp) {
                if (op.mCmd == 8) {
                    op.mFromExpandedOp = false;
                    this.mOps.remove(opNum - 1);
                    opNum--;
                } else {
                    int containerId = op.mFragment.mContainerId;
                    op.mCmd = 2;
                    op.mFromExpandedOp = false;
                    for (int replaceOpNum = opNum - 1; replaceOpNum >= 0; replaceOpNum--) {
                        FragmentTransaction.Op potentialReplaceOp = (FragmentTransaction.Op) this.mOps.get(replaceOpNum);
                        if (potentialReplaceOp.mFromExpandedOp && potentialReplaceOp.mFragment.mContainerId == containerId) {
                            this.mOps.remove(replaceOpNum);
                            opNum--;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return this.mOps.isEmpty();
    }
}