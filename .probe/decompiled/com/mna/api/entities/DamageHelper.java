package com.mna.api.entities;

import javax.annotation.Nullable;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class DamageHelper {

    public static final ResourceKey<DamageType> FROST = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("mna", "spell_frost"));

    public static final ResourceKey<DamageType> LIGHTNING = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("mna", "spell_lightning"));

    public static final ResourceKey<DamageType> BRIARS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("mna", "spell_briars"));

    public static final ResourceKey<DamageType> DISPERSE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("mna", "spell_disperse"));

    public static final ResourceKey<DamageType> WTF_BOOM = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("mna", "wtf_boom"));

    public static final ResourceKey<DamageType> CONFLAGRATE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("mna", "spell_conflagrate"));

    public static DamageSource createSourcedType(ResourceKey<DamageType> pDamageTypeKey, RegistryAccess pRegistry, Entity causingEntity) {
        return createSourcedType(pDamageTypeKey, pRegistry, causingEntity, null);
    }

    public static DamageSource createSourcedType(ResourceKey<DamageType> pDamageTypeKey, RegistryAccess pRegistry, Entity causingEntity, @Nullable Entity pDirectEntity) {
        return new DamageSource(pRegistry.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(pDamageTypeKey), pDirectEntity, causingEntity);
    }

    public static DamageSource forType(ResourceKey<DamageType> pDamageTypeKey, RegistryAccess pRegistry) {
        return new DamageSource(pRegistry.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(pDamageTypeKey));
    }
}