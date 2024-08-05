package icyllis.modernui.fragment;

import icyllis.modernui.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Marker;

final class FragmentStore {

    private static final Marker MARKER = FragmentManager.MARKER;

    private final ArrayList<Fragment> mAdded = new ArrayList();

    private final HashMap<String, FragmentStateManager> mActive = new HashMap();

    private FragmentManagerViewModel mViewModel;

    void setViewModel(@Nonnull FragmentManagerViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    FragmentManagerViewModel getViewModel() {
        return this.mViewModel;
    }

    void resetActiveFragments() {
        this.mActive.clear();
    }

    void restoreAddedFragments(@Nullable List<String> added) {
        this.mAdded.clear();
        if (added != null) {
            for (String who : added) {
                Fragment f = this.findActiveFragment(who);
                if (f == null) {
                    throw new IllegalStateException("No instantiated fragment for (" + who + ")");
                }
                this.addFragment(f);
            }
        }
    }

    void makeActive(@Nonnull FragmentStateManager active) {
        Fragment f = active.getFragment();
        if (this.mActive.put(f.mWho, active) == null && f.mRetainInstanceChangedWhileDetached) {
            if (f.mRetainInstance) {
                this.mViewModel.addRetainedFragment(f);
            } else {
                this.mViewModel.removeRetainedFragment(f);
            }
            f.mRetainInstanceChangedWhileDetached = false;
        }
    }

    void addFragment(@Nonnull Fragment fragment) {
        if (this.mAdded.contains(fragment)) {
            throw new IllegalStateException("Fragment already added: " + fragment);
        } else {
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
        }
    }

    void dispatchStateChange(int state) {
        for (FragmentStateManager manager : this.mActive.values()) {
            if (manager != null) {
                manager.setFragmentManagerState(state);
            }
        }
    }

    void moveToExpectedState() {
        for (Fragment f : this.mAdded) {
            FragmentStateManager fragmentStateManager = (FragmentStateManager) this.mActive.get(f.mWho);
            if (fragmentStateManager != null) {
                fragmentStateManager.moveToExpectedState();
            }
        }
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager != null) {
                fragmentStateManager.moveToExpectedState();
                Fragment fx = fragmentStateManager.getFragment();
                boolean beingRemoved = fx.mRemoving && !fx.isInBackStack();
                if (beingRemoved) {
                    this.makeInactive(fragmentStateManager);
                }
            }
        }
    }

    void removeFragment(@Nonnull Fragment fragment) {
        synchronized (this.mAdded) {
            this.mAdded.remove(fragment);
        }
        fragment.mAdded = false;
    }

    void makeInactive(@Nonnull FragmentStateManager inactive) {
        Fragment f = inactive.getFragment();
        if (f.mRetainInstance) {
            this.mViewModel.removeRetainedFragment(f);
        }
        if (this.mActive.put(f.mWho, null) != null) {
        }
    }

    void burpActive() {
        Collection<FragmentStateManager> values = this.mActive.values();
        values.removeAll(Collections.singleton(null));
    }

    @Nullable
    ArrayList<String> saveAddedFragments() {
        synchronized (this.mAdded) {
            if (this.mAdded.isEmpty()) {
                return null;
            } else {
                ArrayList<String> added = new ArrayList(this.mAdded.size());
                for (Fragment f : this.mAdded) {
                    added.add(f.mWho);
                }
                return added;
            }
        }
    }

    @Nonnull
    List<FragmentStateManager> getActiveFragmentStateManagers() {
        ArrayList<FragmentStateManager> list = new ArrayList();
        for (FragmentStateManager manager : this.mActive.values()) {
            if (manager != null) {
                list.add(manager);
            }
        }
        return list;
    }

    @Nonnull
    List<Fragment> getFragments() {
        if (this.mAdded.isEmpty()) {
            return Collections.emptyList();
        } else {
            synchronized (this.mAdded) {
                return new ArrayList(this.mAdded);
            }
        }
    }

    @Nonnull
    List<Fragment> getActiveFragments() {
        ArrayList<Fragment> list = new ArrayList();
        for (FragmentStateManager manager : this.mActive.values()) {
            if (manager != null) {
                list.add(manager.getFragment());
            } else {
                list.add(null);
            }
        }
        return list;
    }

    int getActiveFragmentCount() {
        return this.mActive.size();
    }

    @Nullable
    Fragment findFragmentById(int id) {
        for (int i = this.mAdded.size() - 1; i >= 0; i--) {
            Fragment f = (Fragment) this.mAdded.get(i);
            if (f.mFragmentId == id) {
                return f;
            }
        }
        for (FragmentStateManager manager : this.mActive.values()) {
            if (manager != null) {
                Fragment f = manager.getFragment();
                if (f.mFragmentId == id) {
                    return f;
                }
            }
        }
        return null;
    }

    @Nullable
    Fragment findFragmentByTag(@Nullable String tag) {
        if (tag == null) {
            return null;
        } else {
            for (int i = this.mAdded.size() - 1; i >= 0; i--) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (tag.equals(f.mTag)) {
                    return f;
                }
            }
            for (FragmentStateManager manager : this.mActive.values()) {
                if (manager != null) {
                    Fragment f = manager.getFragment();
                    if (tag.equals(f.mTag)) {
                        return f;
                    }
                }
            }
            return null;
        }
    }

    boolean containsActiveFragment(@Nonnull String who) {
        return this.mActive.get(who) != null;
    }

    @Nullable
    FragmentStateManager getFragmentStateManager(@Nonnull String who) {
        return (FragmentStateManager) this.mActive.get(who);
    }

    @Nullable
    Fragment findFragmentByWho(@Nonnull String who) {
        for (FragmentStateManager manager : this.mActive.values()) {
            if (manager != null) {
                Fragment f = manager.getFragment();
                if ((f = f.findFragmentByWho(who)) != null) {
                    return f;
                }
            }
        }
        return null;
    }

    @Nullable
    Fragment findActiveFragment(@Nonnull String who) {
        FragmentStateManager manager = (FragmentStateManager) this.mActive.get(who);
        return manager != null ? manager.getFragment() : null;
    }

    int findFragmentIndexInContainer(@Nonnull Fragment f) {
        ViewGroup container = f.mContainer;
        if (container == null) {
            return -1;
        } else {
            int fragmentIndex = this.mAdded.indexOf(f);
            for (int i = fragmentIndex - 1; i >= 0; i--) {
                Fragment underFragment = (Fragment) this.mAdded.get(i);
                if (underFragment.mContainer == container && underFragment.mView != null) {
                    int underIndex = container.indexOfChild(underFragment.mView);
                    return underIndex + 1;
                }
            }
            for (int ix = fragmentIndex + 1; ix < this.mAdded.size(); ix++) {
                Fragment overFragment = (Fragment) this.mAdded.get(ix);
                if (overFragment.mContainer == container && overFragment.mView != null) {
                    return container.indexOfChild(overFragment.mView);
                }
            }
            return -1;
        }
    }

    void dump(@Nonnull String prefix, @Nullable FileDescriptor fd, @Nonnull PrintWriter writer, @Nullable String... args) {
        String innerPrefix = prefix + "    ";
        if (!this.mActive.isEmpty()) {
            writer.print(prefix);
            writer.println("Active Fragments:");
            for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
                writer.print(prefix);
                if (fragmentStateManager != null) {
                    Fragment f = fragmentStateManager.getFragment();
                    writer.println(f);
                    f.dump(innerPrefix, fd, writer, args);
                } else {
                    writer.println("null");
                }
            }
        }
        int count = this.mAdded.size();
        if (count > 0) {
            writer.print(prefix);
            writer.println("Added Fragments:");
            for (int i = 0; i < count; i++) {
                Fragment f = (Fragment) this.mAdded.get(i);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i);
                writer.print(": ");
                writer.println(f.toString());
            }
        }
    }
}