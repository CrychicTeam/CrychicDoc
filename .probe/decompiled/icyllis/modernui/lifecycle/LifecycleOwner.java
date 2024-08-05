package icyllis.modernui.lifecycle;

import javax.annotation.Nonnull;

public interface LifecycleOwner {

    @Nonnull
    Lifecycle getLifecycle();
}