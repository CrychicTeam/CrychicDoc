package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class RandomBlockStateMatchTest extends RuleTest {

    public static final Codec<RandomBlockStateMatchTest> CODEC = RecordCodecBuilder.create(p_74287_ -> p_74287_.group(BlockState.CODEC.fieldOf("block_state").forGetter(p_163770_ -> p_163770_.blockState), Codec.FLOAT.fieldOf("probability").forGetter(p_163768_ -> p_163768_.probability)).apply(p_74287_, RandomBlockStateMatchTest::new));

    private final BlockState blockState;

    private final float probability;

    public RandomBlockStateMatchTest(BlockState blockState0, float float1) {
        this.blockState = blockState0;
        this.probability = float1;
    }

    @Override
    public boolean test(BlockState blockState0, RandomSource randomSource1) {
        return blockState0 == this.blockState && randomSource1.nextFloat() < this.probability;
    }

    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.RANDOM_BLOCKSTATE_TEST;
    }
}