package dev.xkmc.l2library.serial.advancements;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;

public class CriterionBuilder implements IAdvBuilder {

    private final RequirementsStrategy req;

    private final List<Pair<String, CriterionTriggerInstance>> list = new ArrayList();

    @Deprecated
    public static CriterionBuilder none() {
        return one(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.ANY));
    }

    public static CriterionBuilder item(Item item) {
        return one(InventoryChangeTrigger.TriggerInstance.hasItems(item));
    }

    public static CriterionBuilder item(TagKey<Item> item, CompoundTag tag) {
        return one(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).hasNbt(tag).build()));
    }

    public static CriterionBuilder item(ItemLike item, CompoundTag tag) {
        return one(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).hasNbt(tag).build()));
    }

    public static CriterionBuilder items(Item... item) {
        return one(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).build()));
    }

    public static CriterionBuilder item(TagKey<Item> item) {
        return one(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).build()));
    }

    public static CriterionBuilder book(Enchantment enchantment) {
        return one(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().hasStoredEnchantment(new EnchantmentPredicate(enchantment, MinMaxBounds.Ints.ANY)).build()));
    }

    public static CriterionBuilder enchanted(Enchantment enchantment) {
        return one(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(enchantment, MinMaxBounds.Ints.ANY)).build()));
    }

    public static CriterionBuilder enchanted(ItemLike item, Enchantment enchantment) {
        return one(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).hasEnchantment(new EnchantmentPredicate(enchantment, MinMaxBounds.Ints.ANY)).build()));
    }

    public static CriterionBuilder enchanted(TagKey<Item> item, Enchantment enchantment) {
        return one(InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).hasEnchantment(new EnchantmentPredicate(enchantment, MinMaxBounds.Ints.ANY)).build()));
    }

    public static CriterionBuilder one(CriterionTriggerInstance instance) {
        return new CriterionBuilder(RequirementsStrategy.AND).add(instance);
    }

    public static CriterionBuilder and() {
        return new CriterionBuilder(RequirementsStrategy.AND);
    }

    public static CriterionBuilder or() {
        return new CriterionBuilder(RequirementsStrategy.OR);
    }

    private CriterionBuilder(RequirementsStrategy req) {
        this.req = req;
    }

    public CriterionBuilder add(CriterionTriggerInstance instance) {
        return this.add(this.list.size() + "", instance);
    }

    public CriterionBuilder add(String str, CriterionTriggerInstance instance) {
        this.list.add(Pair.of(str, instance));
        return this;
    }

    @Override
    public void modify(String id, Advancement.Builder builder) {
        if (this.list.size() > 1) {
            builder.requirements(this.req);
        }
        for (Pair<String, CriterionTriggerInstance> c : this.list) {
            builder.addCriterion((String) c.getFirst(), (CriterionTriggerInstance) c.getSecond());
        }
    }
}