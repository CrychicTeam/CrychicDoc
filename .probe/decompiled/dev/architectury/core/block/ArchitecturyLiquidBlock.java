package dev.architectury.core.block;

import java.util.function.Supplier;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;

public class ArchitecturyLiquidBlock extends LiquidBlock {

    public ArchitecturyLiquidBlock(Supplier<? extends FlowingFluid> fluid, BlockBehaviour.Properties properties) {
        super(fluid, properties);
    }
}