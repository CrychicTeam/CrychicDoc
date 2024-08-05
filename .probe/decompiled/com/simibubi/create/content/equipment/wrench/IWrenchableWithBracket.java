package com.simibubi.create.content.equipment.wrench;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IWrenchableWithBracket extends IWrenchable {

    Optional<ItemStack> removeBracket(BlockGetter var1, BlockPos var2, boolean var3);

    @Override
    default InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return this.tryRemoveBracket(context) ? InteractionResult.SUCCESS : IWrenchable.super.onWrenched(state, context);
    }

    default boolean tryRemoveBracket(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Optional<ItemStack> bracket = this.removeBracket(world, pos, false);
        BlockState blockState = world.getBlockState(pos);
        if (bracket.isPresent()) {
            Player player = context.getPlayer();
            if (!world.isClientSide && !player.isCreative()) {
                player.getInventory().placeItemBackInInventory((ItemStack) bracket.get());
            }
            if (!world.isClientSide && AllBlocks.FLUID_PIPE.has(blockState)) {
                Direction.Axis preferred = FluidPropagator.getStraightPipeAxis(blockState);
                Direction preferredDirection = preferred == null ? Direction.UP : Direction.get(Direction.AxisDirection.POSITIVE, preferred);
                BlockState updated = ((FluidPipeBlock) AllBlocks.FLUID_PIPE.get()).updateBlockState(blockState, preferredDirection, null, world, pos);
                if (updated != blockState) {
                    world.setBlockAndUpdate(pos, updated);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}