package icyllis.modernui.fragment;

import icyllis.modernui.annotation.UiThread;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface FragmentOnAttachListener {

    @UiThread
    void onAttachFragment(@Nonnull FragmentManager var1, @Nonnull Fragment var2);
}