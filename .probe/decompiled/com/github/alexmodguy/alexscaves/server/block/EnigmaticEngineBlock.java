package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.blockentity.ACBlockEntityRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.EnigmaticEngineBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class EnigmaticEngineBlock extends BaseEntityBlock {

    public EnigmaticEngineBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(6.0F, 12.0F).sound(SoundType.COPPER));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return m_152132_(entityType, ACBlockEntityRegistry.ENIGMATIC_ENGINE.get(), EnigmaticEngineBlockEntity::tick);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnigmaticEngineBlockEntity(pos, state);
    }

    public boolean attemptAssembly(LevelAccessor levelAccessor, BlockPos blockPos) {
        return levelAccessor.m_7702_(blockPos) instanceof EnigmaticEngineBlockEntity blockEntity ? blockEntity.attemptAssembly() : false;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState state, @Nullable LivingEntity living, ItemStack itemStack) {
        this.attemptAssembly(level, blockPos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        return this.attemptAssembly(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }
}