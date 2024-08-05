package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseEntityBlock extends Block implements EntityBlock {

    protected BaseEntityBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean triggerEvent(BlockState blockState0, Level level1, BlockPos blockPos2, int int3, int int4) {
        super.m_8133_(blockState0, level1, blockPos2, int3, int4);
        BlockEntity $$5 = level1.getBlockEntity(blockPos2);
        return $$5 == null ? false : $$5.triggerEvent(int3, int4);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState0, Level level1, BlockPos blockPos2) {
        BlockEntity $$3 = level1.getBlockEntity(blockPos2);
        return $$3 instanceof MenuProvider ? (MenuProvider) $$3 : null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityTypeA0, BlockEntityType<E> blockEntityTypeE1, BlockEntityTicker<? super E> blockEntityTickerSuperE2) {
        return blockEntityTypeE1 == blockEntityTypeA0 ? blockEntityTickerSuperE2 : null;
    }
}