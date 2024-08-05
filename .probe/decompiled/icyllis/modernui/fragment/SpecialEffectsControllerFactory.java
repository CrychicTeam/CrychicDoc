package icyllis.modernui.fragment;

import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;

@FunctionalInterface
interface SpecialEffectsControllerFactory {

    @Nonnull
    SpecialEffectsController createController(@Nonnull ViewGroup var1);
}