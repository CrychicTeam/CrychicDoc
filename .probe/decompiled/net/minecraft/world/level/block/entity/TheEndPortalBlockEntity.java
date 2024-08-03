package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class TheEndPortalBlockEntity extends BlockEntity {

    protected TheEndPortalBlockEntity(BlockEntityType<?> blockEntityType0, BlockPos blockPos1, BlockState blockState2) {
        super(blockEntityType0, blockPos1, blockState2);
    }

    public TheEndPortalBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        this(BlockEntityType.END_PORTAL, blockPos0, blockState1);
    }

    public boolean shouldRenderFace(Direction direction0) {
        return direction0.getAxis() == Direction.Axis.Y;
    }
}