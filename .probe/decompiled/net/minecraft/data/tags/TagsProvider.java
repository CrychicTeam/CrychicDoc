package net.minecraft.data.tags;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import org.slf4j.Logger;

public abstract class TagsProvider<T> implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected final PackOutput.PathProvider pathProvider;

    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    private final CompletableFuture<Void> contentsDone = new CompletableFuture();

    private final CompletableFuture<TagsProvider.TagLookup<T>> parentProvider;

    protected final ResourceKey<? extends Registry<T>> registryKey;

    private final Map<ResourceLocation, TagBuilder> builders = Maps.newLinkedHashMap();

    protected TagsProvider(PackOutput packOutput0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider2) {
        this(packOutput0, resourceKeyExtendsRegistryT1, completableFutureHolderLookupProvider2, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()));
    }

    protected TagsProvider(PackOutput packOutput0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider2, CompletableFuture<TagsProvider.TagLookup<T>> completableFutureTagsProviderTagLookupT3) {
        this.pathProvider = packOutput0.createPathProvider(PackOutput.Target.DATA_PACK, TagManager.getTagDir(resourceKeyExtendsRegistryT1));
        this.registryKey = resourceKeyExtendsRegistryT1;
        this.parentProvider = completableFutureTagsProviderTagLookupT3;
        this.lookupProvider = completableFutureHolderLookupProvider2;
    }

    @Override
    public final String getName() {
        return "Tags for " + this.registryKey.location();
    }

    protected abstract void addTags(HolderLookup.Provider var1);

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        return this.createContentsProvider().thenApply(p_275895_ -> {
            this.contentsDone.complete(null);
            return p_275895_;
        }).thenCombineAsync(this.parentProvider, (p_274778_, p_274779_) -> {
            record CombinedData<T>(HolderLookup.Provider f_273893_, TagsProvider.TagLookup<T> f_273819_) {

                private final HolderLookup.Provider contents;

                private final TagsProvider.TagLookup<T> parent;

                CombinedData(HolderLookup.Provider f_273893_, TagsProvider.TagLookup<T> f_273819_) {
                    this.contents = f_273893_;
                    this.parent = f_273819_;
                }
            }
            return new CombinedData(p_274778_, p_274779_);
        }).thenCompose(p_274774_ -> {
            HolderLookup.RegistryLookup<T> $$2 = p_274774_.contents.lookupOrThrow(this.registryKey);
            Predicate<ResourceLocation> $$3 = p_255496_ -> $$2.m_254902_(ResourceKey.create(this.registryKey, p_255496_)).isPresent();
            Predicate<ResourceLocation> $$4 = p_274776_ -> this.builders.containsKey(p_274776_) || p_274774_.parent.contains(TagKey.create(this.registryKey, p_274776_));
            return CompletableFuture.allOf((CompletableFuture[]) this.builders.entrySet().stream().map(p_255499_ -> {
                ResourceLocation $$4x = (ResourceLocation) p_255499_.getKey();
                TagBuilder $$5 = (TagBuilder) p_255499_.getValue();
                List<TagEntry> $$6 = $$5.build();
                List<TagEntry> $$7 = $$6.stream().filter(p_274771_ -> !p_274771_.verifyIfPresent($$3, $$4)).toList();
                if (!$$7.isEmpty()) {
                    throw new IllegalArgumentException(String.format(Locale.ROOT, "Couldn't define tag %s as it is missing following references: %s", $$4x, $$7.stream().map(Objects::toString).collect(Collectors.joining(","))));
                } else {
                    JsonElement $$8 = (JsonElement) TagFile.CODEC.encodeStart(JsonOps.INSTANCE, new TagFile($$6, false)).getOrThrow(false, LOGGER::error);
                    Path $$9 = this.pathProvider.json($$4x);
                    return DataProvider.saveStable(cachedOutput0, $$8, $$9);
                }
            }).toArray(CompletableFuture[]::new));
        });
    }

    protected TagsProvider.TagAppender<T> tag(TagKey<T> tagKeyT0) {
        TagBuilder $$1 = this.getOrCreateRawBuilder(tagKeyT0);
        return new TagsProvider.TagAppender<>($$1);
    }

    protected TagBuilder getOrCreateRawBuilder(TagKey<T> tagKeyT0) {
        return (TagBuilder) this.builders.computeIfAbsent(tagKeyT0.location(), p_236442_ -> TagBuilder.create());
    }

    public CompletableFuture<TagsProvider.TagLookup<T>> contentsGetter() {
        return this.contentsDone.thenApply(p_276016_ -> p_274772_ -> Optional.ofNullable((TagBuilder) this.builders.get(p_274772_.location())));
    }

    protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
        return this.lookupProvider.thenApply(p_274768_ -> {
            this.builders.clear();
            this.addTags(p_274768_);
            return p_274768_;
        });
    }

    protected static class TagAppender<T> {

        private final TagBuilder builder;

        protected TagAppender(TagBuilder tagBuilder0) {
            this.builder = tagBuilder0;
        }

        public final TagsProvider.TagAppender<T> add(ResourceKey<T> resourceKeyT0) {
            this.builder.addElement(resourceKeyT0.location());
            return this;
        }

        @SafeVarargs
        public final TagsProvider.TagAppender<T> add(ResourceKey<T>... resourceKeyT0) {
            for (ResourceKey<T> $$1 : resourceKeyT0) {
                this.builder.addElement($$1.location());
            }
            return this;
        }

        public TagsProvider.TagAppender<T> addOptional(ResourceLocation resourceLocation0) {
            this.builder.addOptionalElement(resourceLocation0);
            return this;
        }

        public TagsProvider.TagAppender<T> addTag(TagKey<T> tagKeyT0) {
            this.builder.addTag(tagKeyT0.location());
            return this;
        }

        public TagsProvider.TagAppender<T> addOptionalTag(ResourceLocation resourceLocation0) {
            this.builder.addOptionalTag(resourceLocation0);
            return this;
        }
    }

    @FunctionalInterface
    public interface TagLookup<T> extends Function<TagKey<T>, Optional<TagBuilder>> {

        static <T> TagsProvider.TagLookup<T> empty() {
            return p_275247_ -> Optional.empty();
        }

        default boolean contains(TagKey<T> tagKeyT0) {
            return ((Optional) this.apply(tagKeyT0)).isPresent();
        }
    }
}