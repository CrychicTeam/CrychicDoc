package icyllis.modernui.fragment;

import icyllis.modernui.lifecycle.LifecycleOwner;
import javax.annotation.Nonnull;

public interface OnBackPressedDispatcherOwner extends LifecycleOwner {

    @Nonnull
    OnBackPressedDispatcher getOnBackPressedDispatcher();
}