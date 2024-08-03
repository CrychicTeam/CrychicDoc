package icyllis.modernui.fragment;

import icyllis.modernui.lifecycle.Lifecycle;
import icyllis.modernui.lifecycle.LifecycleOwner;
import icyllis.modernui.lifecycle.LifecycleRegistry;
import icyllis.modernui.lifecycle.ViewModelProvider;
import icyllis.modernui.lifecycle.ViewModelStore;
import icyllis.modernui.lifecycle.ViewModelStoreOwner;
import javax.annotation.Nonnull;

class FragmentViewLifecycleOwner implements LifecycleOwner, ViewModelStoreOwner {

    private final Fragment mFragment;

    private final ViewModelStore mViewModelStore;

    private ViewModelProvider.Factory mDefaultFactory;

    private LifecycleRegistry mLifecycleRegistry;

    FragmentViewLifecycleOwner(@Nonnull Fragment fragment, @Nonnull ViewModelStore viewModelStore) {
        this.mFragment = fragment;
        this.mViewModelStore = viewModelStore;
    }

    void initialize() {
        if (this.mLifecycleRegistry == null) {
            this.mLifecycleRegistry = new LifecycleRegistry(this);
        }
    }

    boolean isInitialized() {
        return this.mLifecycleRegistry != null;
    }

    @Nonnull
    @Override
    public Lifecycle getLifecycle() {
        this.initialize();
        return this.mLifecycleRegistry;
    }

    @Nonnull
    @Override
    public ViewModelStore getViewModelStore() {
        this.initialize();
        return this.mViewModelStore;
    }

    void setCurrentState(@Nonnull Lifecycle.State state) {
        this.mLifecycleRegistry.setCurrentState(state);
    }

    void handleLifecycleEvent(@Nonnull Lifecycle.Event event) {
        this.mLifecycleRegistry.handleLifecycleEvent(event);
    }

    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        ViewModelProvider.Factory currentFactory = this.mFragment.getDefaultViewModelProviderFactory();
        if (!currentFactory.equals(this.mFragment.mDefaultFactory)) {
            this.mDefaultFactory = currentFactory;
            return currentFactory;
        } else {
            return this.mDefaultFactory;
        }
    }
}