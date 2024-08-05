package com.mna.config;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.config.ISpellConfigHelper;
import java.util.HashMap;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class SpellConfig {

    public static final ForgeConfigSpec.Builder SPELLS_CONFIG_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec SPELLS_CONFIG_SPEC;

    private static boolean finalized = false;

    private static HashMap<SpellConfigProvider.CompoundConfigKey, Double> _resolvedConfigs = new HashMap();

    private static HashMap<ResourceLocation, List<? extends String>> _resolvedDimensionBlacklists = new HashMap();

    private static HashMap<ResourceLocation, List<? extends String>> _resolvedBiomeBlacklists = new HashMap();

    private static List<? extends String> GlobalSpellDimensionBlacklist;

    private static List<? extends String> GlobalSpellBiomeBlacklist;

    public static void finalizeServerConfig() {
        SPELLS_CONFIG_SPEC = SPELLS_CONFIG_BUILDER.build();
        ModLoadingContext.get().registerConfig(Type.SERVER, SPELLS_CONFIG_SPEC, "mna-spells.toml");
        finalized = true;
    }

    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == SPELLS_CONFIG_SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {
        ManaAndArtifice.LOGGER.info("M&A >> Baking Spell Config");
        GlobalSpellDimensionBlacklist = SpellConfigProvider.SPELL_DIMENSION_BLACKLIST.get();
        GlobalSpellBiomeBlacklist = SpellConfigProvider.SPELL_BIOME_BLACKLIST.get();
        ((IForgeRegistry) Registries.Shape.get()).forEach(s -> s.getModifiableAttributes().forEach(attr -> {
            for (ISpellConfigHelper.Value value : ISpellConfigHelper.Value.values()) {
                SpellConfigProvider.CompoundConfigKey key = new SpellConfigProvider.CompoundConfigKey(s, attr.getAttribute(), value);
                _resolvedConfigs.put(key, SpellConfigProvider.GetForKey(key, attr.getDefaultValue()));
            }
        }));
        ((IForgeRegistry) Registries.SpellEffect.get()).forEach(s -> s.getModifiableAttributes().forEach(attr -> {
            for (ISpellConfigHelper.Value value : ISpellConfigHelper.Value.values()) {
                SpellConfigProvider.CompoundConfigKey key = new SpellConfigProvider.CompoundConfigKey(s, attr.getAttribute(), value);
                _resolvedConfigs.put(key, SpellConfigProvider.GetForKey(key, attr.getDefaultValue()));
            }
        }));
        SpellConfigProvider._dimensionBlacklists.forEach((dim, keys) -> _resolvedDimensionBlacklists.put(dim, (List) keys.get()));
        SpellConfigProvider._biomeBlacklists.forEach((dim, keys) -> _resolvedBiomeBlacklists.put(dim, (List) keys.get()));
    }

    public static boolean finalized() {
        return finalized;
    }

    public static float getConfiguredValue(ISpellComponent part, Attribute attribute, ISpellConfigHelper.Value value, float defaultValue) {
        if (!finalized()) {
            return defaultValue;
        } else {
            SpellConfigProvider.CompoundConfigKey key = new SpellConfigProvider.CompoundConfigKey(part, attribute, value);
            _resolvedConfigs.computeIfAbsent(key, k -> SpellConfigProvider.GetForKey(key, defaultValue));
            return ((Double) _resolvedConfigs.get(key)).floatValue();
        }
    }

    public static boolean isDimensionBlacklisted(ISpellComponent part, ResourceLocation dimensionID) {
        String flattenedDimensionID = dimensionID.toString();
        if (GlobalSpellDimensionBlacklist.contains(flattenedDimensionID)) {
            return true;
        } else {
            return _resolvedDimensionBlacklists.containsKey(part.getRegistryName()) ? ((List) _resolvedDimensionBlacklists.get(part.getRegistryName())).contains(dimensionID.toString()) : false;
        }
    }

    public static boolean isBiomeBlacklisted(ISpellComponent part, ResourceLocation biomeID) {
        if (GlobalSpellBiomeBlacklist.contains(biomeID.toString())) {
            return false;
        } else {
            return _resolvedBiomeBlacklists.containsKey(part.getRegistryName()) ? ((List) _resolvedBiomeBlacklists.get(part.getRegistryName())).contains(biomeID.toString()) : false;
        }
    }

    public static boolean isPartInitialized(ISpellComponent part) {
        return SpellConfigProvider.IsPartInitialized(part);
    }
}