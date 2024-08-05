package net.minecraft.world.level.material;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public abstract class WaterFluid extends FlowingFluid {

    @Override
    public Fluid getFlowing() {
        return Fluids.FLOWING_WATER;
    }

    @Override
    public Fluid getSource() {
        return Fluids.WATER;
    }

    @Override
    public Item getBucket() {
        return Items.WATER_BUCKET;
    }

    @Override
    public void animateTick(Level level0, BlockPos blockPos1, FluidState fluidState2, RandomSource randomSource3) {
        if (!fluidState2.isSource() && !(Boolean) fluidState2.m_61143_(f_75947_)) {
            if (randomSource3.nextInt(64) == 0) {
                level0.playLocalSound((double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.5, (double) blockPos1.m_123343_() + 0.5, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, randomSource3.nextFloat() * 0.25F + 0.75F, randomSource3.nextFloat() + 0.5F, false);
            }
        } else if (randomSource3.nextInt(10) == 0) {
            level0.addParticle(ParticleTypes.UNDERWATER, (double) blockPos1.m_123341_() + randomSource3.nextDouble(), (double) blockPos1.m_123342_() + randomSource3.nextDouble(), (double) blockPos1.m_123343_() + randomSource3.nextDouble(), 0.0, 0.0, 0.0);
        }
    }

    @Nullable
    @Override
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    @Override
    protected boolean canConvertToSource(Level level0) {
        return level0.getGameRules().getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION);
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2) {
        BlockEntity $$3 = blockState2.m_155947_() ? levelAccessor0.m_7702_(blockPos1) : null;
        Block.dropResources(blockState2, levelAccessor0, blockPos1, $$3);
    }

    @Override
    public int getSlopeFindDistance(LevelReader levelReader0) {
        return 4;
    }

    @Override
    public BlockState createLegacyBlock(FluidState fluidState0) {
        return (BlockState) Blocks.WATER.defaultBlockState().m_61124_(LiquidBlock.LEVEL, m_76092_(fluidState0));
    }

    @Override
    public boolean isSame(Fluid fluid0) {
        return fluid0 == Fluids.WATER || fluid0 == Fluids.FLOWING_WATER;
    }

    @Override
    public int getDropOff(LevelReader levelReader0) {
        return 1;
    }

    @Override
    public int getTickDelay(LevelReader levelReader0) {
        return 5;
    }

    @Override
    public boolean canBeReplacedWith(FluidState fluidState0, BlockGetter blockGetter1, BlockPos blockPos2, Fluid fluid3, Direction direction4) {
        return direction4 == Direction.DOWN && !fluid3.is(FluidTags.WATER);
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    public static class Flowing extends WaterFluid {

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> stateDefinitionBuilderFluidFluidState0) {
            super.m_7180_(stateDefinitionBuilderFluidFluidState0);
            stateDefinitionBuilderFluidFluidState0.add(f_75948_);
        }

        @Override
        public int getAmount(FluidState fluidState0) {
            return (Integer) fluidState0.m_61143_(f_75948_);
        }

        @Override
        public boolean isSource(FluidState fluidState0) {
            return false;
        }
    }

    public static class Source extends WaterFluid {

        @Override
        public int getAmount(FluidState fluidState0) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState fluidState0) {
            return true;
        }
    }
}