package net.minecraft.data.tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

public abstract class IntrinsicHolderTagsProvider<T> extends TagsProvider<T> {

    private final Function<T, ResourceKey<T>> keyExtractor;

    public IntrinsicHolderTagsProvider(PackOutput packOutput0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider2, Function<T, ResourceKey<T>> functionTResourceKeyT3) {
        super(packOutput0, resourceKeyExtendsRegistryT1, completableFutureHolderLookupProvider2);
        this.keyExtractor = functionTResourceKeyT3;
    }

    public IntrinsicHolderTagsProvider(PackOutput packOutput0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider2, CompletableFuture<TagsProvider.TagLookup<T>> completableFutureTagsProviderTagLookupT3, Function<T, ResourceKey<T>> functionTResourceKeyT4) {
        super(packOutput0, resourceKeyExtendsRegistryT1, completableFutureHolderLookupProvider2, completableFutureTagsProviderTagLookupT3);
        this.keyExtractor = functionTResourceKeyT4;
    }

    protected IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> tag(TagKey<T> tagKeyT0) {
        TagBuilder $$1 = this.m_236451_(tagKeyT0);
        return new IntrinsicHolderTagsProvider.IntrinsicTagAppender<>($$1, this.keyExtractor);
    }

    protected static class IntrinsicTagAppender<T> extends TagsProvider.TagAppender<T> {

        private final Function<T, ResourceKey<T>> keyExtractor;

        IntrinsicTagAppender(TagBuilder tagBuilder0, Function<T, ResourceKey<T>> functionTResourceKeyT1) {
            super(tagBuilder0);
            this.keyExtractor = functionTResourceKeyT1;
        }

        public IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> addTag(TagKey<T> tagKeyT0) {
            super.addTag(tagKeyT0);
            return this;
        }

        public final IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> add(T t0) {
            this.m_255204_((ResourceKey) this.keyExtractor.apply(t0));
            return this;
        }

        @SafeVarargs
        public final IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> add(T... t0) {
            Stream.of(t0).map(this.keyExtractor).forEach(this::m_255204_);
            return this;
        }
    }
}