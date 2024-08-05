package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;

public interface EntityBlock {

    @Nullable
    BlockEntity newBlockEntity(BlockPos var1, BlockState var2);

    @Nullable
    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return null;
    }

    @Nullable
    default <T extends BlockEntity> GameEventListener getListener(ServerLevel serverLevel0, T t1) {
        return t1 instanceof GameEventListener.Holder<?> $$2 ? $$2.getListener() : null;
    }
}