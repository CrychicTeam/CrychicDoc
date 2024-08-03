package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.compat.curios.EntitySlotAccess;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class ItemPopulator {

    static void populateArmors(LivingEntity le, int lv) {
        RandomSource r = le.getRandom();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR && le.getItemBySlot(slot).isEmpty()) {
                ItemStack stack = WeaponConfig.getRandomArmor(slot, lv, r);
                if (!stack.isEmpty()) {
                    le.setItemSlot(slot, stack);
                }
            }
        }
    }

    static void populateWeapons(LivingEntity le, MobTraitCap cap, RandomSource r) {
        ITagManager<Item> manager = ForgeRegistries.ITEMS.tags();
        if (manager != null) {
            if (le instanceof Drowned && le.getMainHandItem().isEmpty()) {
                double factor = (double) cap.getLevel() * LHConfig.COMMON.drownedTridentChancePerLevel.get();
                if (factor > le.getRandom().nextDouble()) {
                    le.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.TRIDENT));
                }
            }
            if (le.m_6095_().is(LHTagGen.MELEE_WEAPON_TARGET)) {
                if (le.getMainHandItem().isEmpty()) {
                    ItemStack stack = WeaponConfig.getRandomMeleeWeapon(cap.getLevel(), r);
                    if (!stack.isEmpty()) {
                        le.setItemSlot(EquipmentSlot.MAINHAND, stack);
                    }
                }
            } else if (le.m_6095_().is(LHTagGen.RANGED_WEAPON_TARGET)) {
                ItemStack stack = WeaponConfig.getRandomRangedWeapon(cap.getLevel(), r);
                if (!stack.isEmpty()) {
                    le.setItemSlot(EquipmentSlot.MAINHAND, stack);
                }
            }
        }
    }

    static void generateItems(MobTraitCap cap, LivingEntity le, EntityConfig.ItemPool pool) {
        if (cap.getLevel() >= pool.level()) {
            if (!(le.getRandom().nextFloat() > pool.chance())) {
                EntitySlotAccess slot = CurioCompat.decode(pool.slot(), le);
                if (slot != null) {
                    ArrayList<EntityConfig.ItemEntry> list = pool.entries();
                    int total = 0;
                    for (EntityConfig.ItemEntry e : list) {
                        total += e.weight();
                    }
                    if (total > 0) {
                        total = le.getRandom().nextInt(total);
                        for (EntityConfig.ItemEntry e : list) {
                            total -= e.weight();
                            if (total <= 0) {
                                slot.set(e.stack().copy());
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void fillEnch(int level, RandomSource source, ItemStack stack, EquipmentSlot slot) {
        WeaponConfig config = L2Hostility.WEAPON.getMerged();
        if (slot != EquipmentSlot.OFFHAND) {
            ArrayList<WeaponConfig.EnchConfig> list = slot == EquipmentSlot.MAINHAND ? config.weapon_enchantments : config.armor_enchantments;
            Map<Enchantment, Integer> map = stack.getAllEnchantments();
            for (WeaponConfig.EnchConfig e : list) {
                int elv = e.level() <= 0 ? 1 : e.level();
                if (elv <= level) {
                    for (Enchantment ench : e.enchantments()) {
                        if (!((double) e.chance() < source.nextDouble()) && stack.canApplyAtEnchantingTable(ench) && isValid(map.keySet(), ench)) {
                            int max = Math.min(level / elv, ench.getMaxLevel());
                            map.put(ench, Math.max(max, (Integer) map.getOrDefault(ench, 0)));
                        }
                    }
                }
            }
            EnchantmentHelper.setEnchantments(map, stack);
        }
    }

    private static boolean isValid(Set<Enchantment> old, Enchantment ench) {
        for (Enchantment other : old) {
            if (ench == other) {
                return true;
            }
        }
        for (Enchantment otherx : old) {
            if (!ench.isCompatibleWith(otherx)) {
                return false;
            }
        }
        return true;
    }

    public static void postFill(MobTraitCap cap, LivingEntity le) {
        RandomSource r = le.getRandom();
        populateWeapons(le, cap, r);
        for (EquipmentSlot e : EquipmentSlot.values()) {
            ItemStack stack = le.getItemBySlot(e);
            if (stack.isEnchantable()) {
                if (!stack.isEnchanted()) {
                    float lvl = Mth.clamp((float) cap.getLevel() * 0.02F, 0.0F, 1.0F) * (float) r.nextInt(30) + (float) cap.getEnchantBonus();
                    stack = EnchantmentHelper.enchantItem(r, stack, (int) lvl, false);
                }
                if (LHConfig.COMMON.allowExtraEnchantments.get()) {
                    fillEnch(cap.getLevel(), le.getRandom(), stack, e);
                }
                le.setItemSlot(e, stack);
            }
        }
        EntityConfig.Config config = cap.getConfigCache(le);
        if (config != null && !config.items.isEmpty()) {
            for (EntityConfig.ItemPool pool : config.items) {
                generateItems(cap, le, pool);
            }
        }
    }
}