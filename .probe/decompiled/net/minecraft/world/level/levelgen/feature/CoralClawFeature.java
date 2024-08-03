package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CoralClawFeature extends CoralFeature {

    public CoralClawFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    protected boolean placeFeature(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        if (!this.m_224973_(levelAccessor0, randomSource1, blockPos2, blockState3)) {
            return false;
        } else {
            Direction $$4 = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource1);
            int $$5 = randomSource1.nextInt(2) + 2;
            List<Direction> $$6 = Util.toShuffledList(Stream.of($$4, $$4.getClockWise(), $$4.getCounterClockWise()), randomSource1);
            for (Direction $$8 : $$6.subList(0, $$5)) {
                BlockPos.MutableBlockPos $$9 = blockPos2.mutable();
                int $$10 = randomSource1.nextInt(2) + 1;
                $$9.move($$8);
                int $$12;
                Direction $$11;
                if ($$8 == $$4) {
                    $$11 = $$4;
                    $$12 = randomSource1.nextInt(3) + 2;
                } else {
                    $$9.move(Direction.UP);
                    Direction[] $$13 = new Direction[] { $$8, Direction.UP };
                    $$11 = Util.getRandom($$13, randomSource1);
                    $$12 = randomSource1.nextInt(3) + 3;
                }
                for (int $$16 = 0; $$16 < $$10 && this.m_224973_(levelAccessor0, randomSource1, $$9, blockState3); $$16++) {
                    $$9.move($$11);
                }
                $$9.move($$11.getOpposite());
                $$9.move(Direction.UP);
                for (int $$17 = 0; $$17 < $$12; $$17++) {
                    $$9.move($$4);
                    if (!this.m_224973_(levelAccessor0, randomSource1, $$9, blockState3)) {
                        break;
                    }
                    if (randomSource1.nextFloat() < 0.25F) {
                        $$9.move(Direction.UP);
                    }
                }
            }
            return true;
        }
    }
}