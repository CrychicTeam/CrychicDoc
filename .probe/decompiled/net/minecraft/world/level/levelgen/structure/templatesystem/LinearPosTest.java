package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class LinearPosTest extends PosRuleTest {

    public static final Codec<LinearPosTest> CODEC = RecordCodecBuilder.create(p_74160_ -> p_74160_.group(Codec.FLOAT.fieldOf("min_chance").orElse(0.0F).forGetter(p_163737_ -> p_163737_.minChance), Codec.FLOAT.fieldOf("max_chance").orElse(0.0F).forGetter(p_163735_ -> p_163735_.maxChance), Codec.INT.fieldOf("min_dist").orElse(0).forGetter(p_163733_ -> p_163733_.minDist), Codec.INT.fieldOf("max_dist").orElse(0).forGetter(p_163731_ -> p_163731_.maxDist)).apply(p_74160_, LinearPosTest::new));

    private final float minChance;

    private final float maxChance;

    private final int minDist;

    private final int maxDist;

    public LinearPosTest(float float0, float float1, int int2, int int3) {
        if (int2 >= int3) {
            throw new IllegalArgumentException("Invalid range: [" + int2 + "," + int3 + "]");
        } else {
            this.minChance = float0;
            this.maxChance = float1;
            this.minDist = int2;
            this.maxDist = int3;
        }
    }

    @Override
    public boolean test(BlockPos blockPos0, BlockPos blockPos1, BlockPos blockPos2, RandomSource randomSource3) {
        int $$4 = blockPos1.m_123333_(blockPos2);
        float $$5 = randomSource3.nextFloat();
        return $$5 <= Mth.clampedLerp(this.minChance, this.maxChance, Mth.inverseLerp((float) $$4, (float) this.minDist, (float) this.maxDist));
    }

    @Override
    protected PosRuleTestType<?> getType() {
        return PosRuleTestType.LINEAR_POS_TEST;
    }
}