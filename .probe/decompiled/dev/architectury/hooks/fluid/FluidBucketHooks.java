package dev.architectury.hooks.fluid;

import dev.architectury.hooks.fluid.forge.FluidBucketHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;

public final class FluidBucketHooks {

    @ExpectPlatform
    @Transformed
    public static Fluid getFluid(BucketItem item) {
        return FluidBucketHooksImpl.getFluid(item);
    }
}