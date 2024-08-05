package icyllis.modernui.fragment;

import icyllis.modernui.lifecycle.ViewModel;
import icyllis.modernui.lifecycle.ViewModelProvider;
import icyllis.modernui.lifecycle.ViewModelStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Marker;

final class FragmentManagerViewModel extends ViewModel {

    private static final Marker MARKER = FragmentManager.MARKER;

    private static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory() {

        @Nonnull
        @Override
        public <T extends ViewModel> T create(@Nonnull Class<T> modelClass) {
            return (T) (new FragmentManagerViewModel());
        }
    };

    private final HashMap<String, Fragment> mRetainedFragments = new HashMap();

    private final HashMap<String, FragmentManagerViewModel> mChildViewModels = new HashMap();

    private final HashMap<String, ViewModelStore> mViewModelStores = new HashMap();

    private boolean mHasBeenCleared = false;

    private boolean mIsStateSaved = false;

    @Nonnull
    static FragmentManagerViewModel getInstance(ViewModelStore viewModelStore) {
        return new ViewModelProvider(viewModelStore, FACTORY).get(FragmentManagerViewModel.class);
    }

    void setIsStateSaved(boolean isStateSaved) {
        this.mIsStateSaved = isStateSaved;
    }

    @Override
    protected void onCleared() {
        this.mHasBeenCleared = true;
    }

    boolean isCleared() {
        return this.mHasBeenCleared;
    }

    void addRetainedFragment(@Nonnull Fragment fragment) {
        if (!this.mIsStateSaved) {
            this.mRetainedFragments.putIfAbsent(fragment.mWho, fragment);
        }
    }

    @Nullable
    Fragment findRetainedFragmentByWho(@Nonnull String who) {
        return (Fragment) this.mRetainedFragments.get(who);
    }

    @Nonnull
    Collection<Fragment> getRetainedFragments() {
        return new ArrayList(this.mRetainedFragments.values());
    }

    boolean shouldDestroy(@Nonnull Fragment fragment) {
        return this.mHasBeenCleared || !this.mRetainedFragments.containsKey(fragment.mWho);
    }

    void removeRetainedFragment(@Nonnull Fragment fragment) {
        if (!this.mIsStateSaved) {
            this.mRetainedFragments.remove(fragment.mWho);
        }
    }

    @Nonnull
    FragmentManagerViewModel getChildViewModel(@Nonnull Fragment f) {
        return (FragmentManagerViewModel) this.mChildViewModels.computeIfAbsent(f.mWho, i -> new FragmentManagerViewModel());
    }

    @Nonnull
    ViewModelStore getViewModelStore(@Nonnull Fragment f) {
        return (ViewModelStore) this.mViewModelStores.computeIfAbsent(f.mWho, i -> new ViewModelStore());
    }

    void clearViewModelState(@Nonnull Fragment f) {
        FragmentManagerViewModel childViewModel = (FragmentManagerViewModel) this.mChildViewModels.remove(f.mWho);
        if (childViewModel != null) {
            childViewModel.onCleared();
        }
        ViewModelStore viewModelStore = (ViewModelStore) this.mViewModelStores.remove(f.mWho);
        if (viewModelStore != null) {
            viewModelStore.clear();
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            FragmentManagerViewModel that = (FragmentManagerViewModel) o;
            return this.mRetainedFragments.equals(that.mRetainedFragments) && this.mChildViewModels.equals(that.mChildViewModels) && this.mViewModelStores.equals(that.mViewModelStores);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.mRetainedFragments.hashCode();
        result = 31 * result + this.mChildViewModels.hashCode();
        return 31 * result + this.mViewModelStores.hashCode();
    }

    @Nonnull
    public String toString() {
        StringBuilder s = new StringBuilder("FragmentManagerViewModel{");
        s.append(Integer.toHexString(super.hashCode()));
        s.append("} Fragments (");
        Iterator<Fragment> fragmentIterator = this.mRetainedFragments.values().iterator();
        while (fragmentIterator.hasNext()) {
            s.append(fragmentIterator.next());
            if (fragmentIterator.hasNext()) {
                s.append(',').append(' ');
            }
        }
        s.append(") ChildViewModels (");
        Iterator<String> it = this.mChildViewModels.keySet().iterator();
        while (it.hasNext()) {
            s.append((String) it.next());
            if (it.hasNext()) {
                s.append(',').append(' ');
            }
        }
        s.append(") ViewModelStores (");
        it = this.mViewModelStores.keySet().iterator();
        while (it.hasNext()) {
            s.append((String) it.next());
            if (it.hasNext()) {
                s.append(',').append(' ');
            }
        }
        s.append(')');
        return s.toString();
    }
}