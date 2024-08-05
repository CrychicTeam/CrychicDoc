package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.filters.Resolver;
import com.almostreliable.lootjs.kube.builder.DamageSourcePredicateBuilderJS;
import com.almostreliable.lootjs.kube.builder.EntityPredicateBuilderJS;
import com.almostreliable.lootjs.loot.condition.AndCondition;
import com.almostreliable.lootjs.loot.condition.AnyBiomeCheck;
import com.almostreliable.lootjs.loot.condition.AnyDimension;
import com.almostreliable.lootjs.loot.condition.AnyStructure;
import com.almostreliable.lootjs.loot.condition.BiomeCheck;
import com.almostreliable.lootjs.loot.condition.ContainsLootCondition;
import com.almostreliable.lootjs.loot.condition.CustomParamPredicate;
import com.almostreliable.lootjs.loot.condition.IsLightLevel;
import com.almostreliable.lootjs.loot.condition.MainHandTableBonus;
import com.almostreliable.lootjs.loot.condition.MatchEquipmentSlot;
import com.almostreliable.lootjs.loot.condition.MatchKillerDistance;
import com.almostreliable.lootjs.loot.condition.MatchPlayer;
import com.almostreliable.lootjs.loot.condition.NotCondition;
import com.almostreliable.lootjs.loot.condition.OrCondition;
import com.almostreliable.lootjs.loot.condition.PlayerParamPredicate;
import com.almostreliable.lootjs.loot.condition.builder.DistancePredicateBuilder;
import com.almostreliable.lootjs.util.Utils;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.stages.Stages;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;
import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;

public interface LootConditionsContainer<B extends LootConditionsContainer<?>> {

    default B matchLoot(ItemFilter filter) {
        return this.matchLoot(filter, false);
    }

    default B matchLoot(ItemFilter filter, boolean exact) {
        return this.addCondition(new ContainsLootCondition(filter, exact));
    }

    default B matchMainHand(ItemFilter filter) {
        return this.addCondition(new MatchEquipmentSlot(EquipmentSlot.MAINHAND, filter));
    }

    default B matchOffHand(ItemFilter filter) {
        return this.addCondition(new MatchEquipmentSlot(EquipmentSlot.OFFHAND, filter));
    }

    default B matchEquip(EquipmentSlot slot, ItemFilter filter) {
        return this.addCondition(new MatchEquipmentSlot(slot, filter));
    }

    default B survivesExplosion() {
        return this.addCondition(ExplosionCondition.survivesExplosion());
    }

    default B timeCheck(long period, int min, int max) {
        return this.addCondition(new TimeCheck.Builder(IntRange.range(min, max)).setPeriod(period));
    }

    default B timeCheck(int min, int max) {
        return this.timeCheck(24000L, min, max);
    }

    default B weatherCheck(Map<String, Boolean> map) {
        Boolean isRaining = (Boolean) map.getOrDefault("raining", null);
        Boolean isThundering = (Boolean) map.getOrDefault("thundering", null);
        return this.addCondition(new WeatherCheck.Builder().setRaining(isRaining).setThundering(isThundering));
    }

    default B randomChance(float value) {
        return this.addCondition(LootItemRandomChanceCondition.randomChance(value));
    }

    default B randomChanceWithLooting(float value, float looting) {
        return this.addCondition(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(value, looting));
    }

    default B randomChanceWithEnchantment(@Nullable Enchantment enchantment, float[] chances) {
        if (enchantment == null) {
            throw new IllegalArgumentException("Enchant not found");
        } else {
            return this.addCondition(new MainHandTableBonus(enchantment, chances));
        }
    }

    default B randomTableBonus(Enchantment enchantment, float[] chances) {
        return this.addCondition(BonusLevelTableCondition.bonusLevelFlatChance(enchantment, chances));
    }

    default B biome(Resolver... resolvers) {
        List<ResourceKey<Biome>> biomes = new ArrayList();
        List<TagKey<Biome>> tagKeys = new ArrayList();
        for (Resolver resolver : resolvers) {
            if (resolver instanceof Resolver.ByEntry byEntry) {
                biomes.add(byEntry.resolve(Registries.BIOME));
            } else if (resolver instanceof Resolver.ByTagKey byTagKey) {
                tagKeys.add(byTagKey.resolve(Registries.BIOME));
            }
        }
        return this.addCondition(new BiomeCheck(biomes, tagKeys));
    }

    default B anyBiome(Resolver... resolvers) {
        List<ResourceKey<Biome>> biomes = new ArrayList();
        List<TagKey<Biome>> tagKeys = new ArrayList();
        for (Resolver resolver : resolvers) {
            if (resolver instanceof Resolver.ByEntry byEntry) {
                biomes.add(byEntry.resolve(Registries.BIOME));
            } else if (resolver instanceof Resolver.ByTagKey byTagKey) {
                tagKeys.add(byTagKey.resolve(Registries.BIOME));
            }
        }
        return this.addCondition(new AnyBiomeCheck(biomes, tagKeys));
    }

    default B anyDimension(ResourceLocation... dimensions) {
        return this.addCondition(new AnyDimension(dimensions));
    }

