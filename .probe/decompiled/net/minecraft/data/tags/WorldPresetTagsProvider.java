package net.minecraft.data.tags;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

public class WorldPresetTagsProvider extends TagsProvider<WorldPreset> {

    public WorldPresetTagsProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1) {
        super(packOutput0, Registries.WORLD_PRESET, completableFutureHolderLookupProvider1);
    }

    @Override
    protected void addTags(HolderLookup.Provider holderLookupProvider0) {
        this.m_206424_(WorldPresetTags.NORMAL).add(WorldPresets.NORMAL).add(WorldPresets.FLAT).add(WorldPresets.LARGE_BIOMES).add(WorldPresets.AMPLIFIED).add(WorldPresets.SINGLE_BIOME_SURFACE);
        this.m_206424_(WorldPresetTags.EXTENDED).addTag(WorldPresetTags.NORMAL).add(WorldPresets.DEBUG);
    }
}