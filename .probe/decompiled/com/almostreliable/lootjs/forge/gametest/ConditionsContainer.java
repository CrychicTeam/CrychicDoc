package com.almostreliable.lootjs.forge.gametest;

import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.kube.LootConditionsContainer;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("lootjs")
@PrefixGameTestTemplate(false)
public class ConditionsContainer {

    @GameTest(m_177046_ = "empty_test_structure")
    public void entityTarget_Entity(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ConditionsContainer.TestLootConditionsContainer conditions = new ConditionsContainer.TestLootConditionsContainer();
            conditions.matchEntity(e -> {
            });
            GameTestUtils.assertEquals(helper, conditions.last().entityTarget, LootContext.EntityTarget.THIS);
        });
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void entityTarget_Killer(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ConditionsContainer.TestLootConditionsContainer conditions = new ConditionsContainer.TestLootConditionsContainer();
            conditions.matchKiller(e -> {
            });
            GameTestUtils.assertEquals(helper, conditions.last().entityTarget, LootContext.EntityTarget.KILLER);
        });
    }

    @GameTest(m_177046_ = "empty_test_structure")
    public void entityTarget_DirectKiller(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ConditionsContainer.TestLootConditionsContainer conditions = new ConditionsContainer.TestLootConditionsContainer();
            conditions.matchDirectKiller(e -> {
            });
            GameTestUtils.assertEquals(helper, conditions.last().entityTarget, LootContext.EntityTarget.DIRECT_KILLER);
        });
    }

    public static class TestLootConditionsContainer implements LootConditionsContainer<ConditionsContainer.TestLootConditionsContainer> {

        private ILootCondition last;

        public ConditionsContainer.TestLootConditionsContainer addCondition(ILootCondition condition) {
            this.last = condition;
            return this;
        }

        public <T extends LootItemCondition> T last() {
            return (T) this.last;
        }
    }
}