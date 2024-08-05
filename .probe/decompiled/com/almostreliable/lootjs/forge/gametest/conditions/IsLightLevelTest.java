package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.IsLightLevel;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("lootjs")
@PrefixGameTestTemplate(false)
public class IsLightLevelTest {

    private static final Vec3 TEST_POS = new Vec3(0.0, 1.0, 0.0);

    @GameTest(m_177046_ = "empty_test_structure")
    public void matchLight(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        IsLightLevel isLightLevel = new IsLightLevel(0, 15);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, isLightLevel.test(ctx), "IsLightLevel check should pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void failMatchLight(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        IsLightLevel isLightLevel = new IsLightLevel(15, 15);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, isLightLevel.test(ctx), "IsLightLevel check should fail"));
    }
}