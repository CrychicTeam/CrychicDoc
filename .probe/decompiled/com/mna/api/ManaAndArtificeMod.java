package com.mna.api;

import com.mna.api.advancements.IAdvancementHelper;
import com.mna.api.cantrips.ICantripRegistry;
import com.mna.api.capabilities.IChunkMagic;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IWorldMagic;
import com.mna.api.entities.IEntityHelper;
import com.mna.api.entities.ISummonHelper;
import com.mna.api.entities.construct.ai.ConstructTask;
import com.mna.api.faction.IFaction;
import com.mna.api.faction.IFactionHelper;
import com.mna.api.faction.IRaidHelper;
import com.mna.api.gui.IContainerHelper;
import com.mna.api.gui.IGuiRenderHelper;
import com.mna.api.items.IItemHelper;
import com.mna.api.particles.IParticleSerializerHelper;
import com.mna.api.particles.IWorldRenderUtils;
import com.mna.api.spells.ISpellHelper;
import com.mna.api.spells.adjusters.SpellAdjustingContext;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.config.ISpellConfigHelper;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.tools.IInventoryHelper;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ManaAndArtificeMod {

    public static final String ID = "mna";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final UUID REACH_DISTANCE_UUID = UUID.fromString("cfb81e6d-8447-4e92-8683-ae23800cecca");

    private static Capability<IPlayerMagic> __magicCap;

    private static Capability<IPlayerProgression> __progressionCap;

    private static Capability<IWorldMagic> __worldMagicCap;

    private static Capability<IChunkMagic> __chunkMagicCap;

    private static ICantripRegistry __cantripRegistry;

    private static IWorldRenderUtils __worldRenderUtils;

    private static IGuiRenderHelper __guiRenderHelper;

    private static ISummonHelper __summonHelper;

    private static IPortalHelper __portalHelper;

    private static IEntityHelper __entityHelper;

    private static ISpellHelper __spellHelper;

    private static IRaidHelper __raidHelper;

    private static IFactionHelper __factionHelper;

    private static ISpellConfigHelper __configHelper;

    private static IInventoryHelper __inventoryHelper;

    private static IAdvancementHelper __advancementHelper;

    private static IParticleSerializerHelper __particleSerializationHelper;

    private static IItemHelper __itemHelper;

    private static IContainerHelper __containerHelper;

    @Deprecated(forRemoval = true)
    public static void registerSpellAdjuster(Predicate<SpellAdjustingContext> predicate, BiConsumer<ISpellDefinition, LivingEntity> adjuster) {
        getSpellHelper().registerSpellAdjuster(predicate, adjuster);
    }

    public static void registerSpellAdjuster(Predicate<SpellAdjustingContext> predicate, Consumer<SpellAdjustingContext> adjuster) {
        getSpellHelper().registerSpellAdjuster(predicate, adjuster);
    }

    public static final IAdvancementHelper getAdvancementHelper() {
        return __advancementHelper;
    }

    public static final ISpellConfigHelper getConfigHelper() {
        return __configHelper;
    }

    @Nullable
    public static final IEntityHelper getEntityHelper() {
        return __entityHelper;
    }

    public static final IFactionHelper getFactionHelper() {
        return __factionHelper;
    }

    @Nullable
    public static final IGuiRenderHelper getGuiRenderHelper() {
        return __guiRenderHelper;
    }

    public static final IInventoryHelper getInventoryHelper() {
        return __inventoryHelper;
    }

    public static final IParticleSerializerHelper getParticleSerializationHelper() {
        return __particleSerializationHelper;
    }

    @Nullable
    public static final IPortalHelper getPortalHelper() {
        return __portalHelper;
    }

    public static final IRaidHelper getRaidHelper() {
        return __raidHelper;
    }

    public static final ISpellHelper getSpellHelper() {
        return __spellHelper;
    }

    public static final ISummonHelper getSummonHelper() {
        return __summonHelper;
    }

    @Nullable
    public static final IWorldRenderUtils getWorldRenderUtils() {
        return __worldRenderUtils;
    }

    public static final IItemHelper getItemHelper() {
        return __itemHelper;
    }

    public static final IContainerHelper getContainerHelper() {
        return __containerHelper;
    }

    public static final ForgeRegistry<ConstructTask> getConstructTaskRegistry() {
        ResourceLocation registryID = new ResourceLocation("mna", "construct_task");
        ForgeRegistry<ConstructTask> frozenRegistry = RegistryManager.FROZEN.getRegistry(registryID);
        return frozenRegistry != null ? frozenRegistry : RegistryManager.ACTIVE.getRegistry(registryID);
    }

    public static final ForgeRegistry<Shape> getShapeRegistry() {
        ResourceLocation registryID = new ResourceLocation("mna", "shapes");
        ForgeRegistry<Shape> frozenRegistry = RegistryManager.FROZEN.getRegistry(registryID);
        return frozenRegistry != null ? frozenRegistry : RegistryManager.ACTIVE.getRegistry(registryID);
    }

    public static final ForgeRegistry<SpellEffect> getComponentRegistry() {
        ResourceLocation registryID = new ResourceLocation("mna", "components");
        ForgeRegistry<SpellEffect> frozenRegistry = RegistryManager.FROZEN.getRegistry(registryID);
        return frozenRegistry != null ? frozenRegistry : RegistryManager.ACTIVE.getRegistry(registryID);
    }

    public static final ForgeRegistry<Modifier> getModifierRegistry() {
        ResourceLocation registryID = new ResourceLocation("mna", "modifiers");
        ForgeRegistry<Modifier> frozenRegistry = RegistryManager.FROZEN.getRegistry(registryID);
        return frozenRegistry != null ? frozenRegistry : RegistryManager.ACTIVE.getRegistry(registryID);
    }

    public static final ForgeRegistry<IFaction> getFactionsRegistry() {
        ResourceLocation registryID = new ResourceLocation("mna", "factions");
        ForgeRegistry<IFaction> frozenRegistry = RegistryManager.FROZEN.getRegistry(registryID);
        return frozenRegistry != null ? frozenRegistry : RegistryManager.ACTIVE.getRegistry(registryID);
    }

    public static final ICantripRegistry getCantripRegistry() {
        return __cantripRegistry;
    }

    public static final Capability<IWorldMagic> getWorldMagicCapability() {
        return __worldMagicCap;
    }

    public static final Capability<IPlayerMagic> getMagicCapability() {
        return __magicCap;
    }

    public static final Capability<IPlayerProgression> getProgressionCapability() {
        return __progressionCap;
    }

    public static final Capability<IChunkMagic> getChunkMagicCapability() {
        return __chunkMagicCap;
    }

    public static void setMagicCapability(Capability<IPlayerMagic> cap) {
        if (__magicCap == null) {
            __magicCap = cap;
        }
    }

    public static void setProgressionCapability(Capability<IPlayerProgression> cap) {
        if (__progressionCap == null) {
            __progressionCap = cap;
        }
    }

    public static void setWorldMagicCapability(Capability<IWorldMagic> cap) {
        if (__worldMagicCap == null) {
            __worldMagicCap = cap;
        }
    }

    public static void setChunkMagicCapability(Capability<IChunkMagic> cap) {
        if (__chunkMagicCap == null) {
            __chunkMagicCap = cap;
        }
    }

    public static void setCantripRegistry(ICantripRegistry registry) {
        if (__cantripRegistry == null) {
            __cantripRegistry = registry;
        }
    }

    public static void setAPIHelper(IPortalHelper helper) {
        if (__portalHelper == null) {
            __portalHelper = helper;
        }
    }

    public static void setAPIHelper(IEntityHelper helper) {
        if (__entityHelper == null) {
            __entityHelper = helper;
        }
    }

    public static void setAPIHelper(ISpellHelper helper) {
        if (__spellHelper == null) {
            __spellHelper = helper;
        }
    }

    public static void setAPIHelper(IRaidHelper helper) {
        if (__raidHelper == null) {
            __raidHelper = helper;
        }
    }

    public static void setAPIHelper(IWorldRenderUtils registry) {
        if (__worldRenderUtils == null) {
            __worldRenderUtils = registry;
        }
    }

    public static void setAPIHelper(IGuiRenderHelper registry) {
        if (__guiRenderHelper == null) {
            __guiRenderHelper = registry;
        }
    }

    public static void setAPIHelper(ISummonHelper helper) {
        if (__summonHelper == null) {
            __summonHelper = helper;
        }
    }

    public static void setAPIHelper(IFactionHelper helper) {
        if (__factionHelper == null) {
            __factionHelper = helper;
        }
    }

    public static void setAPIHelper(ISpellConfigHelper helper) {
        if (__configHelper == null) {
            __configHelper = helper;
        }
    }

    public static void setAPIHelper(IInventoryHelper helper) {
        if (__inventoryHelper == null) {
            __inventoryHelper = helper;
        }
    }

    public static void setAPIHelper(IAdvancementHelper helper) {
        if (__advancementHelper == null) {
            __advancementHelper = helper;
        }
    }

    public static void setAPIHelper(IParticleSerializerHelper helper) {
        if (__particleSerializationHelper == null) {
            __particleSerializationHelper = helper;
        }
    }

    public static void setAPIHelper(IItemHelper helper) {
        if (__itemHelper == null) {
            __itemHelper = helper;
        }
    }

    public static void setAPIHelper(IContainerHelper helper) {
        if (__containerHelper == null) {
            __containerHelper = helper;
        }
    }
}