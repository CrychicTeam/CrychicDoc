package com.simibubi.create.content.redstone.contact;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.elevator.ElevatorColumn;
import com.simibubi.create.foundation.utility.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneContactItem extends BlockItem {

    public RedstoneContactItem(Block pBlock, Item.Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext ctx) {
        Level world = ctx.m_43725_();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = super.getPlacementState(ctx);
        if (state == null) {
            return state;
        } else if (!(state.m_60734_() instanceof RedstoneContactBlock)) {
            return state;
        } else {
            Direction facing = (Direction) state.m_61143_(RedstoneContactBlock.f_52588_);
            if (facing.getAxis() == Direction.Axis.Y) {
                return state;
            } else {
                return ElevatorColumn.get(world, new ElevatorColumn.ColumnCoords(pos.m_123341_(), pos.m_123343_(), facing)) == null ? state : BlockHelper.copyProperties(state, AllBlocks.ELEVATOR_CONTACT.getDefaultState());
            }
        }
    }
}