package net.minecraft.client.gui.screens.worldselection;

import java.util.Map;
import java.util.Optional;
import net.minecraft.client.gui.screens.CreateBuffetWorldScreen;
import net.minecraft.client.gui.screens.CreateFlatWorldScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public interface PresetEditor {

    Map<Optional<ResourceKey<WorldPreset>>, PresetEditor> EDITORS = Map.of(Optional.of(WorldPresets.FLAT), (PresetEditor) (p_232974_, p_232975_) -> {
        ChunkGenerator $$2 = p_232975_.selectedDimensions().overworld();
        RegistryAccess $$3 = p_232975_.worldgenLoadContext();
        HolderGetter<Biome> $$4 = $$3.m_255025_(Registries.BIOME);
        HolderGetter<StructureSet> $$5 = $$3.m_255025_(Registries.STRUCTURE_SET);
        HolderGetter<PlacedFeature> $$6 = $$3.m_255025_(Registries.PLACED_FEATURE);
        return new CreateFlatWorldScreen(p_232974_, p_267859_ -> p_232974_.getUiState().updateDimensions(flatWorldConfigurator(p_267859_)), $$2 instanceof FlatLevelSource ? ((FlatLevelSource) $$2).settings() : FlatLevelGeneratorSettings.getDefault($$4, $$5, $$6));
    }, Optional.of(WorldPresets.SINGLE_BIOME_SURFACE), (PresetEditor) (p_232962_, p_232963_) -> new CreateBuffetWorldScreen(p_232962_, p_232963_, p_267861_ -> p_232962_.getUiState().updateDimensions(fixedBiomeConfigurator(p_267861_))));

    Screen createEditScreen(CreateWorldScreen var1, WorldCreationContext var2);

    private static WorldCreationContext.DimensionsUpdater flatWorldConfigurator(FlatLevelGeneratorSettings flatLevelGeneratorSettings0) {
        return (p_255454_, p_255455_) -> {
            ChunkGenerator $$3 = new FlatLevelSource(flatLevelGeneratorSettings0);
            return p_255455_.replaceOverworldGenerator(p_255454_, $$3);
        };
    }

    private static WorldCreationContext.DimensionsUpdater fixedBiomeConfigurator(Holder<Biome> holderBiome0) {
        return (p_258137_, p_258138_) -> {
            Registry<NoiseGeneratorSettings> $$3 = p_258137_.m_175515_(Registries.NOISE_SETTINGS);
            Holder<NoiseGeneratorSettings> $$4 = $$3.getHolderOrThrow(NoiseGeneratorSettings.OVERWORLD);
            BiomeSource $$5 = new FixedBiomeSource(holderBiome0);
            ChunkGenerator $$6 = new NoiseBasedChunkGenerator($$5, $$4);
            return p_258138_.replaceOverworldGenerator(p_258137_, $$6);
        };
    }
}