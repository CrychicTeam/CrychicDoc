package net.minecraft.world.item.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;

public class EnchantmentHelper {

    private static final String TAG_ENCH_ID = "id";

    private static final String TAG_ENCH_LEVEL = "lvl";

    private static final float SWIFT_SNEAK_EXTRA_FACTOR = 0.15F;

    public static CompoundTag storeEnchantment(@Nullable ResourceLocation resourceLocation0, int int1) {
        CompoundTag $$2 = new CompoundTag();
        $$2.putString("id", String.valueOf(resourceLocation0));
        $$2.putShort("lvl", (short) int1);
        return $$2;
    }

    public static void setEnchantmentLevel(CompoundTag compoundTag0, int int1) {
        compoundTag0.putShort("lvl", (short) int1);
    }

    public static int getEnchantmentLevel(CompoundTag compoundTag0) {
        return Mth.clamp(compoundTag0.getInt("lvl"), 0, 255);
    }

    @Nullable
    public static ResourceLocation getEnchantmentId(CompoundTag compoundTag0) {
        return ResourceLocation.tryParse(compoundTag0.getString("id"));
    }

    @Nullable
    public static ResourceLocation getEnchantmentId(Enchantment enchantment0) {
        return BuiltInRegistries.ENCHANTMENT.getKey(enchantment0);
    }

