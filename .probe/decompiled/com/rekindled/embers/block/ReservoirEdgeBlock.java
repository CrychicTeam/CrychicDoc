package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ReservoirEdgeBlock extends MechEdgeBlockBase {

    public ReservoirEdgeBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Block getCenterBlock() {
        return RegistryManager.RESERVOIR.get();
    }
}