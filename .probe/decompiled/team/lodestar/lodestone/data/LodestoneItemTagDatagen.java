package team.lodestar.lodestone.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.registry.common.tag.LodestoneItemTags;

public class LodestoneItemTagDatagen extends ItemTagsProvider {

    public LodestoneItemTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockProvider, "lodestone", existingFileHelper);
    }

    @Override
    public String getName() {
        return "Malum Item Tags";
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        this.m_206424_(LodestoneItemTags.NUGGETS_COPPER);
        this.m_206424_(LodestoneItemTags.INGOTS_COPPER).add(Items.COPPER_INGOT);
        this.m_206424_(LodestoneItemTags.NUGGETS_LEAD);
        this.m_206424_(LodestoneItemTags.INGOTS_LEAD);
        this.m_206424_(LodestoneItemTags.NUGGETS_SILVER);
        this.m_206424_(LodestoneItemTags.INGOTS_SILVER);
        this.m_206424_(LodestoneItemTags.NUGGETS_ALUMINUM);
        this.m_206424_(LodestoneItemTags.INGOTS_ALUMINUM);
        this.m_206424_(LodestoneItemTags.NUGGETS_NICKEL);
        this.m_206424_(LodestoneItemTags.INGOTS_NICKEL);
        this.m_206424_(LodestoneItemTags.NUGGETS_URANIUM);
        this.m_206424_(LodestoneItemTags.INGOTS_URANIUM);
        this.m_206424_(LodestoneItemTags.NUGGETS_OSMIUM);
        this.m_206424_(LodestoneItemTags.INGOTS_OSMIUM);
        this.m_206424_(LodestoneItemTags.NUGGETS_ZINC);
        this.m_206424_(LodestoneItemTags.INGOTS_ZINC);
        this.m_206424_(LodestoneItemTags.NUGGETS_TIN);
        this.m_206424_(LodestoneItemTags.INGOTS_TIN);
    }
}