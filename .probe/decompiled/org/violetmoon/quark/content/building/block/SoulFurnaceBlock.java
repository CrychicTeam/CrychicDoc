package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.module.ZetaModule;

public class SoulFurnaceBlock extends VariantFurnaceBlock {

    public static final BooleanProperty SOUL = BooleanProperty.create("soul");

    public SoulFurnaceBlock(String type, ZetaModule module, BlockBehaviour.Properties props) {
        super(type, module, props);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(SOUL, false));
    }

    @Override
    public void animateTick(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if ((Boolean) state.m_61143_(f_48684_) && (Boolean) state.m_61143_(SOUL)) {
            double d0 = (double) pos.m_123341_() + 0.5;
            double d1 = (double) pos.m_123342_();
            double d2 = (double) pos.m_123343_() + 0.5;
            if (random.nextDouble() < 0.1) {
                level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
            Direction direction = (Direction) state.m_61143_(f_48683_);
            Direction.Axis direction$axis = direction.getAxis();
            double d4 = random.nextDouble() * 0.6 - 0.3;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52 : d4;
            double d6 = random.nextDouble() * 6.0 / 16.0;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52 : d4;
            level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0, 0.0, 0.0);
        } else {
            super.m_214162_(state, level, pos, random);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter iblockreader = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        BlockPos down = blockpos.below();
        BlockState downState = iblockreader.getBlockState(down);
        return (BlockState) super.m_5573_(context).m_61124_(SOUL, downState.m_204336_(BlockTags.SOUL_FIRE_BASE_BLOCKS));
    }

    @NotNull
    @Override
    public BlockState updateShape(@NotNull BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        return facing == Direction.DOWN ? (BlockState) stateIn.m_61124_(SOUL, facingState.m_204336_(BlockTags.SOUL_FIRE_BASE_BLOCKS)) : super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(SOUL);
    }
}