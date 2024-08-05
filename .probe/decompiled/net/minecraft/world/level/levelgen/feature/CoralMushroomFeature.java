package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CoralMushroomFeature extends CoralFeature {

    public CoralMushroomFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    protected boolean placeFeature(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        int $$4 = randomSource1.nextInt(3) + 3;
        int $$5 = randomSource1.nextInt(3) + 3;
        int $$6 = randomSource1.nextInt(3) + 3;
        int $$7 = randomSource1.nextInt(3) + 1;
        BlockPos.MutableBlockPos $$8 = blockPos2.mutable();
        for (int $$9 = 0; $$9 <= $$5; $$9++) {
            for (int $$10 = 0; $$10 <= $$4; $$10++) {
                for (int $$11 = 0; $$11 <= $$6; $$11++) {
                    $$8.set($$9 + blockPos2.m_123341_(), $$10 + blockPos2.m_123342_(), $$11 + blockPos2.m_123343_());
                    $$8.move(Direction.DOWN, $$7);
                    if (($$9 != 0 && $$9 != $$5 || $$10 != 0 && $$10 != $$4) && ($$11 != 0 && $$11 != $$6 || $$10 != 0 && $$10 != $$4) && ($$9 != 0 && $$9 != $$5 || $$11 != 0 && $$11 != $$6) && ($$9 == 0 || $$9 == $$5 || $$10 == 0 || $$10 == $$4 || $$11 == 0 || $$11 == $$6) && !(randomSource1.nextFloat() < 0.1F) && !this.m_224973_(levelAccessor0, randomSource1, $$8, blockState3)) {
                    }
                }
            }
        }
        return true;
    }
}