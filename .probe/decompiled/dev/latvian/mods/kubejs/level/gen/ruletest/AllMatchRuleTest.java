package dev.latvian.mods.kubejs.level.gen.ruletest;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class AllMatchRuleTest extends RuleTest {

    public static final Codec<AllMatchRuleTest> CODEC = RuleTest.CODEC.listOf().fieldOf("rules").xmap(AllMatchRuleTest::new, t -> t.rules).codec();

    public final List<RuleTest> rules;

    public AllMatchRuleTest() {
        this(new ArrayList());
    }

    public AllMatchRuleTest(List<RuleTest> rules) {
        this.rules = rules;
    }

    @Override
    public boolean test(BlockState blockState, RandomSource random) {
        for (RuleTest test : this.rules) {
            if (!test.test(blockState, random)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected RuleTestType<?> getType() {
        return KubeJSRuleTests.ALL_MATCH;
    }
}