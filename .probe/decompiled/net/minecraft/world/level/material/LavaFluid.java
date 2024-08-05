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
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public abstract class LavaFluid extends FlowingFluid {

    public static final float MIN_LEVEL_CUTOFF = 0.44444445F;

    @Override
    public Fluid getFlowing() {
        return Fluids.FLOWING_LAVA;
    }

    @Override
    public Fluid getSource() {
        return Fluids.LAVA;
    }

    @Override
    public Item getBucket() {
        return Items.LAVA_BUCKET;
    }

    @Override
    public void animateTick(Level level0, BlockPos blockPos1, FluidState fluidState2, RandomSource randomSource3) {
        BlockPos $$4 = blockPos1.above();
        if (level0.getBlockState($$4).m_60795_() && !level0.getBlockState($$4).m_60804_(level0, $$4)) {
            if (randomSource3.nextInt(100) == 0) {
                double $$5 = (double) blockPos1.m_123341_() + randomSource3.nextDouble();
                double $$6 = (double) blockPos1.m_123342_() + 1.0;
                double $$7 = (double) blockPos1.m_123343_() + randomSource3.nextDouble();
                level0.addParticle(ParticleTypes.LAVA, $$5, $$6, $$7, 0.0, 0.0, 0.0);
                level0.playLocalSound($$5, $$6, $$7, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + randomSource3.nextFloat() * 0.2F, 0.9F + randomSource3.nextFloat() * 0.15F, false);
            }
            if (randomSource3.nextInt(200) == 0) {
                level0.playLocalSound((double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + randomSource3.nextFloat() * 0.2F, 0.9F + randomSource3.nextFloat() * 0.15F, false);
            }
        }
    }

    @Override
    public void randomTick(Level level0, BlockPos blockPos1, FluidState fluidState2, RandomSource randomSource3) {
        if (level0.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            int $$4 = randomSource3.nextInt(3);
            if ($$4 > 0) {
                BlockPos $$5 = blockPos1;
                for (int $$6 = 0; $$6 < $$4; $$6++) {
                    $$5 = $$5.offset(randomSource3.nextInt(3) - 1, 1, randomSource3.nextInt(3) - 1);
                    if (!level0.isLoaded($$5)) {
                        return;
                    }
                    BlockState $$7 = level0.getBlockState($$5);
                    if ($$7.m_60795_()) {
                        if (this.hasFlammableNeighbours(level0, $$5)) {
                            level0.setBlockAndUpdate($$5, BaseFireBlock.getState(level0, $$5));
                            return;
                        }
                    } else if ($$7.m_280555_()) {
                        return;
                    }
                }
            } else {
                for (int $$8 = 0; $$8 < 3; $$8++) {
                    BlockPos $$9 = blockPos1.offset(randomSource3.nextInt(3) - 1, 0, randomSource3.nextInt(3) - 1);
                    if (!level0.isLoaded($$9)) {
                        return;
                    }
                    if (level0.m_46859_($$9.above()) && this.isFlammable(level0, $$9)) {
                        level0.setBlockAndUpdate($$9.above(), BaseFireBlock.getState(level0, $$9));
                    }
                }
            }
        }
    }

    private boolean hasFlammableNeighbours(LevelReader levelReader0, BlockPos blockPos1) {
        for (Direction $$2 : Direction.values()) {
            if (this.isFlammable(levelReader0, blockPos1.relative($$2))) {
                return true;
            }
        }
        return false;
    }

    private boolean isFlammable(LevelReader levelReader0, BlockPos blockPos1) {
        return blockPos1.m_123342_() >= levelReader0.getMinBuildHeight() && blockPos1.m_123342_() < levelReader0.m_151558_() && !levelReader0.hasChunkAt(blockPos1) ? false : levelReader0.m_8055_(blockPos1).m_278200_();
    }

    @Nullable
    @Override
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_LAVA;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2) {
        this.fizz(levelAccessor0, blockPos1);
    }

    @Override
    public int getSlopeFindDistance(LevelReader levelReader0) {
        return levelReader0.dimensionType().ultraWarm() ? 4 : 2;
    }

    @Override
    public BlockState createLegacyBlock(FluidState fluidState0) {
        return (BlockState) Blocks.LAVA.defaultBlockState().m_61124_(LiquidBlock.LEVEL, m_76092_(fluidState0));
    }

    @Override
    public boolean isSame(Fluid fluid0) {
        return fluid0 == Fluids.LAVA || fluid0 == Fluids.FLOWING_LAVA;
    }

    @Override
    public int getDropOff(LevelReader levelReader0) {
        return levelReader0.dimensionType().ultraWarm() ? 1 : 2;
    }

    @Override
    public boolean canBeReplacedWith(FluidState fluidState0, BlockGetter blockGetter1, BlockPos blockPos2, Fluid fluid3, Direction direction4) {
        return fluidState0.getHeight(blockGetter1, blockPos2) >= 0.44444445F && fluid3.is(FluidTags.WATER);
    }

    @Override
    public int getTickDelay(LevelReader levelReader0) {
        return levelReader0.dimensionType().ultraWarm() ? 10 : 30;
    }

    @Override
    public int getSpreadDelay(Level level0, BlockPos blockPos1, FluidState fluidState2, FluidState fluidState3) {
        int $$4 = this.getTickDelay(level0);
        if (!fluidState2.isEmpty() && !fluidState3.isEmpty() && !(Boolean) fluidState2.m_61143_(f_75947_) && !(Boolean) fluidState3.m_61143_(f_75947_) && fluidState3.getHeight(level0, blockPos1) > fluidState2.getHeight(level0, blockPos1) && level0.getRandom().nextInt(4) != 0) {
            $$4 *= 4;
        }
        return $$4;
    }

    private void fizz(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        levelAccessor0.levelEvent(1501, blockPos1, 0);
    }

    @Override
    protected boolean canConvertToSource(Level level0) {
        return level0.getGameRules().getBoolean(GameRules.RULE_LAVA_SOURCE_CONVERSION);
    }

    @Override
    protected void spreadTo(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, Direction direction3, FluidState fluidState4) {
        if (direction3 == Direction.DOWN) {
            FluidState $$5 = levelAccessor0.m_6425_(blockPos1);
            if (this.m_205067_(FluidTags.LAVA) && $$5.is(FluidTags.WATER)) {
                if (blockState2.m_60734_() instanceof LiquidBlock) {
                    levelAccessor0.m_7731_(blockPos1, Blocks.STONE.defaultBlockState(), 3);
                }
                this.fizz(levelAccessor0, blockPos1);
                return;
            }
        }
        super.spreadTo(levelAccessor0, blockPos1, blockState2, direction3, fluidState4);
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
    }

    public static class Flowing extends LavaFluid {

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

    public static class Source extends LavaFluid {

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