package com.simibubi.create.content.decoration;

import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ScaffoldingBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MetalScaffoldingBlockItem extends ScaffoldingBlockItem {

    public MetalScaffoldingBlockItem(Block pBlock, Item.Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.m_43725_();
        BlockState blockstate = level.getBlockState(blockpos);
        Block block = this.m_40614_();
        if (!blockstate.m_60713_(block)) {
            return pContext;
        } else {
            Direction direction;
            if (pContext.m_7078_()) {
                direction = pContext.m_43721_() ? pContext.m_43719_().getOpposite() : pContext.m_43719_();
            } else {
                direction = pContext.m_43719_() == Direction.UP ? pContext.m_8125_() : Direction.UP;
            }
            int i = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = blockpos.mutable().move(direction);
            while (i < 7) {
                if (!level.isClientSide && !level.isInWorldBounds(blockpos$mutableblockpos)) {
                    Player player = pContext.m_43723_();
                    int j = level.m_151558_();
                    if (player instanceof ServerPlayer sp && blockpos$mutableblockpos.m_123342_() >= j) {
                        sp.sendSystemMessage(Component.translatable("build.tooHigh", j - 1).withStyle(ChatFormatting.RED), true);
                    }
                    break;
                }
                blockstate = level.getBlockState(blockpos$mutableblockpos);
                if (!blockstate.m_60713_(this.m_40614_())) {
                    if (blockstate.m_60629_(pContext)) {
                        return BlockPlaceContext.at(pContext, blockpos$mutableblockpos, direction);
                    }
                    break;
                }
                blockpos$mutableblockpos.move(direction);
                if (direction.getAxis().isHorizontal()) {
                    i++;
                }
            }
            return null;
        }
    }
}