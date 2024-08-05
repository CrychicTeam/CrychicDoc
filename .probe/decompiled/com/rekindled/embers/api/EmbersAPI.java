package com.rekindled.embers.api;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.tuple.Pair;

public class EmbersAPI {

    public static IEmbersAPI IMPL;

    public static float getEmberDensity(long seed, int x, int z) {
        return IMPL.getEmberDensity(seed, x, z);
    }

    public static float getEmberStability(long seed, int x, int z) {
        return IMPL.getEmberStability(seed, x, z);
    }

    public static void registerLinkingHammer(Item item) {
        IMPL.registerLinkingHammer(item);
    }

    public static void registerLinkingHammer(BiPredicate<Player, InteractionHand> predicate) {
        IMPL.registerLinkingHammer(predicate);
    }

    public static void registerHammerTargetGetter(Item item) {
        IMPL.registerHammerTargetGetter(item);
    }

    public static void registerHammerTargetGetter(Function<Player, Pair<BlockPos, Direction>> predicate) {
        IMPL.registerHammerTargetGetter(predicate);
    }

    boolean isHoldingHammer(Player player, InteractionHand hand) {
        return IMPL.isHoldingHammer(player, hand);
    }

    Pair<BlockPos, Direction> getHammerTarget(Player player) {
        return IMPL.getHammerTarget(player);
    }

    public static void registerLens(Ingredient ingredient) {
        IMPL.registerLens(ingredient);
    }

    public static void registerWearableLens(Ingredient ingredient) {
        IMPL.registerWearableLens(ingredient);
    }

    public static void registerLens(Predicate<Player> predicate) {
        IMPL.registerLens(predicate);
    }

    boolean isWearingLens(Player player) {
        return IMPL.isWearingLens(player);
    }

    public static void registerEmberResonance(Ingredient ingredient, double resonance) {
        IMPL.registerEmberResonance(ingredient, resonance);
    }

    public static double getEmberResonance(ItemStack stack) {
        return IMPL.getEmberResonance(stack);
    }

    public static double getEmberTotal(Player player) {
        return IMPL.getEmberTotal(player);
    }

    public static double getEmberCapacityTotal(Player player) {
        return IMPL.getEmberCapacityTotal(player);
    }

    public static void removeEmber(Player player, double amount) {
        IMPL.removeEmber(player, amount);
    }

    Item getTaggedItem(TagKey<Item> tag) {
        return IMPL.getTaggedItem(tag);
    }

    public static double getScales(LivingEntity entity) {
        return IMPL.getScales(entity);
    }

    public static void setScales(LivingEntity entity, double scales) {
        IMPL.setScales(entity, scales);
    }
}