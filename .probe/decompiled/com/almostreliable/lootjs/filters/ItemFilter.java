package com.almostreliable.lootjs.filters;

import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public interface ItemFilter extends Predicate<ItemStack> {

    ItemFilter ALWAYS_FALSE = itemStack -> false;

    ItemFilter ALWAYS_TRUE = itemStack -> true;

    ItemFilter SWORD = itemStack -> itemStack.getItem() instanceof SwordItem;

    ItemFilter PICKAXE = itemStack -> itemStack.getItem() instanceof PickaxeItem;

    ItemFilter AXE = itemStack -> itemStack.getItem() instanceof AxeItem;

    ItemFilter SHOVEL = itemStack -> itemStack.getItem() instanceof ShovelItem;

    ItemFilter HOE = itemStack -> itemStack.getItem() instanceof HoeItem;

    ItemFilter TOOL = itemStack -> itemStack.getItem() instanceof DiggerItem;

    ItemFilter POTION = itemStack -> itemStack.getItem() instanceof PotionItem;

    ItemFilter HAS_TIER = itemStack -> itemStack.getItem() instanceof TieredItem;

    ItemFilter PROJECTILE_WEAPON = itemStack -> itemStack.getItem() instanceof ProjectileWeaponItem;

    ItemFilter ARMOR = itemStack -> itemStack.getItem() instanceof ArmorItem;

    ItemFilter WEAPON = itemStack -> {
        Item i = itemStack.getItem();
        return i instanceof SwordItem || i instanceof DiggerItem || i instanceof ProjectileWeaponItem || i instanceof TridentItem;
    };

    ItemFilter HEAD_ARMOR = equipmentSlot(EquipmentSlot.HEAD);

    ItemFilter CHEST_ARMOR = equipmentSlot(EquipmentSlot.CHEST);

    ItemFilter LEGS_ARMOR = equipmentSlot(EquipmentSlot.LEGS);

    ItemFilter FEET_ARMOR = equipmentSlot(EquipmentSlot.FEET);

    ItemFilter FOOD = ItemStack::m_41614_;

    ItemFilter DAMAGEABLE = ItemStack::m_41763_;

    ItemFilter DAMAGED = ItemStack::m_41768_;

    ItemFilter ENCHANTABLE = ItemStack::m_41792_;

    ItemFilter ENCHANTED = ItemStack::m_41793_;

    ItemFilter BLOCK = itemStack -> itemStack.getItem() instanceof BlockItem;

    static ItemFilter hasEnchantment(ResourceLocationFilter filter) {
        return hasEnchantment(filter, 1, 255);
    }

    static ItemFilter hasEnchantment(ResourceLocationFilter filter, int min, int max) {
        return itemStack -> {
            ListTag listTag = itemStack.is(Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantments(itemStack) : itemStack.getEnchantmentTags();
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag tag = listTag.getCompound(i);
                ResourceLocation id = EnchantmentHelper.getEnchantmentId(tag);
                int level = EnchantmentHelper.getEnchantmentLevel(tag);
                if (id != null && filter.test(id) && min <= level && level <= max) {
                    return true;
                }
            }
            return false;
        };
    }

    static ItemFilter equipmentSlot(EquipmentSlot slot) {
        return itemStack -> LivingEntity.getEquipmentSlotForItem(itemStack) == slot;
    }

    static ItemFilter and(ItemFilter... itemFilters) {
        Objects.requireNonNull(itemFilters);
        return switch(itemFilters.length) {
            case 0 ->
                ALWAYS_TRUE;
            case 1 ->
                itemFilters[0];
            case 2 ->
                itemFilters[0].and(itemFilters[1]);
            default ->
                itemStack -> {
                    for (ItemFilter itemFilter : itemFilters) {
                        if (!itemFilter.test(itemStack)) {
                            return false;
                        }
                    }
                    return true;
                };
        };
    }

    static ItemFilter not(ItemFilter itemFilter) {
        return itemFilter.negate();
    }

    static ItemFilter or(ItemFilter... itemFilters) {
        Objects.requireNonNull(itemFilters);
        return switch(itemFilters.length) {
            case 0 ->
                ALWAYS_FALSE;
            case 1 ->
                itemFilters[0];
            case 2 ->
                itemFilters[0].or(itemFilters[1]);
            default ->
                itemStack -> {
                    for (ItemFilter itemFilter : itemFilters) {
                        if (itemFilter.test(itemStack)) {
                            return true;
                        }
                    }
                    return false;
                };
        };
    }

    static ItemFilter custom(Predicate<ItemStack> predicate) {
        return predicate::test;
    }

    boolean test(ItemStack var1);

    default ItemFilter and(ItemFilter other) {
        Objects.requireNonNull(other);
        return itemStack -> this.test(itemStack) && other.test(itemStack);
    }

    default ItemFilter negate() {
        return itemS -> !this.test(itemS);
    }

    default ItemFilter or(ItemFilter other) {
        Objects.requireNonNull(other);
        return itemStack -> this.test(itemStack) || other.test(itemStack);
    }
}