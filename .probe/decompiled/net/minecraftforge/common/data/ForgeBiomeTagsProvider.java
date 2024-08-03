package net.minecraftforge.common.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;

public final class ForgeBiomeTagsProvider extends BiomeTagsProvider {

    public ForgeBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "forge", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        this.tag(Biomes.PLAINS, Tags.Biomes.IS_PLAINS);
        this.tag(Biomes.DESERT, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_DRY_OVERWORLD, Tags.Biomes.IS_SANDY, Tags.Biomes.IS_DESERT);
        this.tag(Biomes.TAIGA, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_CONIFEROUS);
        this.tag(Biomes.SWAMP, Tags.Biomes.IS_WET_OVERWORLD, Tags.Biomes.IS_SWAMP);
        this.tag(Biomes.NETHER_WASTES, Tags.Biomes.IS_HOT_NETHER, Tags.Biomes.IS_DRY_NETHER);
        this.tag(Biomes.THE_END, Tags.Biomes.IS_COLD_END, Tags.Biomes.IS_DRY_END);
        this.tag(Biomes.FROZEN_OCEAN, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_SNOWY);
        this.tag(Biomes.FROZEN_RIVER, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_SNOWY);
        this.tag(Biomes.SNOWY_PLAINS, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_SNOWY, Tags.Biomes.IS_WASTELAND, Tags.Biomes.IS_PLAINS);
        this.tag(Biomes.MUSHROOM_FIELDS, Tags.Biomes.IS_MUSHROOM, Tags.Biomes.IS_RARE);
        this.tag(Biomes.JUNGLE, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_WET_OVERWORLD, Tags.Biomes.IS_DENSE_OVERWORLD);
        this.tag(Biomes.SPARSE_JUNGLE, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_WET_OVERWORLD, Tags.Biomes.IS_RARE);
        this.tag(Biomes.BEACH, Tags.Biomes.IS_WET_OVERWORLD, Tags.Biomes.IS_SANDY);
        this.tag(Biomes.SNOWY_BEACH, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_SNOWY);
        this.tag(Biomes.DARK_FOREST, Tags.Biomes.IS_SPOOKY, Tags.Biomes.IS_DENSE_OVERWORLD);
        this.tag(Biomes.SNOWY_TAIGA, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_CONIFEROUS, Tags.Biomes.IS_SNOWY);
        this.tag(Biomes.OLD_GROWTH_PINE_TAIGA, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_CONIFEROUS);
        this.tag(Biomes.WINDSWEPT_FOREST, Tags.Biomes.IS_SPARSE_OVERWORLD);
        this.tag(Biomes.SAVANNA, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_SPARSE_OVERWORLD);
        this.tag(Biomes.SAVANNA_PLATEAU, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_SPARSE_OVERWORLD, Tags.Biomes.IS_RARE, Tags.Biomes.IS_SLOPE, Tags.Biomes.IS_PLATEAU);
        this.tag(Biomes.BADLANDS, Tags.Biomes.IS_SANDY, Tags.Biomes.IS_DRY_OVERWORLD);
        this.tag(Biomes.WOODED_BADLANDS, Tags.Biomes.IS_SANDY, Tags.Biomes.IS_DRY_OVERWORLD, Tags.Biomes.IS_SPARSE_OVERWORLD, Tags.Biomes.IS_SLOPE, Tags.Biomes.IS_PLATEAU);
        this.tag(Biomes.MEADOW, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_PLATEAU, Tags.Biomes.IS_SLOPE);
        this.tag(Biomes.GROVE, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_CONIFEROUS, Tags.Biomes.IS_SNOWY, Tags.Biomes.IS_SLOPE);
        this.tag(Biomes.SNOWY_SLOPES, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_SPARSE_OVERWORLD, Tags.Biomes.IS_SNOWY, Tags.Biomes.IS_SLOPE);
        this.tag(Biomes.JAGGED_PEAKS, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_SPARSE_OVERWORLD, Tags.Biomes.IS_SNOWY, Tags.Biomes.IS_PEAK);
        this.tag(Biomes.FROZEN_PEAKS, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_SPARSE_OVERWORLD, Tags.Biomes.IS_SNOWY, Tags.Biomes.IS_PEAK);
        this.tag(Biomes.STONY_PEAKS, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_PEAK);
        this.tag(Biomes.SMALL_END_ISLANDS, Tags.Biomes.IS_COLD_END, Tags.Biomes.IS_DRY_END);
        this.tag(Biomes.END_MIDLANDS, Tags.Biomes.IS_COLD_END, Tags.Biomes.IS_DRY_END);
        this.tag(Biomes.END_HIGHLANDS, Tags.Biomes.IS_COLD_END, Tags.Biomes.IS_DRY_END);
        this.tag(Biomes.END_BARRENS, Tags.Biomes.IS_COLD_END, Tags.Biomes.IS_DRY_END);
        this.tag(Biomes.WARM_OCEAN, Tags.Biomes.IS_HOT_OVERWORLD);
        this.tag(Biomes.COLD_OCEAN, Tags.Biomes.IS_COLD_OVERWORLD);
        this.tag(Biomes.DEEP_COLD_OCEAN, Tags.Biomes.IS_COLD_OVERWORLD);
        this.tag(Biomes.DEEP_FROZEN_OCEAN, Tags.Biomes.IS_COLD_OVERWORLD);
        this.tag(Biomes.THE_VOID, Tags.Biomes.IS_VOID);
        this.tag(Biomes.SUNFLOWER_PLAINS, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_RARE);
        this.tag(Biomes.WINDSWEPT_GRAVELLY_HILLS, Tags.Biomes.IS_SPARSE_OVERWORLD, Tags.Biomes.IS_RARE);
        this.tag(Biomes.FLOWER_FOREST, Tags.Biomes.IS_RARE);
        this.tag(Biomes.ICE_SPIKES, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_SNOWY, Tags.Biomes.IS_RARE);
        this.tag(Biomes.OLD_GROWTH_BIRCH_FOREST, Tags.Biomes.IS_DENSE_OVERWORLD, Tags.Biomes.IS_RARE);
        this.tag(Biomes.OLD_GROWTH_SPRUCE_TAIGA, Tags.Biomes.IS_DENSE_OVERWORLD, Tags.Biomes.IS_RARE);
        this.tag(Biomes.WINDSWEPT_SAVANNA, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_DRY_OVERWORLD, Tags.Biomes.IS_SPARSE_OVERWORLD, Tags.Biomes.IS_RARE);
        this.tag(Biomes.ERODED_BADLANDS, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_DRY_OVERWORLD, Tags.Biomes.IS_SPARSE_OVERWORLD, Tags.Biomes.IS_RARE);
        this.tag(Biomes.BAMBOO_JUNGLE, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_WET_OVERWORLD, Tags.Biomes.IS_RARE);
        this.tag(Biomes.LUSH_CAVES, Tags.Biomes.IS_CAVE, Tags.Biomes.IS_LUSH, Tags.Biomes.IS_WET_OVERWORLD);
        this.tag(Biomes.DRIPSTONE_CAVES, Tags.Biomes.IS_CAVE, Tags.Biomes.IS_SPARSE_OVERWORLD);
        this.tag(Biomes.SOUL_SAND_VALLEY, Tags.Biomes.IS_HOT_NETHER, Tags.Biomes.IS_DRY_NETHER);
        this.tag(Biomes.CRIMSON_FOREST, Tags.Biomes.IS_HOT_NETHER, Tags.Biomes.IS_DRY_NETHER);
        this.tag(Biomes.WARPED_FOREST, Tags.Biomes.IS_HOT_NETHER, Tags.Biomes.IS_DRY_NETHER);
        this.tag(Biomes.BASALT_DELTAS, Tags.Biomes.IS_HOT_NETHER, Tags.Biomes.IS_DRY_NETHER);
        this.tag(Biomes.MANGROVE_SWAMP, Tags.Biomes.IS_WET_OVERWORLD, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_SWAMP);
        this.tag(Biomes.DEEP_DARK, Tags.Biomes.IS_CAVE, Tags.Biomes.IS_RARE, Tags.Biomes.IS_SPOOKY);
        this.m_206424_(Tags.Biomes.IS_HOT).addTag(Tags.Biomes.IS_HOT_OVERWORLD).addTag(Tags.Biomes.IS_HOT_NETHER).addOptionalTag(Tags.Biomes.IS_HOT_END.location());
        this.m_206424_(Tags.Biomes.IS_COLD).addTag(Tags.Biomes.IS_COLD_OVERWORLD).addOptionalTag(Tags.Biomes.IS_COLD_NETHER.location()).addTag(Tags.Biomes.IS_COLD_END);
        this.m_206424_(Tags.Biomes.IS_SPARSE).addTag(Tags.Biomes.IS_SPARSE_OVERWORLD).addOptionalTag(Tags.Biomes.IS_SPARSE_NETHER.location()).addOptionalTag(Tags.Biomes.IS_SPARSE_END.location());
        this.m_206424_(Tags.Biomes.IS_DENSE).addTag(Tags.Biomes.IS_DENSE_OVERWORLD).addOptionalTag(Tags.Biomes.IS_DENSE_NETHER.location()).addOptionalTag(Tags.Biomes.IS_DENSE_END.location());
        this.m_206424_(Tags.Biomes.IS_WET).addTag(Tags.Biomes.IS_WET_OVERWORLD).addOptionalTag(Tags.Biomes.IS_WET_NETHER.location()).addOptionalTag(Tags.Biomes.IS_WET_END.location());
        this.m_206424_(Tags.Biomes.IS_DRY).addTag(Tags.Biomes.IS_DRY_OVERWORLD).addTag(Tags.Biomes.IS_DRY_NETHER).addTag(Tags.Biomes.IS_DRY_END);
        this.m_206424_(Tags.Biomes.IS_WATER).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER);
        this.m_206424_(Tags.Biomes.IS_MOUNTAIN).addTag(Tags.Biomes.IS_PEAK).addTag(Tags.Biomes.IS_SLOPE);
        this.m_206424_(Tags.Biomes.IS_UNDERGROUND).addTag(Tags.Biomes.IS_CAVE);
    }

    @SafeVarargs
    private void tag(ResourceKey<Biome> biome, TagKey<Biome>... tags) {
        for (TagKey<Biome> key : tags) {
            this.m_206424_(key).add(biome);
        }
    }

    @Override
    public String getName() {
        return "Forge Biome Tags";
    }
}