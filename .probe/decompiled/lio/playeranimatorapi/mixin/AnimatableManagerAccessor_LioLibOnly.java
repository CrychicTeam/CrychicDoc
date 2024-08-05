package lio.playeranimatorapi.mixin;

import net.liopyu.liolib.core.animation.AnimatableManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ AnimatableManager.class })
public interface AnimatableManagerAccessor_LioLibOnly {

    @Invoker(remap = false)
    void callFinishFirstTick();
}