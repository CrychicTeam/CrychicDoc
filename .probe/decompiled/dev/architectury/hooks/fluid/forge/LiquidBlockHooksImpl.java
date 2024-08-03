package dev.architectury.hooks.fluid.forge;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public class LiquidBlockHooksImpl {

    public static FlowingFluid getFluid(LiquidBlock block) {
        return block.getFluid();
    }
}