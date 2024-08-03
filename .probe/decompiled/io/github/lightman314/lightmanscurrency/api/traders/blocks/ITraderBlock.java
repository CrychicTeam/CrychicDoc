package io.github.lightman314.lightmanscurrency.api.traders.blocks;

import io.github.lightman314.lightmanscurrency.api.misc.blockentity.IOwnableBlockEntity;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.ICapabilityBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IOwnableBlock;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface ITraderBlock extends IOwnableBlock, ICapabilityBlock {

    @Nullable
    BlockEntity getBlockEntity(@Nonnull BlockState var1, @Nonnull LevelAccessor var2, @Nonnull BlockPos var3);

    @Override
    default boolean canBreak(@Nonnull Player player, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return this.getBlockEntity(state, level, pos) instanceof IOwnableBlockEntity ownableBlockEntity ? ownableBlockEntity.canBreak(player) : true;
    }

    default ItemStack getDropBlockItem(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new ItemStack(state.m_60734_());
    }

    @Override
    default BlockEntity getCapabilityBlockEntity(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos) {
        return this.getBlockEntity(state, level, pos);
    }
}