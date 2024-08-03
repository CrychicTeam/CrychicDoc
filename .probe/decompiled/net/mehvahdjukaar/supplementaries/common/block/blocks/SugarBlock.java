package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.supplementaries.integration.BumblezoneCompat;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SugarBlock extends ConcretePowderBlock {

    public SugarBlock(BlockBehaviour.Properties properties) {
        super(Blocks.WATER, properties);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState blockState, BlockState blockState2, FallingBlockEntity fallingBlock) {
        if (level instanceof ServerLevel serverLevel) {
            this.tick(blockState, serverLevel, pos, level.random);
        }
        if (this.isWater(blockState2)) {
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.m_49966_();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        level.scheduleTick(currentPos, this, this.m_7198_());
        return state;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.touchesLiquid(level, pos)) {
            level.blockEvent(pos, state.m_60734_(), 1, 0);
        } else {
            super.m_213897_(state, level, pos, random);
        }
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        if (id == 1) {
            if (level.isClientSide) {
                this.spawnDissolveParticles(level, pos);
            }
            if (this.shouldTurnToWater(level, pos)) {
                turnIntoWater(level, pos);
            } else {
                level.removeBlock(pos, false);
            }
            return true;
        } else {
            return super.m_8133_(state, level, pos, id, param);
        }
    }

    private static void turnIntoWater(Level level, BlockPos pos) {
        if (CompatHandler.BUMBLEZONE) {
            BumblezoneCompat.turnToSugarWater(level, pos);
        } else {
            level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        }
    }

    private boolean shouldTurnToWater(Level level, BlockPos pos) {
        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
        int count = 0;
        for (Direction direction : Direction.values()) {
            if (direction != Direction.DOWN) {
                mutableBlockPos.setWithOffset(pos, direction);
                BlockState s = level.getBlockState(mutableBlockPos);
                if (this.isWater(s) && (direction == Direction.UP || s.m_60819_().isSource())) {
                    count++;
                }
                if (count >= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean touchesLiquid(BlockGetter level, BlockPos pos) {
        boolean bl = false;
        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
        BlockState blockState = level.getBlockState(mutableBlockPos);
        if (this.isWater(blockState)) {
            return true;
        } else {
            for (Direction direction : Direction.values()) {
                if (direction != Direction.DOWN) {
                    mutableBlockPos.setWithOffset(pos, direction);
                    blockState = level.getBlockState(mutableBlockPos);
                    if (this.isWater(blockState) && !blockState.m_60783_(level, pos, direction.getOpposite())) {
                        bl = true;
                        break;
                    }
                }
            }
            return bl;
        }
    }

    private boolean isWater(BlockState state) {
        return state.m_60819_().is(FluidTags.WATER);
    }

    public void spawnDissolveParticles(Level level, BlockPos pos) {
        int d = 0;
        int e = 0;
        int f = 0;
        int amount = 4;
        for (int ax = 0; ax < amount; ax++) {
            for (int ay = 0; ay < amount; ay++) {
                for (int az = 0; az < amount; az++) {
                    double s = ((double) ax + 0.5) / (double) amount;
                    double t = ((double) ay + 0.5) / (double) amount;
                    double u = ((double) az + 0.5) / (double) amount;
                    double px = s + (double) d;
                    double py = t + (double) e;
                    double pz = u + (double) f;
                    level.addParticle((ParticleOptions) ModParticles.SUGAR_PARTICLE.get(), (double) pos.m_123341_() + px, (double) pos.m_123342_() + py, (double) pos.m_123343_() + pz, s - 0.5, 0.0, u - 0.5);
                }
            }
        }
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        return state.m_284242_(level, pos).col;
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        if (level.isClientSide) {
            this.spawnDissolveParticles(level, pos);
        }
        SoundType soundtype = state.m_60827_();
        level.playSound(null, pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
    }
}