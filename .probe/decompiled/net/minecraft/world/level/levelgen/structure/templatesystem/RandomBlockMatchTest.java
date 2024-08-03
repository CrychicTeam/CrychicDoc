package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RandomBlockMatchTest extends RuleTest {

    public static final Codec<RandomBlockMatchTest> CODEC = RecordCodecBuilder.create(p_259017_ -> p_259017_.group(BuiltInRegistries.BLOCK.m_194605_().fieldOf("block").forGetter(p_163766_ -> p_163766_.block), Codec.FLOAT.fieldOf("probability").forGetter(p_163764_ -> p_163764_.probability)).apply(p_259017_, RandomBlockMatchTest::new));

    private final Block block;

    private final float probability;

    public RandomBlockMatchTest(Block block0, float float1) {
        this.block = block0;
        this.probability = float1;
    }

    @Override
    public boolean test(BlockState blockState0, RandomSource randomSource1) {
        return blockState0.m_60713_(this.block) && randomSource1.nextFloat() < this.probability;
    }

    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.RANDOM_BLOCK_TEST;
    }
}