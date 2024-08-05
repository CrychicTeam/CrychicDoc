package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class HearthCoilEdgeBlock extends MechEdgeBlockBase {

    public HearthCoilEdgeBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Block getCenterBlock() {
        return RegistryManager.HEARTH_COIL.get();
    }
}