package com.mna.effects.harmful;

import com.mna.api.config.GeneralConfigValues;
import com.mna.effects.interfaces.INoCreeperLingering;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectDisjunction extends SimpleHarmfulEffect implements INoCreeperLingering {

    public static final String NBT_DISJUNCTION = "disjunction";

    public static final String NBT_ENCHANTMENTS = "Enchantments";

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ApplyDisjunction(pLivingEntity.getItemBySlot(slot), pAmplifier + 1);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            RemoveDisjunction(pLivingEntity.getItemBySlot(slot));
        }
    }

    public static void ApplyDisjunction(ItemStack stack, int maxLevels) {
        if (stack.hasTag() && maxLevels >= 1 && !stack.getTag().contains("disjunction")) {
            ListTag listtag = stack.getEnchantmentTags();
            if (listtag.size() != 0) {
                stack.getTag().put("disjunction", listtag);
                Map<Enchantment, Integer> chants = EnchantmentHelper.getEnchantments(stack);
                List<Enchantment> keys = Arrays.asList((Enchantment[]) chants.keySet().toArray(new Enchantment[0]));
                Collections.shuffle(keys);
                int total = chants.values().stream().mapToInt(Integer::intValue).sum();
                int suppression = (int) Math.min(Math.ceil((double) ((float) total / (float) chants.size())), (double) maxLevels);
                int totalSuppressed = 0;
                for (Enchantment e : keys) {
                    if (!GeneralConfigValues.DisjunctionBlacklist.contains(ForgeRegistries.ENCHANTMENTS.getKey(e).toString())) {
                        Integer existing = (Integer) chants.get(e);
                        int loopSuppression = Math.min(suppression, maxLevels - totalSuppressed);
                        if (existing > loopSuppression) {
                            totalSuppressed += loopSuppression;
                            chants.put(e, existing - loopSuppression);
                        } else {
                            totalSuppressed += existing;
                            chants.remove(e);
                        }
                        if (totalSuppressed >= maxLevels) {
                            break;
                        }
                    }
                }
                EnchantmentHelper.setEnchantments(chants, stack);
            }
        }
    }

    public static void RemoveDisjunction(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("disjunction")) {
            ListTag backup = stack.getTag().getList("disjunction", 10);
            stack.getTag().put("Enchantments", backup);
            stack.getTag().remove("disjunction");
        }
    }
}