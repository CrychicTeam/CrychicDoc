package icyllis.modernui.fragment;

import icyllis.modernui.annotation.UiThread;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Nonnull;

public abstract class OnBackPressedCallback {

    private boolean mEnabled;

    private final CopyOnWriteArrayList<OnBackPressedCallback.Cancellable> mCancellables = new CopyOnWriteArrayList();

    public OnBackPressedCallback(boolean enabled) {
        this.mEnabled = enabled;
    }

    @UiThread
    public final void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    @UiThread
    public final boolean isEnabled() {
        return this.mEnabled;
    }

    @UiThread
    public final void remove() {
        for (OnBackPressedCallback.Cancellable cancellable : this.mCancellables) {
            cancellable.cancel();
        }
    }

    @UiThread
    public abstract void handleOnBackPressed();

    void addCancellable(@Nonnull OnBackPressedCallback.Cancellable cancellable) {
        this.mCancellables.add(cancellable);
    }

    void removeCancellable(@Nonnull OnBackPressedCallback.Cancellable cancellable) {
        this.mCancellables.remove(cancellable);
    }

    interface Cancellable {

        void cancel();
    }
}