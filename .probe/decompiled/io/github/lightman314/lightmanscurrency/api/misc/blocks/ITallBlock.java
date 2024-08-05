package io.github.lightman314.lightmanscurrency.api.misc.blocks;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface ITallBlock {

    BooleanProperty ISBOTTOM = BlockStateProperties.BOTTOM;

    default BlockPos getOtherHeight(BlockPos pos, BlockState state) {
        return this.getIsBottom(state) ? pos.above() : pos.below();
    }

    default boolean getIsBottom(BlockState state) {
        return (Boolean) state.m_61143_(ISBOTTOM);
    }

    default boolean getIsTop(BlockState state) {
        return !this.getIsBottom(state);
    }

    default boolean isReplaceable(@Nonnull Level level, @Nonnull BlockPos pos) {
        if (level.getBlockState(pos).m_60734_() == Blocks.AIR) {
            LightmansCurrency.LogDebug("Block at " + pos.m_123344_() + " is air, and can be replaced.");
            return true;
        } else {
            return false;
        }
    }
}