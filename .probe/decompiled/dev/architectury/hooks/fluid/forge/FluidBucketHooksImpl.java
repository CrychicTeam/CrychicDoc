package dev.architectury.hooks.fluid.forge;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;

public class FluidBucketHooksImpl {

    public static Fluid getFluid(BucketItem item) {
        return item.getFluid();
    }
}