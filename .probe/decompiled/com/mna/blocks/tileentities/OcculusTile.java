package com.mna.blocks.tileentities;

import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class OcculusTile extends BlockEntity {

    public OcculusTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.OCCULUS.get(), pos, state);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_, this.f_58858_.offset(1, 2, 1));
    }
}