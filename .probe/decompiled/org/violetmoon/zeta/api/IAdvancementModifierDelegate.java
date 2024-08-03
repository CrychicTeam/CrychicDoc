package org.violetmoon.zeta.api;

import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public interface IAdvancementModifierDelegate {

    IAdvancementModifier createAdventuringTimeMod(Set<ResourceKey<Biome>> var1);

    IAdvancementModifier createBalancedDietMod(Set<ItemLike> var1);

    IAdvancementModifier createFuriousCocktailMod(BooleanSupplier var1, Set<MobEffect> var2);

    IAdvancementModifier createMonsterHunterMod(Set<EntityType<?>> var1);

    IAdvancementModifier createTwoByTwoMod(Set<EntityType<?>> var1);

    IAdvancementModifier createWaxOnWaxOffMod(Set<Block> var1, Set<Block> var2);

    IAdvancementModifier createFishyBusinessMod(Set<ItemLike> var1);

    IAdvancementModifier createTacticalFishingMod(Set<BucketItem> var1);

    IAdvancementModifier createASeedyPlaceMod(Set<Block> var1);

    IAdvancementModifier createGlowAndBeholdMod(Set<Block> var1);
}