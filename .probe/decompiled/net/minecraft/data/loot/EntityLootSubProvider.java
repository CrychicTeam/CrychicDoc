package net.minecraft.data.loot;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public abstract class EntityLootSubProvider implements LootTableSubProvider {

    protected static final EntityPredicate.Builder ENTITY_ON_FIRE = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build());

    private static final Set<EntityType<?>> SPECIAL_LOOT_TABLE_TYPES = ImmutableSet.of(EntityType.PLAYER, EntityType.ARMOR_STAND, EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.VILLAGER);

    private final FeatureFlagSet allowed;

    private final FeatureFlagSet required;

    private final Map<EntityType<?>, Map<ResourceLocation, LootTable.Builder>> map = Maps.newHashMap();

    protected EntityLootSubProvider(FeatureFlagSet featureFlagSet0) {
        this(featureFlagSet0, featureFlagSet0);
    }

    protected EntityLootSubProvider(FeatureFlagSet featureFlagSet0, FeatureFlagSet featureFlagSet1) {
        this.allowed = featureFlagSet0;
        this.required = featureFlagSet1;
    }

    protected static LootTable.Builder createSheepTable(ItemLike itemLike0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(itemLike0))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(EntityType.SHEEP.getDefaultLootTable())));
    }

    public abstract void generate();

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumerResourceLocationLootTableBuilder0) {
        this.generate();
        Set<ResourceLocation> $$1 = Sets.newHashSet();
        BuiltInRegistries.ENTITY_TYPE.m_203611_().forEach(p_266624_ -> {
            EntityType<?> $$3 = (EntityType<?>) p_266624_.value();
            if ($$3.m_245993_(this.allowed)) {
                if (canHaveLootTable($$3)) {
                    Map<ResourceLocation, LootTable.Builder> $$4 = (Map<ResourceLocation, LootTable.Builder>) this.map.remove($$3);
                    ResourceLocation $$5 = $$3.getDefaultLootTable();
                    if (!$$5.equals(BuiltInLootTables.EMPTY) && $$3.m_245993_(this.required) && ($$4 == null || !$$4.containsKey($$5))) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", $$5, p_266624_.key().location()));
                    }
                    if ($$4 != null) {
                        $$4.forEach((p_250376_, p_250972_) -> {
                            if (!$$1.add(p_250376_)) {
                                throw new IllegalStateException(String.format(Locale.ROOT, "Duplicate loottable '%s' for '%s'", p_250376_, p_266624_.key().location()));
                            } else {
                                biConsumerResourceLocationLootTableBuilder0.accept(p_250376_, p_250972_);
                            }
                        });
                    }
                } else {
                    Map<ResourceLocation, LootTable.Builder> $$6 = (Map<ResourceLocation, LootTable.Builder>) this.map.remove($$3);
                    if ($$6 != null) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Weird loottables '%s' for '%s', not a LivingEntity so should not have loot", $$6.keySet().stream().map(ResourceLocation::toString).collect(Collectors.joining(",")), p_266624_.key().location()));
                    }
                }
            }
        });
        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created loot tables for entities not supported by datapack: " + this.map.keySet());
        }
    }

    private static boolean canHaveLootTable(EntityType<?> entityType0) {
        return SPECIAL_LOOT_TABLE_TYPES.contains(entityType0) || entityType0.getCategory() != MobCategory.MISC;
    }

    protected LootItemCondition.Builder killedByFrog() {
        return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().source(EntityPredicate.Builder.entity().of(EntityType.FROG)));
    }

    protected LootItemCondition.Builder killedByFrogVariant(FrogVariant frogVariant0) {
        return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().source(EntityPredicate.Builder.entity().of(EntityType.FROG).subPredicate(EntitySubPredicate.variant(frogVariant0))));
    }

    protected void add(EntityType<?> entityType0, LootTable.Builder lootTableBuilder1) {
        this.add(entityType0, entityType0.getDefaultLootTable(), lootTableBuilder1);
    }

    protected void add(EntityType<?> entityType0, ResourceLocation resourceLocation1, LootTable.Builder lootTableBuilder2) {
        ((Map) this.map.computeIfAbsent(entityType0, p_251466_ -> new HashMap())).put(resourceLocation1, lootTableBuilder2);
    }
}