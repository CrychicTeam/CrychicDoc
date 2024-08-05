package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.blockentity.AmbersolBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class AmbersolBlock extends BaseEntityBlock {

    public AmbersolBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(3.0F, 10.0F).randomTicks().sound(ACSoundTypes.AMBER).lightLevel(i -> 15).emissiveRendering((state, level, pos) -> true));
    }

    public static BlockPos fillWithLights(BlockPos current, LevelAccessor level) {
        for (current = current.below(); current.m_123342_() > level.m_141937_() && AmbersolLightBlock.testSkylight(level, level.m_8055_(current), current); current = current.below()) {
            if (level.m_8055_(current).m_60795_()) {
                level.m_7731_(current, ACBlockRegistry.AMBERSOL_LIGHT.get().defaultBlockState(), 3);
            }
        }
        return current;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        fillWithLights(blockPos, levelAccessor);
        return super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
        fillWithLights(pos, serverLevel);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        fillWithLights(pos, level);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity livingEntity, ItemStack stack) {
        fillWithLights(pos, level);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AmbersolBlockEntity(pos, state);
    }
}