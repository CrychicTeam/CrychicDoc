package com.simibubi.create.foundation.block;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntityTicker;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface IBE<T extends BlockEntity> extends EntityBlock {

    Class<T> getBlockEntityClass();

    BlockEntityType<? extends T> getBlockEntityType();

    default void withBlockEntityDo(BlockGetter world, BlockPos pos, Consumer<T> action) {
        this.getBlockEntityOptional(world, pos).ifPresent(action);
    }

    default InteractionResult onBlockEntityUse(BlockGetter world, BlockPos pos, Function<T, InteractionResult> action) {
        return (InteractionResult) this.getBlockEntityOptional(world, pos).map(action).orElse(InteractionResult.PASS);
    }

    static void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState) {
        if (blockState.m_155947_()) {
            if (!blockState.m_60713_(newBlockState.m_60734_()) || !newBlockState.m_155947_()) {
                if (level.getBlockEntity(pos) instanceof SmartBlockEntity sbe) {
                    sbe.destroy();
                }
                level.removeBlockEntity(pos);
            }
        }
    }

    default Optional<T> getBlockEntityOptional(BlockGetter world, BlockPos pos) {
        return Optional.ofNullable(this.getBlockEntity(world, pos));
    }

    @Override
    default BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return this.getBlockEntityType().create(blockPos0, blockState1);
    }

    @Override
    default <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level0, BlockState blockState1, BlockEntityType<S> blockEntityTypeS2) {
        return SmartBlockEntity.class.isAssignableFrom(this.getBlockEntityClass()) ? new SmartBlockEntityTicker<>() : null;
    }

    @Nullable
    default T getBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        Class<T> expectedClass = this.getBlockEntityClass();
        if (blockEntity == null) {
            return null;
        } else {
            return (T) (!expectedClass.isInstance(blockEntity) ? null : blockEntity);
        }
    }
}