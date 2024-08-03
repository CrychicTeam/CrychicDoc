package com.yungnickyoung.minecraft.yungsapi.api.autoregister;

import com.google.common.collect.ImmutableSet;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterEntry;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

public class AutoRegisterEntityType<T extends Entity> extends AutoRegisterEntry<EntityType<T>> {

    private Supplier<AttributeSupplier.Builder> attributesBuilderSupplier;

    public static <U extends Entity> AutoRegisterEntityType<U> of(Supplier<EntityType<U>> entityTypeSupplier) {
        return new AutoRegisterEntityType<>(entityTypeSupplier);
    }

    public AutoRegisterEntityType<T> attributes(Supplier<AttributeSupplier.Builder> attributesBuilderSupplier) {
        this.attributesBuilderSupplier = attributesBuilderSupplier;
        return this;
    }

    public boolean hasAttributes() {
        return this.attributesBuilderSupplier != null;
    }

    public Supplier<AttributeSupplier.Builder> getAttributesSupplier() {
        return this.attributesBuilderSupplier;
    }

    private AutoRegisterEntityType(Supplier<EntityType<T>> entityTypeSupplier) {
        super(entityTypeSupplier);
    }

    public static class Builder<T extends Entity> {

        private final EntityType.EntityFactory<T> factory;

        private final MobCategory category;

        private ImmutableSet<Block> immuneTo = ImmutableSet.of();

        private boolean serialize = true;

        private boolean summon = true;

        private boolean fireImmune;

        private boolean canSpawnFarFromPlayer;

        private int clientTrackingRange = 5;

        private int updateInterval = 3;

        private EntityDimensions dimensions = EntityDimensions.scalable(0.6F, 1.8F);

        private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

        private Builder(EntityType.EntityFactory<T> entityFactory, MobCategory mobCategory) {
            this.factory = entityFactory;
            this.category = mobCategory;
            this.canSpawnFarFromPlayer = mobCategory == MobCategory.CREATURE || mobCategory == MobCategory.MISC;
        }

        public static <T extends Entity> AutoRegisterEntityType.Builder<T> of(EntityType.EntityFactory<T> entityFactory, MobCategory mobCategory) {
            return new AutoRegisterEntityType.Builder<>(entityFactory, mobCategory);
        }

        public AutoRegisterEntityType.Builder<T> sized(float width, float height) {
            this.dimensions = EntityDimensions.scalable(width, height);
            return this;
        }

        public AutoRegisterEntityType.Builder<T> noSummon() {
            this.summon = false;
            return this;
        }

        public AutoRegisterEntityType.Builder<T> noSave() {
            this.serialize = false;
            return this;
        }

        public AutoRegisterEntityType.Builder<T> fireImmune() {
            this.fireImmune = true;
            return this;
        }

        public AutoRegisterEntityType.Builder<T> immuneTo(Block... blocks) {
            this.immuneTo = ImmutableSet.copyOf(blocks);
            return this;
        }

        public AutoRegisterEntityType.Builder<T> canSpawnFarFromPlayer() {
            this.canSpawnFarFromPlayer = true;
            return this;
        }

        public AutoRegisterEntityType.Builder<T> clientTrackingRange(int chunkRange) {
            this.clientTrackingRange = chunkRange;
            return this;
        }

        public AutoRegisterEntityType.Builder<T> updateInterval(int interval) {
            this.updateInterval = interval;
            return this;
        }

        public AutoRegisterEntityType.Builder<T> requiredFeatures(FeatureFlag... $$0) {
            this.requiredFeatures = FeatureFlags.REGISTRY.subset($$0);
            return this;
        }

        public EntityType<T> build() {
            return new EntityType<>(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions, this.clientTrackingRange, this.updateInterval, this.requiredFeatures);
        }
    }
}