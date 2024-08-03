package com.almostreliable.lootjs.kube.builder;

import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.filters.Resolver;
import com.almostreliable.lootjs.predicate.CustomItemPredicate;
import com.almostreliable.lootjs.predicate.ExtendedEntityFlagsPredicate;
import com.almostreliable.lootjs.predicate.MultiEntityTypePredicate;
import com.almostreliable.lootjs.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FluidPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class EntityPredicateBuilderJS implements ExtendedEntityFlagsPredicate.IBuilder<EntityPredicate> {

    private final EntityPredicate.Builder vanillaBuilder = EntityPredicate.Builder.entity();

    private final Map<MobEffect, MobEffectsPredicate.MobEffectInstancePredicate> effects = new HashMap();

    private final ExtendedEntityFlagsPredicate.Builder flagsBuilder = new ExtendedEntityFlagsPredicate.Builder();

    @Nullable
    private EntityEquipmentPredicate.Builder equipmentPredicateBuilder;

    @Nullable
    private FluidPredicate fluidPredicate;

    @Nullable
    private BlockPredicate blockPredicate;

    public EntityPredicateBuilderJS isOnFire(boolean flag) {
        this.flagsBuilder.isOnFire(flag);
        return this;
    }

    public EntityPredicateBuilderJS isCrouching(boolean flag) {
        this.flagsBuilder.isCrouching(flag);
        return this;
    }

    public EntityPredicateBuilderJS isSprinting(boolean flag) {
        this.flagsBuilder.isSprinting(flag);
        return this;
    }

    public EntityPredicateBuilderJS isSwimming(boolean flag) {
        this.flagsBuilder.isSwimming(flag);
        return this;
    }

    public EntityPredicateBuilderJS isBaby(boolean flag) {
        this.flagsBuilder.isBaby(flag);
        return this;
    }

    public EntityPredicateBuilderJS isInWater(boolean flag) {
        this.flagsBuilder.isInWater(flag);
        return this;
    }

    public EntityPredicateBuilderJS isUnderWater(boolean flag) {
        this.flagsBuilder.isUnderWater(flag);
        return this;
    }

    public EntityPredicateBuilderJS isMonster(boolean flag) {
        this.flagsBuilder.isMonster(flag);
        return this;
    }

    public EntityPredicateBuilderJS isCreature(boolean flag) {
        this.flagsBuilder.isCreature(flag);
        return this;
    }

    public EntityPredicateBuilderJS isOnGround(boolean flag) {
        this.flagsBuilder.isOnGround(flag);
        return this;
    }

    public EntityPredicateBuilderJS isUndeadMob(boolean flag) {
        this.flagsBuilder.isUndeadMob(flag);
        return this;
    }

    public EntityPredicateBuilderJS isArthropodMob(boolean flag) {
        this.flagsBuilder.isArthropodMob(flag);
        return this;
    }

    public EntityPredicateBuilderJS isIllegarMob(boolean flag) {
        this.flagsBuilder.isIllegarMob(flag);
        return this;
    }

    public EntityPredicateBuilderJS isWaterMob(boolean flag) {
        this.flagsBuilder.isWaterMob(flag);
        return this;
    }

    public EntityPredicateBuilderJS matchBlock(Resolver resolver, Map<String, String> propertyMap) {
        BlockPredicate.Builder builder = BlockPredicate.Builder.block();
        if (resolver instanceof Resolver.ByEntry byEntry) {
            Block block = byEntry.resolve(BuiltInRegistries.BLOCK);
            StatePropertiesPredicate.Builder properties = Utils.createProperties(block, propertyMap);
            builder.setProperties(properties.build());
            builder.of(block);
        } else if (resolver instanceof Resolver.ByTagKey byTag) {
            TagKey<Block> tagKey = byTag.resolve(Registries.BLOCK);
            builder.of(tagKey);
        }
        this.blockPredicate = builder.build();
        return this;
    }

    public EntityPredicateBuilderJS matchBlock(Resolver resolver) {
        return this.matchBlock(resolver, new HashMap());
    }

    public EntityPredicateBuilderJS matchFluid(Resolver resolver) {
        if (resolver instanceof Resolver.ByEntry byEntry) {
            Fluid fluid = byEntry.resolve(BuiltInRegistries.FLUID);
            this.fluidPredicate = new FluidPredicate(null, fluid, StatePropertiesPredicate.ANY);
        } else if (resolver instanceof Resolver.ByTagKey byTag) {
            TagKey<Fluid> tagKey = byTag.resolve(Registries.FLUID);
            this.fluidPredicate = new FluidPredicate(tagKey, null, StatePropertiesPredicate.ANY);
        }
        return this;
    }

    public EntityPredicateBuilderJS hasEffect(MobEffect effect, int amplifier) {
        MinMaxBounds.Ints bounds = MinMaxBounds.Ints.atLeast(amplifier);
        MobEffectsPredicate.MobEffectInstancePredicate predicate = new MobEffectsPredicate.MobEffectInstancePredicate(bounds, MinMaxBounds.Ints.ANY, null, null);
        this.effects.put(effect, predicate);
        return this;
    }

    public EntityPredicateBuilderJS hasEffect(MobEffect effect) {
        return this.hasEffect(effect, 0);
    }

    public EntityPredicateBuilderJS nbt(CompoundTag nbt) {
        this.vanillaBuilder.nbt(new NbtPredicate(nbt));
        return this;
    }

    public EntityPredicateBuilderJS matchMount(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS mountPredicateBuilder = new EntityPredicateBuilderJS();
        action.accept(mountPredicateBuilder);
        this.vanillaBuilder.vehicle(mountPredicateBuilder.build());
        return this;
    }

    public EntityPredicateBuilderJS matchTargetedEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS vehiclePredicateBuilder = new EntityPredicateBuilderJS();
        action.accept(vehiclePredicateBuilder);
        this.vanillaBuilder.targetedEntity(vehiclePredicateBuilder.build());
        return this;
    }

    public EntityPredicateBuilderJS matchSlot(EquipmentSlot slot, ItemFilter itemFilter) {
        if (this.equipmentPredicateBuilder == null) {
            this.equipmentPredicateBuilder = new EntityEquipmentPredicate.Builder();
        }
        CustomItemPredicate predicate = new CustomItemPredicate(itemFilter);
        switch(slot) {
            case MAINHAND:
                this.equipmentPredicateBuilder.mainhand(predicate);
                break;
            case OFFHAND:
                this.equipmentPredicateBuilder.offhand(predicate);
                break;
            case FEET:
                this.equipmentPredicateBuilder.feet(predicate);
                break;
            case LEGS:
                this.equipmentPredicateBuilder.legs(predicate);
                break;
            case CHEST:
                this.equipmentPredicateBuilder.chest(predicate);
                break;
            case HEAD:
                this.equipmentPredicateBuilder.head(predicate);
        }
        return this;
    }

    public EntityPredicateBuilderJS anyType(Resolver... resolvers) {
        List<EntityType<?>> types = new ArrayList();
        List<TagKey<EntityType<?>>> tags = new ArrayList();
        for (Resolver resolver : resolvers) {
            if (resolver instanceof Resolver.ByEntry byEntry) {
                types.add(byEntry.resolve(BuiltInRegistries.ENTITY_TYPE));
            } else if (resolver instanceof Resolver.ByTagKey byTag) {
                tags.add(byTag.resolve(Registries.ENTITY_TYPE));
            }
        }
        this.vanillaBuilder.entityType(new MultiEntityTypePredicate(tags, types));
        return this;
    }

    public EntityPredicate build() {
        this.tryBuildFLags();
        this.tryBuildEffects();
        this.tryBuildEquipment();
        this.tryBuildLocation();
        return this.vanillaBuilder.build();
    }

    private void tryBuildLocation() {
        LocationPredicate.Builder locationBuilder = new LocationPredicate.Builder();
        if (this.blockPredicate != null) {
            locationBuilder.setBlock(this.blockPredicate);
        }
        if (this.fluidPredicate != null) {
            locationBuilder.setFluid(this.fluidPredicate);
        }
        this.vanillaBuilder.located(locationBuilder.build());
    }

    private void tryBuildEquipment() {
        if (this.equipmentPredicateBuilder != null) {
            this.vanillaBuilder.equipment(this.equipmentPredicateBuilder.build());
        }
    }

    private void tryBuildEffects() {
        if (!this.effects.isEmpty()) {
            this.vanillaBuilder.effects(new MobEffectsPredicate(this.effects));
        }
    }

    private void tryBuildFLags() {
        this.vanillaBuilder.flags(this.flagsBuilder.build());
    }
}