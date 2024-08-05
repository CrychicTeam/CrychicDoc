package com.mna.blocks.tileentities;

import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class WellspringPillarTile extends BlockEntity {

    public WellspringPillarTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public WellspringPillarTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.WELLSPRING_PILLAR.get(), pos, state);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_, this.f_58858_.offset(1, 6, 1));
    }
}