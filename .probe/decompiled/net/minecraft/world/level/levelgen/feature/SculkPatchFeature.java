package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;

public class SculkPatchFeature extends Feature<SculkPatchConfiguration> {

    public SculkPatchFeature(Codec<SculkPatchConfiguration> codecSculkPatchConfiguration0) {
        super(codecSculkPatchConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<SculkPatchConfiguration> featurePlaceContextSculkPatchConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextSculkPatchConfiguration0.level();
        BlockPos $$2 = featurePlaceContextSculkPatchConfiguration0.origin();
        if (!this.canSpreadFrom($$1, $$2)) {
            return false;
        } else {
            SculkPatchConfiguration $$3 = featurePlaceContextSculkPatchConfiguration0.config();
            RandomSource $$4 = featurePlaceContextSculkPatchConfiguration0.random();
            SculkSpreader $$5 = SculkSpreader.createWorldGenSpreader();
            int $$6 = $$3.spreadRounds() + $$3.growthRounds();
            for (int $$7 = 0; $$7 < $$6; $$7++) {
                for (int $$8 = 0; $$8 < $$3.chargeCount(); $$8++) {
                    $$5.addCursors($$2, $$3.amountPerCharge());
                }
                boolean $$9 = $$7 < $$3.spreadRounds();
                for (int $$10 = 0; $$10 < $$3.spreadAttempts(); $$10++) {
                    $$5.updateCursors($$1, $$2, $$4, $$9);
                }
                $$5.clear();
            }
            BlockPos $$11 = $$2.below();
            if ($$4.nextFloat() <= $$3.catalystChance() && $$1.m_8055_($$11).m_60838_($$1, $$11)) {
                $$1.m_7731_($$2, Blocks.SCULK_CATALYST.defaultBlockState(), 3);
            }
            int $$12 = $$3.extraRareGrowths().sample($$4);
            for (int $$13 = 0; $$13 < $$12; $$13++) {
                BlockPos $$14 = $$2.offset($$4.nextInt(5) - 2, 0, $$4.nextInt(5) - 2);
                if ($$1.m_8055_($$14).m_60795_() && $$1.m_8055_($$14.below()).m_60783_($$1, $$14.below(), Direction.UP)) {
                    $$1.m_7731_($$14, (BlockState) Blocks.SCULK_SHRIEKER.defaultBlockState().m_61124_(SculkShriekerBlock.CAN_SUMMON, true), 3);
                }
            }
            return true;
        }
    }

    private boolean canSpreadFrom(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        BlockState $$2 = levelAccessor0.m_8055_(blockPos1);
        if ($$2.m_60734_() instanceof SculkBehaviour) {
            return true;
        } else {
            return !$$2.m_60795_() && (!$$2.m_60713_(Blocks.WATER) || !$$2.m_60819_().isSource()) ? false : Direction.stream().map(blockPos1::m_121945_).anyMatch(p_225245_ -> levelAccessor0.m_8055_(p_225245_).m_60838_(levelAccessor0, p_225245_));
        }
    }
}