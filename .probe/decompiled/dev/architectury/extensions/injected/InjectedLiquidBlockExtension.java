package dev.architectury.extensions.injected;

import dev.architectury.hooks.fluid.LiquidBlockHooks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public interface InjectedLiquidBlockExtension {

    default FlowingFluid arch$getFluid() {
        return LiquidBlockHooks.getFluid((LiquidBlock) this);
    }
}