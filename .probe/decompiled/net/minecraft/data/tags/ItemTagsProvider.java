package net.minecraft.data.tags;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public abstract class ItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {

    private final CompletableFuture<TagsProvider.TagLookup<Block>> blockTags;

    private final Map<TagKey<Block>, TagKey<Item>> tagsToCopy = new HashMap();

    public ItemTagsProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1, CompletableFuture<TagsProvider.TagLookup<Block>> completableFutureTagsProviderTagLookupBlock2) {
        super(packOutput0, Registries.ITEM, completableFutureHolderLookupProvider1, p_255790_ -> p_255790_.builtInRegistryHolder().key());
        this.blockTags = completableFutureTagsProviderTagLookupBlock2;
    }

    public ItemTagsProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1, CompletableFuture<TagsProvider.TagLookup<Item>> completableFutureTagsProviderTagLookupItem2, CompletableFuture<TagsProvider.TagLookup<Block>> completableFutureTagsProviderTagLookupBlock3) {
        super(packOutput0, Registries.ITEM, completableFutureHolderLookupProvider1, completableFutureTagsProviderTagLookupItem2, p_274765_ -> p_274765_.builtInRegistryHolder().key());
        this.blockTags = completableFutureTagsProviderTagLookupBlock3;
    }

    protected void copy(TagKey<Block> tagKeyBlock0, TagKey<Item> tagKeyItem1) {
        this.tagsToCopy.put(tagKeyBlock0, tagKeyItem1);
    }

    @Override
    protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
        return super.m_274574_().thenCombineAsync(this.blockTags, (p_274766_, p_274767_) -> {
            this.tagsToCopy.forEach((p_274763_, p_274764_) -> {
                TagBuilder $$3 = this.m_236451_(p_274764_);
                Optional<TagBuilder> $$4 = (Optional<TagBuilder>) p_274767_.apply(p_274763_);
                ((TagBuilder) $$4.orElseThrow(() -> new IllegalStateException("Missing block tag " + p_274764_.location()))).build().forEach($$3::m_215902_);
            });
            return p_274766_;
        });
    }
}