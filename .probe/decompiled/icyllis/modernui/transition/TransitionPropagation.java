package icyllis.modernui.transition;

import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TransitionPropagation {

    public abstract long getStartDelay(@Nonnull ViewGroup var1, @Nonnull Transition var2, @Nullable TransitionValues var3, @Nullable TransitionValues var4);

    public abstract void captureValues(@Nonnull TransitionValues var1);

    @Nullable
    public abstract String[] getPropagationProperties();
}