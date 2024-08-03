package com.simibubi.create.content.kinetics.gearbox;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class VerticalGearboxItem extends BlockItem {

    public VerticalGearboxItem(Item.Properties builder) {
        super((Block) AllBlocks.GEARBOX.get(), builder);
    }

    @Override
    public String getDescriptionId() {
        return "item.create.vertical_gearbox";
    }

    @Override
    public void registerBlocks(Map<Block, Item> p_195946_1_, Item p_195946_2_) {
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level world, Player player, ItemStack stack, BlockState state) {
        Direction.Axis prefferedAxis = null;
        for (Direction side : Iterate.horizontalDirections) {
            BlockState blockState = world.getBlockState(pos.relative(side));
            if (blockState.m_60734_() instanceof IRotate && ((IRotate) blockState.m_60734_()).hasShaftTowards(world, pos.relative(side), blockState, side.getOpposite())) {
                if (prefferedAxis != null && prefferedAxis != side.getAxis()) {
                    prefferedAxis = null;
                    break;
                }
                prefferedAxis = side.getAxis();
            }
        }
        Direction.Axis axis = prefferedAxis == null ? player.m_6350_().getClockWise().getAxis() : (prefferedAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
        world.setBlockAndUpdate(pos, (BlockState) state.m_61124_(BlockStateProperties.AXIS, axis));
        return super.updateCustomBlockEntityTag(pos, world, player, stack, state);
    }
}