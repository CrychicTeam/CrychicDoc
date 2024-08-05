package net.mehvahdjukaar.supplementaries.integration.inspirations;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CauldronPlugin {

    public static boolean doStuff(BlockEntity cauldronTile, SoftFluidTank faucetFluidHolder, boolean doTransfer, Supplier<Boolean> transferBelow) {
        return false;
    }

    public static boolean tryAddFluid(BlockEntity cauldronTile, SoftFluidTank faucetFluidHolder) {
        return false;
    }
}