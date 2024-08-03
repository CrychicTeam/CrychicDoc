package noobanidus.mods.lootr.gen;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import noobanidus.mods.lootr.LootrTags;
import noobanidus.mods.lootr.init.ModItems;
import org.jetbrains.annotations.Nullable;

public class LootrItemTagsProvider extends ItemTagsProvider {

    public LootrItemTagsProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1, CompletableFuture<TagsProvider.TagLookup<Block>> completableFutureTagsProviderTagLookupBlock2, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput0, completableFutureHolderLookupProvider1, completableFutureTagsProviderTagLookupBlock2, "lootr", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206424_(LootrTags.Items.BARRELS).add(ModItems.BARREL.get());
        this.m_206424_(LootrTags.Items.CHESTS).add(ModItems.CHEST.get(), ModItems.INVENTORY.get());
        this.m_206424_(LootrTags.Items.TRAPPED_CHESTS).add(ModItems.TRAPPED_CHEST.get());
        this.m_206424_(LootrTags.Items.SHULKERS).add(ModItems.SHULKER.get());
        this.m_206424_(LootrTags.Items.CONTAINERS).addTags(new TagKey[] { LootrTags.Items.BARRELS, LootrTags.Items.CHESTS, LootrTags.Items.TRAPPED_CHESTS, LootrTags.Items.SHULKERS });
    }

    @Override
    public String getName() {
        return "Lootr Item Tags";
    }
}