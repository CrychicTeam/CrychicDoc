package com.github.alexthe666.iceandfire.misc;

import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = "iceandfire", bus = Bus.MOD)
public class IafDamageRegistry {

    public static final ResourceKey<DamageType> GORGON_DMG_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("iceandfire:gorgon"));

    public static final ResourceKey<DamageType> DRAGON_FIRE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("iceandfire:dragon_fire"));

    public static final ResourceKey<DamageType> DRAGON_ICE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("iceandfire:dragon_ice"));

    public static final ResourceKey<DamageType> DRAGON_LIGHTNING_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("iceandfire:dragon_lightning"));

    public static IafDamageRegistry.CustomEntityDamageSource causeGorgonDamage(@Nullable Entity entity) {
        Holder<DamageType> holder = (Holder<DamageType>) entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(GORGON_DMG_TYPE).get();
        return new IafDamageRegistry.CustomEntityDamageSource(holder, entity);
    }

    public static IafDamageRegistry.CustomEntityDamageSource causeDragonFireDamage(@Nullable Entity entity) {
        Holder<DamageType> holder = (Holder<DamageType>) entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(DRAGON_FIRE_TYPE).get();
        return new IafDamageRegistry.CustomEntityDamageSource(holder, entity);
    }

    public static IafDamageRegistry.CustomIndirectEntityDamageSource causeIndirectDragonFireDamage(Entity source, @Nullable Entity indirectEntityIn) {
        Holder<DamageType> holder = (Holder<DamageType>) indirectEntityIn.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(DRAGON_FIRE_TYPE).get();
        return new IafDamageRegistry.CustomIndirectEntityDamageSource(holder, source, indirectEntityIn);
    }

    public static IafDamageRegistry.CustomEntityDamageSource causeDragonIceDamage(@Nullable Entity entity) {
        Holder<DamageType> holder = (Holder<DamageType>) entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(DRAGON_ICE_TYPE).get();
        return new IafDamageRegistry.CustomEntityDamageSource(holder, entity);
    }

    public static IafDamageRegistry.CustomIndirectEntityDamageSource causeIndirectDragonIceDamage(Entity source, @Nullable Entity indirectEntityIn) {
        Holder<DamageType> holder = (Holder<DamageType>) indirectEntityIn.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(DRAGON_ICE_TYPE).get();
        return new IafDamageRegistry.CustomIndirectEntityDamageSource(holder, source, indirectEntityIn);
    }

    public static IafDamageRegistry.CustomEntityDamageSource causeDragonLightningDamage(@Nullable Entity entity) {
        Holder<DamageType> holder = (Holder<DamageType>) entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(DRAGON_LIGHTNING_TYPE).get();
        return new IafDamageRegistry.CustomEntityDamageSource(holder, entity);
    }

    public static IafDamageRegistry.CustomIndirectEntityDamageSource causeIndirectDragonLightningDamage(Entity source, @Nullable Entity indirectEntityIn) {
        Holder<DamageType> holder = (Holder<DamageType>) indirectEntityIn.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(DRAGON_LIGHTNING_TYPE).get();
        return new IafDamageRegistry.CustomIndirectEntityDamageSource(holder, source, indirectEntityIn);
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeServer(), output -> new IafDamageRegistry.IafDamageTypeTagsProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), "iceandfire", event.getExistingFileHelper()));
    }

    static class CustomEntityDamageSource extends DamageSource {

        public CustomEntityDamageSource(Holder<DamageType> damageTypeIn, @Nullable Entity damageSourceEntityIn) {
            super(damageTypeIn, damageSourceEntityIn);
        }

        @NotNull
        @Override
        public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            LivingEntity livingentity = entityLivingBaseIn.getKillCredit();
            String s = "death.attack." + this.m_19385_();
            int index = entityLivingBaseIn.getRandom().nextInt(2);
            String s1 = s + "." + index;
            String s2 = s + ".attacker_" + index;
            return livingentity != null ? Component.translatable(s2, entityLivingBaseIn.m_5446_(), livingentity.m_5446_()) : Component.translatable(s1, entityLivingBaseIn.m_5446_());
        }
    }

    static class CustomIndirectEntityDamageSource extends DamageSource {

        public CustomIndirectEntityDamageSource(Holder<DamageType> damageTypeIn, Entity source, @Nullable Entity indirectEntityIn) {
            super(damageTypeIn, source, indirectEntityIn);
        }

        @NotNull
        @Override
        public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            LivingEntity livingentity = entityLivingBaseIn.getKillCredit();
            String s = "death.attack." + this.m_19385_();
            int index = entityLivingBaseIn.getRandom().nextInt(2);
            String s1 = s + "." + index;
            String s2 = s + ".attacker_" + index;
            return livingentity != null ? Component.translatable(s2, entityLivingBaseIn.m_5446_(), livingentity.m_5446_()) : Component.translatable(s1, entityLivingBaseIn.m_5446_());
        }
    }

    public static class IafDamageTypeTagsProvider extends DamageTypeTagsProvider {

        public IafDamageTypeTagsProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1, String modId, @org.jetbrains.annotations.Nullable ExistingFileHelper existingFileHelper) {
            super(packOutput0, completableFutureHolderLookupProvider1, modId, existingFileHelper);
        }

        @Override
        public void addTags(HolderLookup.Provider pProvider) {
            this.m_206424_(DamageTypeTags.BYPASSES_ARMOR).add(IafDamageRegistry.GORGON_DMG_TYPE);
            this.m_206424_(DamageTypeTags.BYPASSES_EFFECTS).add(IafDamageRegistry.GORGON_DMG_TYPE);
        }
    }
}