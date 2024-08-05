package icyllis.modernui.fragment;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class FragmentController {

    @Nonnull
    private final FragmentHostCallback<?> mHost;

    @Nonnull
    public static FragmentController createController(@Nonnull FragmentHostCallback<?> callbacks) {
        return new FragmentController((FragmentHostCallback<?>) Objects.requireNonNull(callbacks));
    }

    private FragmentController(@Nonnull FragmentHostCallback<?> callbacks) {
        this.mHost = callbacks;
    }

    @Nonnull
    public FragmentManager getFragmentManager() {
        return this.mHost.mFragmentManager;
    }

    @Nullable
    public Fragment findFragmentByWho(@Nonnull String who) {
        return this.mHost.mFragmentManager.findFragmentByWho(who);
    }

    public int getActiveFragmentCount() {
        return this.mHost.mFragmentManager.getActiveFragmentCount();
    }

    @Nonnull
    public List<Fragment> getActiveFragments() {
        return this.mHost.mFragmentManager.getActiveFragments();
    }

    public void attachHost(@Nullable Fragment parent) {
        this.mHost.mFragmentManager.attachController(this.mHost, this.mHost, parent);
    }

    public void noteStateNotSaved() {
        this.mHost.mFragmentManager.noteStateNotSaved();
    }

    public void dispatchCreate() {
        this.mHost.mFragmentManager.dispatchCreate();
    }

    public void dispatchActivityCreated() {
        this.mHost.mFragmentManager.dispatchActivityCreated();
    }

    public void dispatchStart() {
        this.mHost.mFragmentManager.dispatchStart();
    }

    public void dispatchResume() {
        this.mHost.mFragmentManager.dispatchResume();
    }

    public void dispatchPause() {
        this.mHost.mFragmentManager.dispatchPause();
    }

    public void dispatchStop() {
        this.mHost.mFragmentManager.dispatchStop();
    }

    public void dispatchDestroyView() {
        this.mHost.mFragmentManager.dispatchDestroyView();
    }

    public void dispatchDestroy() {
        this.mHost.mFragmentManager.dispatchDestroy();
    }

    public boolean execPendingActions() {
        return this.mHost.mFragmentManager.execPendingActions(true);
    }
}