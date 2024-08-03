package org.violetmoon.quark.content.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.ZetaBushBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class GlowShroomBlock extends ZetaBushBlock implements BonemealableBlock {

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);

    public GlowShroomBlock(@Nullable ZetaModule module) {
        super("glow_shroom", module, null, BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM).randomTicks().lightLevel(s -> 10));
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        super.m_214162_(stateIn, worldIn, pos, rand);
        if (rand.nextInt(12) == 0 && worldIn.getBlockState(pos.above()).m_60795_()) {
            worldIn.addParticle(ParticleTypes.END_ROD, (double) pos.m_123341_() + 0.4 + rand.nextDouble() * 0.2, (double) pos.m_123342_() + 0.5 + rand.nextDouble() * 0.1, (double) pos.m_123343_() + 0.4 + rand.nextDouble() * 0.2, (Math.random() - 0.5) * 0.04, (1.0 + Math.random()) * 0.02, (Math.random() - 0.5) * 0.04);
        }
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos) {
        return state.m_60734_() == Blocks.DEEPSLATE;
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader levelReader, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level world, RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return (double) random.nextFloat() < 0.4;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel world, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        HugeGlowShroomBlock.place(world, random, pos);
    }
}