    public static int getItemEnchantmentLevel(Enchantment enchantment0, ItemStack itemStack1) {
        if (itemStack1.isEmpty()) {
            return 0;
        } else {
            ResourceLocation $$2 = getEnchantmentId(enchantment0);
            ListTag $$3 = itemStack1.getEnchantmentTags();
            for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
                CompoundTag $$5 = $$3.getCompound($$4);
                ResourceLocation $$6 = getEnchantmentId($$5);
                if ($$6 != null && $$6.equals($$2)) {
                    return getEnchantmentLevel($$5);
                }
            }
            return 0;
        }
    }

    public static Map<Enchantment, Integer> getEnchantments(ItemStack itemStack0) {
        ListTag $$1 = itemStack0.is(Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantments(itemStack0) : itemStack0.getEnchantmentTags();
        return deserializeEnchantments($$1);
    }

    public static Map<Enchantment, Integer> deserializeEnchantments(ListTag listTag0) {
        Map<Enchantment, Integer> $$1 = Maps.newLinkedHashMap();
        for (int $$2 = 0; $$2 < listTag0.size(); $$2++) {
            CompoundTag $$3 = listTag0.getCompound($$2);
            BuiltInRegistries.ENCHANTMENT.getOptional(getEnchantmentId($$3)).ifPresent(p_44871_ -> $$1.put(p_44871_, getEnchantmentLevel($$3)));
        }
        return $$1;
    }

    public static void setEnchantments(Map<Enchantment, Integer> mapEnchantmentInteger0, ItemStack itemStack1) {
        ListTag $$2 = new ListTag();
        for (Entry<Enchantment, Integer> $$3 : mapEnchantmentInteger0.entrySet()) {
            Enchantment $$4 = (Enchantment) $$3.getKey();
            if ($$4 != null) {
                int $$5 = (Integer) $$3.getValue();
                $$2.add(storeEnchantment(getEnchantmentId($$4), $$5));
                if (itemStack1.is(Items.ENCHANTED_BOOK)) {
                    EnchantedBookItem.addEnchantment(itemStack1, new EnchantmentInstance($$4, $$5));
                }
            }
        }
        if ($$2.isEmpty()) {
            itemStack1.removeTagKey("Enchantments");
        } else if (!itemStack1.is(Items.ENCHANTED_BOOK)) {
            itemStack1.addTagElement("Enchantments", $$2);
        }
    }

    private static void runIterationOnItem(EnchantmentHelper.EnchantmentVisitor enchantmentHelperEnchantmentVisitor0, ItemStack itemStack1) {
        if (!itemStack1.isEmpty()) {
            ListTag $$2 = itemStack1.getEnchantmentTags();
            for (int $$3 = 0; $$3 < $$2.size(); $$3++) {
                CompoundTag $$4 = $$2.getCompound($$3);
                BuiltInRegistries.ENCHANTMENT.getOptional(getEnchantmentId($$4)).ifPresent(p_182437_ -> enchantmentHelperEnchantmentVisitor0.accept(p_182437_, getEnchantmentLevel($$4)));
            }
        }
    }

    private static void runIterationOnInventory(EnchantmentHelper.EnchantmentVisitor enchantmentHelperEnchantmentVisitor0, Iterable<ItemStack> iterableItemStack1) {
        for (ItemStack $$2 : iterableItemStack1) {
            runIterationOnItem(enchantmentHelperEnchantmentVisitor0, $$2);
        }
    }

    public static int getDamageProtection(Iterable<ItemStack> iterableItemStack0, DamageSource damageSource1) {
        MutableInt $$2 = new MutableInt();
        runIterationOnInventory((p_44892_, p_44893_) -> $$2.add(p_44892_.getDamageProtection(p_44893_, damageSource1)), iterableItemStack0);
        return $$2.intValue();
    }

    public static float getDamageBonus(ItemStack itemStack0, MobType mobType1) {
        MutableFloat $$2 = new MutableFloat();
        runIterationOnItem((p_44887_, p_44888_) -> $$2.add(p_44887_.getDamageBonus(p_44888_, mobType1)), itemStack0);
        return $$2.floatValue();
    }

    public static float getSweepingDamageRatio(LivingEntity livingEntity0) {
        int $$1 = getEnchantmentLevel(Enchantments.SWEEPING_EDGE, livingEntity0);
        return $$1 > 0 ? SweepingEdgeEnchantment.getSweepingDamageRatio($$1) : 0.0F;
    }

    public static void doPostHurtEffects(LivingEntity livingEntity0, Entity entity1) {
        EnchantmentHelper.EnchantmentVisitor $$2 = (p_44902_, p_44903_) -> p_44902_.doPostHurt(livingEntity0, entity1, p_44903_);
        if (livingEntity0 != null) {
            runIterationOnInventory($$2, livingEntity0.m_20158_());
        }
        if (entity1 instanceof Player) {
            runIterationOnItem($$2, livingEntity0.getMainHandItem());
        }
    }

    public static void doPostDamageEffects(LivingEntity livingEntity0, Entity entity1) {
        EnchantmentHelper.EnchantmentVisitor $$2 = (p_44829_, p_44830_) -> p_44829_.doPostAttack(livingEntity0, entity1, p_44830_);
        if (livingEntity0 != null) {
            runIterationOnInventory($$2, livingEntity0.m_20158_());
        }
        if (livingEntity0 instanceof Player) {
            runIterationOnItem($$2, livingEntity0.getMainHandItem());
        }
    }

    public static int getEnchantmentLevel(Enchantment enchantment0, LivingEntity livingEntity1) {
        Iterable<ItemStack> $$2 = enchantment0.getSlotItems(livingEntity1).values();
        if ($$2 == null) {
            return 0;
        } else {
            int $$3 = 0;
            for (ItemStack $$4 : $$2) {
                int $$5 = getItemEnchantmentLevel(enchantment0, $$4);
                if ($$5 > $$3) {
                    $$3 = $$5;
                }
            }
            return $$3;
        }
    }

    public static float getSneakingSpeedBonus(LivingEntity livingEntity0) {
        return (float) getEnchantmentLevel(Enchantments.SWIFT_SNEAK, livingEntity0) * 0.15F;
    }

    public static int getKnockbackBonus(LivingEntity livingEntity0) {
        return getEnchantmentLevel(Enchantments.KNOCKBACK, livingEntity0);
    }

    public static int getFireAspect(LivingEntity livingEntity0) {
        return getEnchantmentLevel(Enchantments.FIRE_ASPECT, livingEntity0);
    }

    public static int getRespiration(LivingEntity livingEntity0) {
        return getEnchantmentLevel(Enchantments.RESPIRATION, livingEntity0);
    }

    public static int getDepthStrider(LivingEntity livingEntity0) {
        return getEnchantmentLevel(Enchantments.DEPTH_STRIDER, livingEntity0);
    }

    public static int getBlockEfficiency(LivingEntity livingEntity0) {
        return getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, livingEntity0);
    }

    public static int getFishingLuckBonus(ItemStack itemStack0) {
        return getItemEnchantmentLevel(Enchantments.FISHING_LUCK, itemStack0);
    }

    public static int getFishingSpeedBonus(ItemStack itemStack0) {
        return getItemEnchantmentLevel(Enchantments.FISHING_SPEED, itemStack0);
    }

    public static int getMobLooting(LivingEntity livingEntity0) {
        return getEnchantmentLevel(Enchantments.MOB_LOOTING, livingEntity0);
    }

    public static boolean hasAquaAffinity(LivingEntity livingEntity0) {
        return getEnchantmentLevel(Enchantments.AQUA_AFFINITY, livingEntity0) > 0;
    }

    public static boolean hasFrostWalker(LivingEntity livingEntity0) {
        return getEnchantmentLevel(Enchantments.FROST_WALKER, livingEntity0) > 0;
    }

    public static boolean hasSoulSpeed(LivingEntity livingEntity0) {
        return getEnchantmentLevel(Enchantments.SOUL_SPEED, livingEntity0) > 0;
    }

    public static boolean hasBindingCurse(ItemStack itemStack0) {
        return getItemEnchantmentLevel(Enchantments.BINDING_CURSE, itemStack0) > 0;
    }

    public static boolean hasVanishingCurse(ItemStack itemStack0) {
        return getItemEnchantmentLevel(Enchantments.VANISHING_CURSE, itemStack0) > 0;
    }

    public static boolean hasSilkTouch(ItemStack itemStack0) {
        return getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack0) > 0;
    }

    public static int getLoyalty(ItemStack itemStack0) {
        return getItemEnchantmentLevel(Enchantments.LOYALTY, itemStack0);
    }

    public static int getRiptide(ItemStack itemStack0) {
        return getItemEnchantmentLevel(Enchantments.RIPTIDE, itemStack0);
    }

    public static boolean hasChanneling(ItemStack itemStack0) {
        return getItemEnchantmentLevel(Enchantments.CHANNELING, itemStack0) > 0;
    }

    @Nullable
    public static Entry<EquipmentSlot, ItemStack> getRandomItemWith(Enchantment enchantment0, LivingEntity livingEntity1) {
        return getRandomItemWith(enchantment0, livingEntity1, p_44941_ -> true);
    }

    @Nullable
    public static Entry<EquipmentSlot, ItemStack> getRandomItemWith(Enchantment enchantment0, LivingEntity livingEntity1, Predicate<ItemStack> predicateItemStack2) {
        Map<EquipmentSlot, ItemStack> $$3 = enchantment0.getSlotItems(livingEntity1);
        if ($$3.isEmpty()) {
            return null;
        } else {
            List<Entry<EquipmentSlot, ItemStack>> $$4 = Lists.newArrayList();
            for (Entry<EquipmentSlot, ItemStack> $$5 : $$3.entrySet()) {
                ItemStack $$6 = (ItemStack) $$5.getValue();
                if (!$$6.isEmpty() && getItemEnchantmentLevel(enchantment0, $$6) > 0 && predicateItemStack2.test($$6)) {
                    $$4.add($$5);
                }
            }
            return $$4.isEmpty() ? null : (Entry) $$4.get(livingEntity1.getRandom().nextInt($$4.size()));
        }
    }

    public static int getEnchantmentCost(RandomSource randomSource0, int int1, int int2, ItemStack itemStack3) {
        Item $$4 = itemStack3.getItem();
        int $$5 = $$4.getEnchantmentValue();
        if ($$5 <= 0) {
            return 0;
        } else {
            if (int2 > 15) {
                int2 = 15;
            }
            int $$6 = randomSource0.nextInt(8) + 1 + (int2 >> 1) + randomSource0.nextInt(int2 + 1);
            if (int1 == 0) {
                return Math.max($$6 / 3, 1);
            } else {
                return int1 == 1 ? $$6 * 2 / 3 + 1 : Math.max($$6, int2 * 2);
            }
        }
    }

    public static ItemStack enchantItem(RandomSource randomSource0, ItemStack itemStack1, int int2, boolean boolean3) {
        List<EnchantmentInstance> $$4 = selectEnchantment(randomSource0, itemStack1, int2, boolean3);
        boolean $$5 = itemStack1.is(Items.BOOK);
        if ($$5) {
            itemStack1 = new ItemStack(Items.ENCHANTED_BOOK);
        }
        for (EnchantmentInstance $$6 : $$4) {
            if ($$5) {
                EnchantedBookItem.addEnchantment(itemStack1, $$6);
            } else {
                itemStack1.enchant($$6.enchantment, $$6.level);
            }
        }
        return itemStack1;
    }

    public static List<EnchantmentInstance> selectEnchantment(RandomSource randomSource0, ItemStack itemStack1, int int2, boolean boolean3) {
        List<EnchantmentInstance> $$4 = Lists.newArrayList();
        Item $$5 = itemStack1.getItem();
        int $$6 = $$5.getEnchantmentValue();
        if ($$6 <= 0) {
            return $$4;
        } else {
            int2 += 1 + randomSource0.nextInt($$6 / 4 + 1) + randomSource0.nextInt($$6 / 4 + 1);
            float $$7 = (randomSource0.nextFloat() + randomSource0.nextFloat() - 1.0F) * 0.15F;
            int2 = Mth.clamp(Math.round((float) int2 + (float) int2 * $$7), 1, Integer.MAX_VALUE);
            List<EnchantmentInstance> $$8 = getAvailableEnchantmentResults(int2, itemStack1, boolean3);
            if (!$$8.isEmpty()) {
                WeightedRandom.getRandomItem(randomSource0, $$8).ifPresent($$4::add);
                while (randomSource0.nextInt(50) <= int2) {
                    if (!$$4.isEmpty()) {
                        filterCompatibleEnchantments($$8, Util.lastOf($$4));
                    }
                    if ($$8.isEmpty()) {
                        break;
                    }
                    WeightedRandom.getRandomItem(randomSource0, $$8).ifPresent($$4::add);
                    int2 /= 2;
                }
            }
            return $$4;
        }
    }

    public static void filterCompatibleEnchantments(List<EnchantmentInstance> listEnchantmentInstance0, EnchantmentInstance enchantmentInstance1) {
        Iterator<EnchantmentInstance> $$2 = listEnchantmentInstance0.iterator();
        while ($$2.hasNext()) {
            if (!enchantmentInstance1.enchantment.isCompatibleWith(((EnchantmentInstance) $$2.next()).enchantment)) {
                $$2.remove();
            }
        }
    }

    public static boolean isEnchantmentCompatible(Collection<Enchantment> collectionEnchantment0, Enchantment enchantment1) {
        for (Enchantment $$2 : collectionEnchantment0) {
            if (!$$2.isCompatibleWith(enchantment1)) {
                return false;
            }
        }
        return true;
    }

    public static List<EnchantmentInstance> getAvailableEnchantmentResults(int int0, ItemStack itemStack1, boolean boolean2) {
        List<EnchantmentInstance> $$3 = Lists.newArrayList();
        Item $$4 = itemStack1.getItem();
        boolean $$5 = itemStack1.is(Items.BOOK);
        for (Enchantment $$6 : BuiltInRegistries.ENCHANTMENT) {
            if ((!$$6.isTreasureOnly() || boolean2) && $$6.isDiscoverable() && ($$6.category.canEnchant($$4) || $$5)) {
                for (int $$7 = $$6.getMaxLevel(); $$7 > $$6.getMinLevel() - 1; $$7--) {
                    if (int0 >= $$6.getMinCost($$7) && int0 <= $$6.getMaxCost($$7)) {
                        $$3.add(new EnchantmentInstance($$6, $$7));
                        break;
                    }
                }
            }
        }
        return $$3;
    }

    @FunctionalInterface
    interface EnchantmentVisitor {

        void accept(Enchantment var1, int var2);
    }
}