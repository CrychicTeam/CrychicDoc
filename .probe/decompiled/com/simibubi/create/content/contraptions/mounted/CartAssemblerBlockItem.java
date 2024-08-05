package com.simibubi.create.content.contraptions.mounted;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.redstone.rail.ControllerRailBlock;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class CartAssemblerBlockItem extends BlockItem {

    public CartAssemblerBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (this.tryPlaceAssembler(context)) {
            context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        } else {
            return super.useOn(context);
        }
    }

    public boolean tryPlaceAssembler(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState state = world.getBlockState(pos);
        Block block = state.m_60734_();
        Player player = context.getPlayer();
        if (player == null) {
            return false;
        } else if (!(block instanceof BaseRailBlock)) {
            Lang.translate("block.cart_assembler.invalid").sendStatus(player);
            return false;
        } else {
            RailShape shape = ((BaseRailBlock) block).getRailDirection(state, world, pos, null);
            if (shape != RailShape.EAST_WEST && shape != RailShape.NORTH_SOUTH) {
                return false;
            } else {
                BlockState newState = (BlockState) AllBlocks.CART_ASSEMBLER.getDefaultState().m_61124_(CartAssemblerBlock.RAIL_SHAPE, shape);
                CartAssembleRailType newType = null;
                for (CartAssembleRailType type : CartAssembleRailType.values()) {
                    if (type.matches(state)) {
                        newType = type;
                    }
                }
                if (newType == null) {
                    return false;
                } else if (world.isClientSide) {
                    return true;
                } else {
                    newState = (BlockState) newState.m_61124_(CartAssemblerBlock.RAIL_TYPE, newType);
                    if (state.m_61138_(ControllerRailBlock.BACKWARDS)) {
                        newState = (BlockState) newState.m_61124_(CartAssemblerBlock.BACKWARDS, (Boolean) state.m_61143_(ControllerRailBlock.BACKWARDS));
                    } else {
                        Direction direction = player.m_6374_();
                        newState = (BlockState) newState.m_61124_(CartAssemblerBlock.BACKWARDS, direction.getAxisDirection() == Direction.AxisDirection.POSITIVE);
                    }
                    world.setBlockAndUpdate(pos, newState);
                    if (!player.isCreative()) {
                        context.getItemInHand().shrink(1);
                    }
                    AdvancementBehaviour.setPlacedBy(world, pos, player);
                    return true;
                }
            }
        }
    }
}