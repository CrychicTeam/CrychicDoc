package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;

public class RandomOffsetPlacement extends PlacementModifier {

    public static final Codec<RandomOffsetPlacement> CODEC = RecordCodecBuilder.create(p_191883_ -> p_191883_.group(IntProvider.codec(-16, 16).fieldOf("xz_spread").forGetter(p_191894_ -> p_191894_.xzSpread), IntProvider.codec(-16, 16).fieldOf("y_spread").forGetter(p_191885_ -> p_191885_.ySpread)).apply(p_191883_, RandomOffsetPlacement::new));

    private final IntProvider xzSpread;

    private final IntProvider ySpread;

    public static RandomOffsetPlacement of(IntProvider intProvider0, IntProvider intProvider1) {
        return new RandomOffsetPlacement(intProvider0, intProvider1);
    }

    public static RandomOffsetPlacement vertical(IntProvider intProvider0) {
        return new RandomOffsetPlacement(ConstantInt.of(0), intProvider0);
    }

    public static RandomOffsetPlacement horizontal(IntProvider intProvider0) {
        return new RandomOffsetPlacement(intProvider0, ConstantInt.of(0));
    }

    private RandomOffsetPlacement(IntProvider intProvider0, IntProvider intProvider1) {
        this.xzSpread = intProvider0;
        this.ySpread = intProvider1;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        int $$3 = blockPos2.m_123341_() + this.xzSpread.sample(randomSource1);
        int $$4 = blockPos2.m_123342_() + this.ySpread.sample(randomSource1);
        int $$5 = blockPos2.m_123343_() + this.xzSpread.sample(randomSource1);
        return Stream.of(new BlockPos($$3, $$4, $$5));
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.RANDOM_OFFSET;
    }
}