package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ScaffoldingBlockItem extends BlockItem {

    public ScaffoldingBlockItem(Block block0, Item.Properties itemProperties1) {
        super(block0, itemProperties1);
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext blockPlaceContext0) {
        BlockPos $$1 = blockPlaceContext0.getClickedPos();
        Level $$2 = blockPlaceContext0.m_43725_();
        BlockState $$3 = $$2.getBlockState($$1);
        Block $$4 = this.m_40614_();
        if (!$$3.m_60713_($$4)) {
            return ScaffoldingBlock.getDistance($$2, $$1) == 7 ? null : blockPlaceContext0;
        } else {
            Direction $$5;
            if (blockPlaceContext0.m_7078_()) {
                $$5 = blockPlaceContext0.m_43721_() ? blockPlaceContext0.m_43719_().getOpposite() : blockPlaceContext0.m_43719_();
            } else {
                $$5 = blockPlaceContext0.m_43719_() == Direction.UP ? blockPlaceContext0.m_8125_() : Direction.UP;
            }
            int $$7 = 0;
            BlockPos.MutableBlockPos $$8 = $$1.mutable().move($$5);
            while ($$7 < 7) {
                if (!$$2.isClientSide && !$$2.isInWorldBounds($$8)) {
                    Player $$9 = blockPlaceContext0.m_43723_();
                    int $$10 = $$2.m_151558_();
                    if ($$9 instanceof ServerPlayer && $$8.m_123342_() >= $$10) {
                        ((ServerPlayer) $$9).sendSystemMessage(Component.translatable("build.tooHigh", $$10 - 1).withStyle(ChatFormatting.RED), true);
                    }
                    break;
                }
                $$3 = $$2.getBlockState($$8);
                if (!$$3.m_60713_(this.m_40614_())) {
                    if ($$3.m_60629_(blockPlaceContext0)) {
                        return BlockPlaceContext.at(blockPlaceContext0, $$8, $$5);
                    }
                    break;
                }
                $$8.move($$5);
                if ($$5.getAxis().isHorizontal()) {
                    $$7++;
                }
            }
            return null;
        }
    }

    @Override
    protected boolean mustSurvive() {
        return false;
    }
}