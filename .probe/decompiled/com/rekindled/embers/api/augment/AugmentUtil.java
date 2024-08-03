package com.rekindled.embers.api.augment;

import java.util.Collection;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AugmentUtil {

    public static IAugmentUtil IMPL;

    public static final String HEAT_TAG = "embers:heat_tag";

    public static Collection<IAugment> getAllAugments() {
        return IMPL.getAllAugments();
    }

    public static IAugment getAugment(ResourceLocation name) {
        return IMPL.getAugment(name);
    }

    public static List<IAugment> getAugments(ItemStack stack) {
        return IMPL.getAugments(stack);
    }

    public static int getTotalAugmentLevel(ItemStack stack) {
        return IMPL.getTotalAugmentLevel(stack);
    }

    public static boolean hasAugment(ItemStack stack, IAugment augment) {
        return IMPL.hasAugment(stack, augment);
    }

    public static void addAugment(ItemStack stack, ItemStack augmentStack, IAugment augment) {
        IMPL.addAugment(stack, augmentStack, augment);
    }

    public static List<ItemStack> removeAllAugments(ItemStack stack) {
        return IMPL.removeAllAugments(stack);
    }

    public static void addAugmentLevel(ItemStack stack, IAugment augment, int levels) {
        IMPL.addAugmentLevel(stack, augment, levels);
    }

    public static void setAugmentLevel(ItemStack stack, IAugment augment, int level) {
        IMPL.setAugmentLevel(stack, augment, level);
    }

    public static int getAugmentLevel(ItemStack stack, IAugment augment) {
        return IMPL.getAugmentLevel(stack, augment);
    }

    public static boolean hasHeat(ItemStack stack) {
        return IMPL.hasHeat(stack);
    }

    public static void addHeat(ItemStack stack, float heat) {
        IMPL.addHeat(stack, heat);
    }

    public static void setHeat(ItemStack stack, float heat) {
        IMPL.setHeat(stack, heat);
    }

    public static float getHeat(ItemStack stack) {
        return IMPL.getHeat(stack);
    }

    public static float getMaxHeat(ItemStack stack) {
        return IMPL.getMaxHeat(stack);
    }

    public static int getLevel(ItemStack stack) {
        return IMPL.getLevel(stack);
    }

    public static void setLevel(ItemStack stack, int level) {
        IMPL.setLevel(stack, level);
    }

    public static int getArmorAugmentLevel(LivingEntity entity, IAugment augment) {
        return IMPL.getArmorAugmentLevel(entity, augment);
    }

    public static IAugment registerAugment(IAugment augment) {
        return IMPL.registerAugment(augment);
    }
}