package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.server.block.MetalScaffoldingBlock;
import com.github.alexthe666.citadel.item.BlockItemWithSupplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class BlockItemWithScaffolding extends BlockItemWithSupplier {

    private final RegistryObject<Block> block;

    public BlockItemWithScaffolding(RegistryObject<Block> blockSupplier, Item.Properties props) {
        super(blockSupplier, props);
        this.block = blockSupplier;
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.m_43725_();
        BlockState blockstate = level.getBlockState(blockpos);
        Block block = this.m_40614_();
        if (!(blockstate.m_60734_() instanceof MetalScaffoldingBlock)) {
            return MetalScaffoldingBlock.getDistance(level, blockpos) == 12 ? null : context;
        } else {
            Direction direction;
            if (context.m_7078_()) {
                direction = context.m_43721_() ? context.m_43719_().getOpposite() : context.m_43719_();
            } else {
                direction = context.m_43719_() == Direction.UP ? context.m_8125_() : Direction.UP;
            }
            int i = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = blockpos.mutable().move(direction);
            while (i < 12) {
                if (!level.isClientSide && !level.isInWorldBounds(blockpos$mutableblockpos)) {
                    Player player = context.m_43723_();
                    int j = level.m_151558_();
                    if (player instanceof ServerPlayer && blockpos$mutableblockpos.m_123342_() >= j) {
                        ((ServerPlayer) player).sendSystemMessage(Component.translatable("build.tooHigh", j - 1).withStyle(ChatFormatting.RED), true);
                    }
                    break;
                }
                blockstate = level.getBlockState(blockpos$mutableblockpos);
                if (!(blockstate.m_60734_() instanceof MetalScaffoldingBlock)) {
                    if (blockstate.m_60629_(context)) {
                        return BlockPlaceContext.at(context, blockpos$mutableblockpos, direction);
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

    @Override
    protected boolean mustSurvive() {
        return false;
    }
}