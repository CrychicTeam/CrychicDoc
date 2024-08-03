package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateMatchTest extends RuleTest {

    public static final Codec<BlockStateMatchTest> CODEC = BlockState.CODEC.fieldOf("block_state").xmap(BlockStateMatchTest::new, p_74099_ -> p_74099_.blockState).codec();

    private final BlockState blockState;

    public BlockStateMatchTest(BlockState blockState0) {
        this.blockState = blockState0;
    }

    @Override
    public boolean test(BlockState blockState0, RandomSource randomSource1) {
        return blockState0 == this.blockState;
    }

    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.BLOCKSTATE_TEST;
    }
}