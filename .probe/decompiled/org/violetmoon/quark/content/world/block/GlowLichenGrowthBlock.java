package org.violetmoon.quark.content.world.block;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTabs;
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
import org.violetmoon.zeta.util.MiscUtil;

public class GlowLichenGrowthBlock extends ZetaBushBlock implements BonemealableBlock {

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);

    public GlowLichenGrowthBlock(@Nullable ZetaModule module) {
        super("glow_lichen_growth", module, CreativeModeTabs.NATURAL_BLOCKS, BlockBehaviour.Properties.copy(Blocks.GLOW_LICHEN).randomTicks().lightLevel(s -> 8));
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        super.m_214162_(stateIn, worldIn, pos, rand);
        for (int i = 0; i < 10; i++) {
            worldIn.addParticle(ParticleTypes.MYCELIUM, (double) pos.m_123341_() + (Math.random() - 0.5) * 5.0 + 0.5, (double) pos.m_123342_() + (Math.random() - 0.5) * 8.0 + 0.5, (double) pos.m_123343_() + (Math.random() - 0.5) * 5.0 + 0.5, 0.0, 0.0, 0.0);
        }
        worldIn.addParticle(ParticleTypes.MYCELIUM, (double) pos.m_123341_() + (Math.random() - 0.5) * 0.4 + 0.5, (double) pos.m_123342_() + (Math.random() - 0.5) * 0.3 + 0.3, (double) pos.m_123343_() + (Math.random() - 0.5) * 0.4 + 0.5, 0.0, 0.0, 0.0);
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos) {
        return state.m_60783_(world, pos, Direction.UP);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean isClientSided) {
        for (Direction dir : MiscUtil.HORIZONTALS) {
            if (this.canSpread(levelReader, blockPos.relative(dir))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level world, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel world, @NotNull RandomSource rand, @NotNull BlockPos pos, @NotNull BlockState state) {
        List<Direction> list = Lists.newArrayList(MiscUtil.HORIZONTALS);
        Collections.shuffle(list);
        for (Direction dir : list) {
            BlockPos offPos = pos.relative(dir);
            if (this.canSpread(world, offPos)) {
                world.m_7731_(offPos, state, 3);
                return;
            }
        }
    }

    private boolean canSpread(BlockGetter world, BlockPos pos) {
        BlockPos below = pos.below();
        return world.getBlockState(pos).m_60795_() && this.mayPlaceOn(world.getBlockState(below), world, below);
    }
}