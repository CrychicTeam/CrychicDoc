package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WetSpongeBlock extends Block {

    protected WetSpongeBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (level1.dimensionType().ultraWarm()) {
            level1.setBlock(blockPos2, Blocks.SPONGE.defaultBlockState(), 3);
            level1.m_46796_(2009, blockPos2, 0);
            level1.playSound(null, blockPos2, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (1.0F + level1.getRandom().nextFloat() * 0.2F) * 0.7F);
        }
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        Direction $$4 = Direction.getRandom(randomSource3);
        if ($$4 != Direction.UP) {
            BlockPos $$5 = blockPos2.relative($$4);
            BlockState $$6 = level1.getBlockState($$5);
            if (!blockState0.m_60815_() || !$$6.m_60783_(level1, $$5, $$4.getOpposite())) {
                double $$7 = (double) blockPos2.m_123341_();
                double $$8 = (double) blockPos2.m_123342_();
                double $$9 = (double) blockPos2.m_123343_();
                if ($$4 == Direction.DOWN) {
                    $$8 -= 0.05;
                    $$7 += randomSource3.nextDouble();
                    $$9 += randomSource3.nextDouble();
                } else {
                    $$8 += randomSource3.nextDouble() * 0.8;
                    if ($$4.getAxis() == Direction.Axis.X) {
                        $$9 += randomSource3.nextDouble();
                        if ($$4 == Direction.EAST) {
                            $$7++;
                        } else {
                            $$7 += 0.05;
                        }
                    } else {
                        $$7 += randomSource3.nextDouble();
                        if ($$4 == Direction.SOUTH) {
                            $$9++;
                        } else {
                            $$9 += 0.05;
                        }
                    }
                }
                level1.addParticle(ParticleTypes.DRIPPING_WATER, $$7, $$8, $$9, 0.0, 0.0, 0.0);
            }
        }
    }
}