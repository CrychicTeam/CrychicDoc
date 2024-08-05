package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;

public class CountPlacement extends RepeatingPlacement {

    public static final Codec<CountPlacement> CODEC = IntProvider.codec(0, 256).fieldOf("count").xmap(CountPlacement::new, p_191633_ -> p_191633_.count).codec();

    private final IntProvider count;

    private CountPlacement(IntProvider intProvider0) {
        this.count = intProvider0;
    }

    public static CountPlacement of(IntProvider intProvider0) {
        return new CountPlacement(intProvider0);
    }

    public static CountPlacement of(int int0) {
        return of(ConstantInt.of(int0));
    }

    @Override
    protected int count(RandomSource randomSource0, BlockPos blockPos1) {
        return this.count.sample(randomSource0);
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.COUNT;
    }
}