    default B anyStructure(String[] idOrTags, boolean exact) {
        AnyStructure.Builder builder = new AnyStructure.Builder();
        for (String s : idOrTags) {
            builder.add(s);
        }
        return this.addCondition(builder.build(exact));
    }

    default B lightLevel(int min, int max) {
        return this.addCondition(new IsLightLevel(min, max));
    }

    default B killedByPlayer() {
        return this.addCondition(LootItemKilledByPlayerCondition.killedByPlayer());
    }

    default B matchBlockState(Block block, Map<String, String> propertyMap) {
        StatePropertiesPredicate.Builder properties = Utils.createProperties(block, propertyMap);
        return this.addCondition(new LootItemBlockStatePropertyCondition.Builder(block).setProperties(properties));
    }

    default B matchFluid(Resolver resolver) {
        throw new UnsupportedOperationException("Not implemented in 1.18.2 currently.");
    }

    default B matchEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return this.addCondition(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, builder.build()));
    }

    default B matchKiller(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return this.addCondition(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, builder.build()));
    }

    default B matchDirectKiller(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return this.addCondition(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_KILLER, builder.build()));
    }

    default B matchPlayer(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return this.addCondition(new MatchPlayer(builder.build()));
    }

    default B matchDamageSource(Consumer<DamageSourcePredicateBuilderJS> action) {
        DamageSourcePredicateBuilderJS builder = new DamageSourcePredicateBuilderJS();
        action.accept(builder);
        return this.addCondition(builder);
    }

    default B distanceToKiller(MinMaxBounds.Doubles bounds) {
        return this.customDistanceToPlayer(builder -> builder.absolute(bounds));
    }

    default B customDistanceToPlayer(Consumer<DistancePredicateBuilder> action) {
        DistancePredicateBuilder builder = new DistancePredicateBuilder();
        action.accept(builder);
        return this.addCondition(new MatchKillerDistance(builder.build()));
    }

    default B playerPredicate(Predicate<ServerPlayer> predicate) {
        return this.addCondition(new PlayerParamPredicate(predicate));
    }

    default B entityPredicate(Predicate<Entity> predicate) {
        return this.addCondition(new CustomParamPredicate<>(LootContextParams.THIS_ENTITY, predicate));
    }

    default B killerPredicate(Predicate<Entity> predicate) {
        return this.addCondition(new CustomParamPredicate<>(LootContextParams.KILLER_ENTITY, predicate));
    }

    default B directKillerPredicate(Predicate<Entity> predicate) {
        return this.addCondition(new CustomParamPredicate<>(LootContextParams.DIRECT_KILLER_ENTITY, predicate));
    }

    default B blockEntityPredicate(Predicate<BlockEntity> predicate) {
        return this.addCondition(new CustomParamPredicate<>(LootContextParams.BLOCK_ENTITY, predicate));
    }

    default B hasAnyStage(String... stages) {
        if (stages.length == 1) {
            String stage = stages[0];
            return this.addCondition(new PlayerParamPredicate(player -> Stages.get(player).has(stage)));
        } else {
            return this.addCondition(new PlayerParamPredicate(player -> {
                for (String stagex : stages) {
                    if (Stages.get(player).has(stagex)) {
                        return true;
                    }
                }
                return false;
            }));
        }
    }

    default B not(Consumer<LootConditionsContainer<B>> action) {
        List<ILootCondition> conditions = this.createConditions(action);
        if (conditions.size() != 1) {
            throw new IllegalArgumentException("You only can have one condition for `not`");
        } else {
            NotCondition condition = new NotCondition((ILootCondition) conditions.get(0));
            return this.addCondition(condition);
        }
    }

    default B or(Consumer<LootConditionsContainer<B>> action) {
        List<ILootCondition> conditions = this.createConditions(action);
        ILootCondition[] array = (ILootCondition[]) conditions.toArray(new ILootCondition[0]);
        return this.addCondition(new OrCondition(array));
    }

    default B and(Consumer<LootConditionsContainer<B>> action) {
        List<ILootCondition> conditions = this.createConditions(action);
        ILootCondition[] array = (ILootCondition[]) conditions.toArray(new ILootCondition[0]);
        return this.addCondition(new AndCondition(array));
    }

    default List<ILootCondition> createConditions(Consumer<LootConditionsContainer<B>> action) {
        final List<ILootCondition> conditions = new ArrayList();
        LootConditionsContainer<B> container = new LootConditionsContainer<B>() {

            @Override
            public B addCondition(ILootCondition condition) {
                conditions.add(condition);
                return (B) this;
            }
        };
        action.accept(container);
        return conditions;
    }

    default B customCondition(JsonObject json) {
        LootItemCondition condition = (LootItemCondition) LootJS.CONDITION_GSON.fromJson(json, LootItemCondition.class);
        return this.addCondition((ILootCondition) condition);
    }

    default B addCondition(LootItemCondition.Builder builder) {
        return this.addCondition((ILootCondition) builder.build());
    }

    B addCondition(ILootCondition var1);
}