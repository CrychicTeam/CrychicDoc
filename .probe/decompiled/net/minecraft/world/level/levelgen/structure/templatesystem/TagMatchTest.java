package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TagMatchTest extends RuleTest {

    public static final Codec<TagMatchTest> CODEC = TagKey.codec(Registries.BLOCK).fieldOf("tag").xmap(TagMatchTest::new, p_205065_ -> p_205065_.tag).codec();

    private final TagKey<Block> tag;

    public TagMatchTest(TagKey<Block> tagKeyBlock0) {
        this.tag = tagKeyBlock0;
    }

    @Override
    public boolean test(BlockState blockState0, RandomSource randomSource1) {
        return blockState0.m_204336_(this.tag);
    }

    @Override
    protected RuleTestType<?> getType() {
        return RuleTestType.TAG_TEST;
    }
}