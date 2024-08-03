package io.github.lightman314.lightmanscurrency.api.misc.blocks;

import io.github.lightman314.lightmanscurrency.common.blockentity.TickableBlockEntity;
import java.util.Collection;
import javax.annotation.Nonnull;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface IEasyEntityBlock extends EntityBlock {

    @Nonnull
    Collection<BlockEntityType<?>> getAllowedTypes();

    @Nonnull
    @Override
    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        return this.getAllowedTypes().contains(type) ? TickableBlockEntity.createTicker(level, state, type) : null;
    }
}