package dev.architectury.hooks.fluid;

import dev.architectury.hooks.fluid.forge.LiquidBlockHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public final class LiquidBlockHooks {

    @ExpectPlatform
    @Transformed
    public static FlowingFluid getFluid(LiquidBlock block) {
        return LiquidBlockHooksImpl.getFluid(block);
    }
}