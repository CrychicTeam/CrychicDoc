package lio.playeranimatorapi.mixin;

import java.util.function.Function;
import net.liopyu.liolib.core.animatable.GeoAnimatable;
import net.liopyu.liolib.core.animation.AnimationController;
import net.liopyu.liolib.core.animation.EasingType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ AnimationController.class })
public interface AnimationControllerAccessor_LioLibOnly<T extends GeoAnimatable> {

    @Accessor(remap = false)
    Function<T, EasingType> getOverrideEasingTypeFunction();

    @Accessor(remap = false)
    void setIsJustStarting(boolean var1);
}