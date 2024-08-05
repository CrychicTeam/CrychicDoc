package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class AxisAlignedLinearPosTest extends PosRuleTest {

    public static final Codec<AxisAlignedLinearPosTest> CODEC = RecordCodecBuilder.create(p_73977_ -> p_73977_.group(Codec.FLOAT.fieldOf("min_chance").orElse(0.0F).forGetter(p_163719_ -> p_163719_.minChance), Codec.FLOAT.fieldOf("max_chance").orElse(0.0F).forGetter(p_163717_ -> p_163717_.maxChance), Codec.INT.fieldOf("min_dist").orElse(0).forGetter(p_163715_ -> p_163715_.minDist), Codec.INT.fieldOf("max_dist").orElse(0).forGetter(p_163713_ -> p_163713_.maxDist), Direction.Axis.CODEC.fieldOf("axis").orElse(Direction.Axis.Y).forGetter(p_163711_ -> p_163711_.axis)).apply(p_73977_, AxisAlignedLinearPosTest::new));

    private final float minChance;

    private final float maxChance;

    private final int minDist;

    private final int maxDist;

    private final Direction.Axis axis;

    public AxisAlignedLinearPosTest(float float0, float float1, int int2, int int3, Direction.Axis directionAxis4) {
        if (int2 >= int3) {
            throw new IllegalArgumentException("Invalid range: [" + int2 + "," + int3 + "]");
        } else {
            this.minChance = float0;
            this.maxChance = float1;
            this.minDist = int2;
            this.maxDist = int3;
            this.axis = directionAxis4;
        }
    }

    @Override
    public boolean test(BlockPos blockPos0, BlockPos blockPos1, BlockPos blockPos2, RandomSource randomSource3) {
        Direction $$4 = Direction.get(Direction.AxisDirection.POSITIVE, this.axis);
        float $$5 = (float) Math.abs((blockPos1.m_123341_() - blockPos2.m_123341_()) * $$4.getStepX());
        float $$6 = (float) Math.abs((blockPos1.m_123342_() - blockPos2.m_123342_()) * $$4.getStepY());
        float $$7 = (float) Math.abs((blockPos1.m_123343_() - blockPos2.m_123343_()) * $$4.getStepZ());
        int $$8 = (int) ($$5 + $$6 + $$7);
        float $$9 = randomSource3.nextFloat();
        return $$9 <= Mth.clampedLerp(this.minChance, this.maxChance, Mth.inverseLerp((float) $$8, (float) this.minDist, (float) this.maxDist));
    }

    @Override
    protected PosRuleTestType<?> getType() {
        return PosRuleTestType.AXIS_ALIGNED_LINEAR_POS_TEST;
    }
}