package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class SculkBlock extends DropExperienceBlock implements SculkBehaviour {

    public SculkBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0, ConstantInt.of(1));
    }

    @Override
    public int attemptUseCharge(SculkSpreader.ChargeCursor sculkSpreaderChargeCursor0, LevelAccessor levelAccessor1, BlockPos blockPos2, RandomSource randomSource3, SculkSpreader sculkSpreader4, boolean boolean5) {
        int $$6 = sculkSpreaderChargeCursor0.getCharge();
        if ($$6 != 0 && randomSource3.nextInt(sculkSpreader4.chargeDecayRate()) == 0) {
            BlockPos $$7 = sculkSpreaderChargeCursor0.getPos();
            boolean $$8 = $$7.m_123314_(blockPos2, (double) sculkSpreader4.noGrowthRadius());
            if (!$$8 && canPlaceGrowth(levelAccessor1, $$7)) {
                int $$9 = sculkSpreader4.growthSpawnCost();
                if (randomSource3.nextInt($$9) < $$6) {
                    BlockPos $$10 = $$7.above();
                    BlockState $$11 = this.getRandomGrowthState(levelAccessor1, $$10, randomSource3, sculkSpreader4.isWorldGeneration());
                    levelAccessor1.m_7731_($$10, $$11, 3);
                    levelAccessor1.playSound(null, $$7, $$11.m_60827_().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return Math.max(0, $$6 - $$9);
            } else {
                return randomSource3.nextInt(sculkSpreader4.additionalDecayRate()) != 0 ? $$6 : $$6 - ($$8 ? 1 : getDecayPenalty(sculkSpreader4, $$7, blockPos2, $$6));
            }
        } else {
            return $$6;
        }
    }

    private static int getDecayPenalty(SculkSpreader sculkSpreader0, BlockPos blockPos1, BlockPos blockPos2, int int3) {
        int $$4 = sculkSpreader0.noGrowthRadius();
        float $$5 = Mth.square((float) Math.sqrt(blockPos1.m_123331_(blockPos2)) - (float) $$4);
        int $$6 = Mth.square(24 - $$4);
        float $$7 = Math.min(1.0F, $$5 / (float) $$6);
        return Math.max(1, (int) ((float) int3 * $$7 * 0.5F));
    }

    private BlockState getRandomGrowthState(LevelAccessor levelAccessor0, BlockPos blockPos1, RandomSource randomSource2, boolean boolean3) {
        BlockState $$4;
        if (randomSource2.nextInt(11) == 0) {
            $$4 = (BlockState) Blocks.SCULK_SHRIEKER.defaultBlockState().m_61124_(SculkShriekerBlock.CAN_SUMMON, boolean3);
        } else {
            $$4 = Blocks.SCULK_SENSOR.defaultBlockState();
        }
        return $$4.m_61138_(BlockStateProperties.WATERLOGGED) && !levelAccessor0.m_6425_(blockPos1).isEmpty() ? (BlockState) $$4.m_61124_(BlockStateProperties.WATERLOGGED, true) : $$4;
    }

    private static boolean canPlaceGrowth(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        BlockState $$2 = levelAccessor0.m_8055_(blockPos1.above());
        if ($$2.m_60795_() || $$2.m_60713_(Blocks.WATER) && $$2.m_60819_().is(Fluids.WATER)) {
            int $$3 = 0;
            for (BlockPos $$4 : BlockPos.betweenClosed(blockPos1.offset(-4, 0, -4), blockPos1.offset(4, 2, 4))) {
                BlockState $$5 = levelAccessor0.m_8055_($$4);
                if ($$5.m_60713_(Blocks.SCULK_SENSOR) || $$5.m_60713_(Blocks.SCULK_SHRIEKER)) {
                    $$3++;
                }
                if ($$3 > 2) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canChangeBlockStateOnSpread() {
        return false;
    }
}