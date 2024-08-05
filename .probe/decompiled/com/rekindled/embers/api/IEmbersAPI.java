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

public interface IEmbersAPI {

    float getEmberDensity(long var1, int var3, int var4);

    float getEmberStability(long var1, int var3, int var4);

    void registerLinkingHammer(Item var1);

    void registerLinkingHammer(BiPredicate<Player, InteractionHand> var1);

    void registerHammerTargetGetter(Item var1);

    void registerHammerTargetGetter(Function<Player, Pair<BlockPos, Direction>> var1);

    boolean isHoldingHammer(Player var1, InteractionHand var2);

    Pair<BlockPos, Direction> getHammerTarget(Player var1);

    void registerLens(Ingredient var1);

    void registerWearableLens(Ingredient var1);

    void registerLens(Predicate<Player> var1);

    boolean isWearingLens(Player var1);

    void registerEmberResonance(Ingredient var1, double var2);

    double getEmberResonance(ItemStack var1);

    double getEmberTotal(Player var1);

    double getEmberCapacityTotal(Player var1);

    void removeEmber(Player var1, double var2);

    Item getTaggedItem(TagKey<Item> var1);

    double getScales(LivingEntity var1);

    void setScales(LivingEntity var1, double var2);
}