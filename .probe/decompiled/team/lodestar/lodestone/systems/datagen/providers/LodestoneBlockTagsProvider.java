package team.lodestar.lodestone.systems.datagen.providers;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.block.LodestoneBlockProperties;
import team.lodestar.lodestone.systems.datagen.LodestoneDatagenBlockData;

public abstract class LodestoneBlockTagsProvider extends BlockTagsProvider {

    public LodestoneBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    public void addTagsFromBlockProperties(Collection<Block> blocks) {
        for (Block block : blocks) {
            LodestoneBlockProperties properties = (LodestoneBlockProperties) block.f_60439_;
            LodestoneDatagenBlockData data = properties.getDatagenData();
            for (TagKey<Block> tag : data.getTags()) {
                this.m_206424_(tag).add(block);
            }
        }
    }
}