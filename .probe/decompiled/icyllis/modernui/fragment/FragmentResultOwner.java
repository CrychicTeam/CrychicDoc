package icyllis.modernui.fragment;

import icyllis.modernui.lifecycle.LifecycleOwner;
import icyllis.modernui.util.DataSet;
import javax.annotation.Nonnull;

public interface FragmentResultOwner {

    void setFragmentResult(@Nonnull String var1, @Nonnull DataSet var2);

    void clearFragmentResult(@Nonnull String var1);

    void setFragmentResultListener(@Nonnull String var1, @Nonnull LifecycleOwner var2, @Nonnull FragmentResultListener var3);

    void clearFragmentResultListener(@Nonnull String var1);
}