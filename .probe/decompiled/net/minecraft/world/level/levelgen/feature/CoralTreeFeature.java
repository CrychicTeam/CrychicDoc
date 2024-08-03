package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CoralTreeFeature extends CoralFeature {

    public CoralTreeFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    protected boolean placeFeature(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        BlockPos.MutableBlockPos $$4 = blockPos2.mutable();
        int $$5 = randomSource1.nextInt(3) + 1;
        for (int $$6 = 0; $$6 < $$5; $$6++) {
            if (!this.m_224973_(levelAccessor0, randomSource1, $$4, blockState3)) {
                return true;
            }
            $$4.move(Direction.UP);
        }
        BlockPos $$7 = $$4.immutable();
        int $$8 = randomSource1.nextInt(3) + 2;
        List<Direction> $$9 = Direction.Plane.HORIZONTAL.shuffledCopy(randomSource1);
        for (Direction $$11 : $$9.subList(0, $$8)) {
            $$4.set($$7);
            $$4.move($$11);
            int $$12 = randomSource1.nextInt(5) + 2;
            int $$13 = 0;
            for (int $$14 = 0; $$14 < $$12 && this.m_224973_(levelAccessor0, randomSource1, $$4, blockState3); $$14++) {
                $$13++;
                $$4.move(Direction.UP);
                if ($$14 == 0 || $$13 >= 2 && randomSource1.nextFloat() < 0.25F) {
                    $$4.move($$11);
                    $$13 = 0;
                }
            }
        }
        return true;
    }
}