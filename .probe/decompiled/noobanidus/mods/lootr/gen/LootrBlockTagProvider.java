package noobanidus.mods.lootr.gen;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import noobanidus.mods.lootr.LootrTags;
import noobanidus.mods.lootr.init.ModBlocks;
import org.jetbrains.annotations.Nullable;

public class LootrBlockTagProvider extends BlockTagsProvider {

    public LootrBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "lootr", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206424_(BlockTags.SHULKER_BOXES).add(ModBlocks.SHULKER.get());
        this.m_206424_(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.TROPHY.get());
        this.m_206424_(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.CHEST.get(), ModBlocks.TRAPPED_CHEST.get(), ModBlocks.BARREL.get());
        this.m_206424_(BlockTags.GUARDED_BY_PIGLINS).add(ModBlocks.CHEST.get(), ModBlocks.TRAPPED_CHEST.get(), ModBlocks.BARREL.get());
        this.m_206424_(Tags.Blocks.CHESTS_WOODEN).add(ModBlocks.CHEST.get());
        this.m_206424_(Tags.Blocks.CHESTS_TRAPPED).add(ModBlocks.TRAPPED_CHEST.get());
        this.m_206424_(Tags.Blocks.BARRELS).add(ModBlocks.BARREL.get());
        this.m_206424_(LootrTags.Blocks.BARRELS).add(ModBlocks.BARREL.get());
        this.m_206424_(LootrTags.Blocks.CHESTS).add(ModBlocks.CHEST.get(), ModBlocks.INVENTORY.get());
        this.m_206424_(LootrTags.Blocks.TRAPPED_CHESTS).add(ModBlocks.TRAPPED_CHEST.get());
        this.m_206424_(LootrTags.Blocks.SHULKERS).add(ModBlocks.SHULKER.get());
        this.m_206424_(LootrTags.Blocks.CONTAINERS).addTags(new TagKey[] { LootrTags.Blocks.BARRELS, LootrTags.Blocks.CHESTS, LootrTags.Blocks.TRAPPED_CHESTS, LootrTags.Blocks.SHULKERS });
        this.m_206424_(LootrTags.Blocks.CONVERT_BARRELS).add(Blocks.BARREL);
        this.m_206424_(LootrTags.Blocks.CONVERT_CHESTS).add(Blocks.CHEST);
        this.m_206424_(LootrTags.Blocks.CONVERT_TRAPPED_CHESTS).add(Blocks.TRAPPED_CHEST);
        this.m_206424_(LootrTags.Blocks.CONVERT_SHULKERS).add(Blocks.SHULKER_BOX);
        this.m_206424_(LootrTags.Blocks.CONVERT_BLOCK).addTags(new TagKey[] { LootrTags.Blocks.CONVERT_BARRELS, LootrTags.Blocks.CONVERT_CHESTS, LootrTags.Blocks.CONVERT_TRAPPED_CHESTS, LootrTags.Blocks.CONVERT_SHULKERS });
    }

    @Override
    public String getName() {
        return "Lootr Block Tags";
    }
}