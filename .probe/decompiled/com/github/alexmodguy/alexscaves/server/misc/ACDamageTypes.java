package com.github.alexmodguy.alexscaves.server.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ACDamageTypes {

    public static final ResourceKey<DamageType> ACID = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("alexscaves", "acid"));

    public static final ResourceKey<DamageType> NUKE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("alexscaves", "nuke"));

    public static final ResourceKey<DamageType> RADIATION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("alexscaves", "radiation"));

    public static final ResourceKey<DamageType> RAYGUN = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("alexscaves", "raygun"));

    public static final ResourceKey<DamageType> FORSAKEN_SONIC_BOOM = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("alexscaves", "forsaken_sonic_boom"));

    public static final ResourceKey<DamageType> DESOLATE_DAGGER = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("alexscaves", "desolate_dagger"));

    public static final ResourceKey<DamageType> DARK_ARROW = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("alexscaves", "dark_arrow"));

    public static final ResourceKey<DamageType> SPIRIT_DINOSAUR = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("alexscaves", "spirit_dinosaur"));

    public static final ResourceKey<DamageType> TREMORZILLA_BEAM = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("alexscaves", "tremorzilla_beam"));

    public static DamageSource causeAcidDamage(RegistryAccess registryAccess) {
        return new ACDamageTypes.DamageSourceRandomMessages(((Registry) registryAccess.registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(ACID), 1);
    }

    public static DamageSource causeRadiationDamage(RegistryAccess registryAccess) {
        return new ACDamageTypes.DamageSourceRandomMessages(((Registry) registryAccess.registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(RADIATION), 2);
    }

    public static DamageSource causeNukeDamage(RegistryAccess registryAccess) {
        return new ACDamageTypes.DamageSourceRandomMessages(((Registry) registryAccess.registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(NUKE), 4);
    }

    public static DamageSource causeRaygunDamage(RegistryAccess registryAccess, Entity source) {
        return new ACDamageTypes.DamageSourceRandomMessages(((Registry) registryAccess.registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(RAYGUN), source, 1);
    }

    public static DamageSource causeForsakenSonicBoomDamage(RegistryAccess registryAccess, Entity source) {
        return new ACDamageTypes.DamageSourceRandomMessages(((Registry) registryAccess.registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(FORSAKEN_SONIC_BOOM), source, 2);
    }

    public static DamageSource causeDesolateDaggerDamage(RegistryAccess registryAccess, Entity source) {
        return new ACDamageTypes.DamageSourceRandomMessages(((Registry) registryAccess.registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(DESOLATE_DAGGER), source, 1);
    }

    public static DamageSource causeDarkArrowDamage(RegistryAccess registryAccess, Entity source) {
        return new ACDamageTypes.DamageSourceRandomMessages(((Registry) registryAccess.registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(DARK_ARROW), source, 1);
    }

    public static DamageSource causeSpiritDinosaurDamage(RegistryAccess registryAccess, Entity source) {
        return new ACDamageTypes.DamageSourceRandomMessages(((Registry) registryAccess.registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(SPIRIT_DINOSAUR), source, 1);
    }

    public static DamageSource causeTremorzillaBeamDamage(RegistryAccess registryAccess, Entity source) {
        return new ACDamageTypes.DamageSourceRandomMessages(((Registry) registryAccess.registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(TREMORZILLA_BEAM), source, 1);
    }

    private static class DamageSourceRandomMessages extends DamageSource {

        private int messageCount;

        public DamageSourceRandomMessages(Holder.Reference<DamageType> message, int messageCount) {
            super(message);
            this.messageCount = messageCount;
        }

        public DamageSourceRandomMessages(Holder.Reference<DamageType> message, Entity source, int messageCount) {
            super(message, source);
            this.messageCount = messageCount;
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity attacked) {
            int type = attacked.getRandom().nextInt(this.messageCount);
            String s = "death.attack." + this.m_19385_() + "_" + type;
            Entity entity = this.m_7640_() == null ? this.m_7639_() : this.m_7640_();
            return entity != null ? Component.translatable(s + ".entity", attacked.m_5446_(), entity.getDisplayName()) : Component.translatable(s, attacked.m_5446_());
        }
    }
}