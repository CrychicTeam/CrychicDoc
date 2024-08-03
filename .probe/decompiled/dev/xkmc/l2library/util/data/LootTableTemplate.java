package dev.xkmc.l2library.util.data;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;

public class LootTableTemplate {

    public static LootPool.Builder getPool(int roll, int bonus) {
        return LootPool.lootPool().setRolls(ConstantValue.exactly((float) roll)).setBonusRolls(ConstantValue.exactly(0.0F));
    }

    public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int count) {
        return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly((float) count)));
    }

    public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int min, int max) {
        return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between((float) min, (float) max)));
    }

    public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int min, int max, int add) {
        return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between((float) min, (float) max))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, (float) add)));
    }

    public static LootItemCondition.Builder byPlayer() {
        return LootItemKilledByPlayerCondition.killedByPlayer();
    }

    public static LootItemCondition.Builder chance(float chance) {
        return LootItemRandomChanceCondition.randomChance(chance);
    }

    public static LootItemCondition.Builder chance(float chance, float add) {
        return LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(chance, add);
    }

    public static LootItemBlockStatePropertyCondition.Builder withBlockState(Block block, Property<Integer> prop, int low, int high) {
        StatePropertiesPredicate.Builder builder = StatePropertiesPredicate.Builder.properties();
        builder.matchers.add(new StatePropertiesPredicate.RangedPropertyMatcher(prop.getName(), Integer.toString(low), Integer.toString(high)));
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(builder);
    }

    public static LootItemBlockStatePropertyCondition.Builder withBlockState(Block block, Property<Integer> prop, int val) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(prop, val));
    }

    public static LootItemBlockStatePropertyCondition.Builder withBlockState(Block block, Property<Boolean> prop, boolean val) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(prop, val));
    }

    public static LootItemBlockStatePropertyCondition.Builder withBlockState(Block block, Property<?> prop, String val) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(prop, val));
    }

    public static LootPoolSingletonContainer.Builder<?> cropDrop(Item item) {
        return LootItem.lootTableItem(item).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.57F, 3));
    }

    public static EnchantmentPredicate hasEnchantment(Enchantment enchant, int min) {
        return new EnchantmentPredicate(enchant, MinMaxBounds.Ints.atLeast(min));
    }

    public static LootItemCondition.Builder shearOrSilk(boolean inverted) {
        AnyOfCondition.Builder ans = AnyOfCondition.anyOf(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS)), MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(hasEnchantment(Enchantments.SILK_TOUCH, 1))));
        return (LootItemCondition.Builder) (inverted ? ans.m_81807_() : ans);
    }

    public static LootItemCondition.Builder silk(boolean inverted) {
        LootItemCondition.Builder ans = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(hasEnchantment(Enchantments.SILK_TOUCH, 1)));
        return inverted ? InvertedLootItemCondition.invert(ans) : ans;
    }

    public static LootTable.Builder selfOrOther(Block block, Block base, Item other, int count) {
        return LootTable.lootTable().withPool(getPool(1, 0).add(getItem(base.asItem(), 1)).when(ExplosionCondition.survivesExplosion()).when(silk(true))).withPool(getPool(1, 0).add(getItem(other, count)).when(ExplosionCondition.survivesExplosion()).when(silk(true))).withPool(getPool(1, 0).add(getItem(block.asItem(), 1)).when(silk(false)));
    }
}