package icyllis.modernui.lifecycle;

import javax.annotation.Nonnull;

public interface ViewModelStoreOwner {

    @Nonnull
    ViewModelStore getViewModelStore();

    default ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return null;
    }
}