package net.minecraft.world.level.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class StainedGlassBlock extends AbstractGlassBlock implements BeaconBeamBlock {

    private final DyeColor color;

    public StainedGlassBlock(DyeColor dyeColor0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.color = dyeColor0;
    }

    @Override
    public DyeColor getColor() {
        return this.color;
    }
}