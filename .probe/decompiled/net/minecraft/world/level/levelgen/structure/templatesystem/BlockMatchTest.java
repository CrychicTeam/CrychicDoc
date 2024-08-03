package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockMatchTest extends RuleTest {

    public static final Codec<BlockMatchTest> CODEC = BuiltInRegistries.BLOCK.m_194605_().fieldOf("block").xmap(BlockMatchTest::new, p_74073_ -> p_74073_.block).codec();

    private final Block block;

    public BlockMatchTest(Block block0) {
        this.block = block0;
    }

    @Override
    public boolean test(BlockState blockState0, RandomSource randomSource1) {
        return blockState0.m_60713_(this.block);
    }

    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.BLOCK_TEST;
    }
}