package journeymap.common.accessors;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public interface FluidAccess {

    default FlowingFluid getFluid(Block block) {
        return ((LiquidBlock) block).getFluid();
    }
}