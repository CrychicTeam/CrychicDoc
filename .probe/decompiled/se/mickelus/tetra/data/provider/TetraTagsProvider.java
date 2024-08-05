package se.mickelus.tetra.data.provider;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TetraTagsProvider extends TagsProvider<Block> {

    public TetraTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, ExistingFileHelper existingFileHelper) {
        super(packOutput, Registries.BLOCK, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        TetraTagsProvider.MultiblockSchematicEntry[] schematics = new TetraTagsProvider.MultiblockSchematicEntry[] { new TetraTagsProvider.MultiblockSchematicEntry("stonecutter", 3, 2), new TetraTagsProvider.MultiblockSchematicEntry("earthpiercer", 2, 2), new TetraTagsProvider.MultiblockSchematicEntry("extractor", 3, 3) };
        TagsProvider.TagAppender<Block> schematicsAppender = this.m_206424_(BlockTags.create(new ResourceLocation("tetra", "multiblock_schematic")));
        for (TetraTagsProvider.MultiblockSchematicEntry schematic : schematics) {
            TagKey<Block> tag = BlockTags.create(new ResourceLocation("tetra", schematic.id));
            TagsProvider.TagAppender<Block> appender = this.m_206424_(tag);
            schematicsAppender.addTag(tag);
            for (int h = 0; h < schematic.width; h++) {
                for (int v = 0; v < schematic.height; v++) {
                    String id = String.format("%s_%d_%d", schematic.id, h, v);
                    appender.addOptional(new ResourceLocation("tetra", id));
                }
            }
        }
    }

    static record MultiblockSchematicEntry(String id, int width, int height) {
    }
}