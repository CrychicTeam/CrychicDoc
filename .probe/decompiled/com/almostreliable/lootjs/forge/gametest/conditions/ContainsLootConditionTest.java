package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.ContainsLootCondition;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("lootjs")
@PrefixGameTestTemplate(false)
public class ContainsLootConditionTest {

    private static final Vec3 TEST_POS = new Vec3(0.0, 0.0, 0.0);

    @GameTest(m_177046_ = "empty_test_structure")
    public void nonExactCheck(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        GameTestUtils.fillExampleLoot(ctx, Items.DIAMOND, Items.DIAMOND_HELMET);
        ContainsLootCondition c = new ContainsLootCondition(itemStack -> itemStack.getItem() == Items.DIAMOND, false);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, c.test(ctx), "Should pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void exactCheck(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        GameTestUtils.fillExampleLoot(ctx, Items.DIAMOND, Items.DIAMOND_HELMET);
        ContainsLootCondition c = new ContainsLootCondition(itemStack -> itemStack.getItem() == Items.DIAMOND || itemStack.getItem() == Items.DIAMOND_HELMET, true);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, c.test(ctx), "Should pass"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void exactCheck_fail(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        GameTestUtils.fillExampleLoot(ctx, Items.DIAMOND, Items.DIAMOND_HELMET);
        ContainsLootCondition c = new ContainsLootCondition(ItemFilter.ARMOR, true);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, c.test(ctx), "Should fail, because of diamond is not armor"));
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void simpleCheck_fail(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        GameTestUtils.fillExampleLoot(ctx, Items.DIAMOND, Items.DIAMOND_HELMET);
        ContainsLootCondition c = new ContainsLootCondition(itemStack -> itemStack.getItem() == Items.NETHER_BRICK, false);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, c.test(ctx), "Should fail"));
    }
}