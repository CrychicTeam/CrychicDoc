package net.minecraft.world.level.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WoolCarpetBlock extends CarpetBlock {

    private final DyeColor color;

    protected WoolCarpetBlock(DyeColor dyeColor0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.color = dyeColor0;
    }

    public DyeColor getColor() {
        return this.color;
    }
}