package net.minecraft.tags;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.DependencySorter;
import org.slf4j.Logger;

public class TagLoader<T> {

    private static final Logger LOGGER = LogUtils.getLogger();

    final Function<ResourceLocation, Optional<? extends T>> idToValue;

    private final String directory;

    public TagLoader(Function<ResourceLocation, Optional<? extends T>> functionResourceLocationOptionalExtendsT0, String string1) {
        this.idToValue = functionResourceLocationOptionalExtendsT0;
        this.directory = string1;
    }

    public Map<ResourceLocation, List<TagLoader.EntryWithSource>> load(ResourceManager resourceManager0) {
        Map<ResourceLocation, List<TagLoader.EntryWithSource>> $$1 = Maps.newHashMap();
        FileToIdConverter $$2 = FileToIdConverter.json(this.directory);
        for (Entry<ResourceLocation, List<Resource>> $$3 : $$2.listMatchingResourceStacks(resourceManager0).entrySet()) {
            ResourceLocation $$4 = (ResourceLocation) $$3.getKey();
            ResourceLocation $$5 = $$2.fileToId($$4);
            for (Resource $$6 : (List) $$3.getValue()) {
                try {
                    Reader $$7 = $$6.openAsReader();
                    try {
                        JsonElement $$8 = JsonParser.parseReader($$7);
                        List<TagLoader.EntryWithSource> $$9 = (List<TagLoader.EntryWithSource>) $$1.computeIfAbsent($$5, p_215974_ -> new ArrayList());
                        TagFile $$10 = (TagFile) TagFile.CODEC.parse(new Dynamic(JsonOps.INSTANCE, $$8)).getOrThrow(false, LOGGER::error);
                        if ($$10.replace()) {
                            $$9.clear();
                        }
                        String $$11 = $$6.sourcePackId();
                        $$10.entries().forEach(p_215997_ -> $$9.add(new TagLoader.EntryWithSource(p_215997_, $$11)));
                    } catch (Throwable var16) {
                        if ($$7 != null) {
                            try {
                                $$7.close();
                            } catch (Throwable var15) {
                                var16.addSuppressed(var15);
                            }
                        }
                        throw var16;
                    }
                    if ($$7 != null) {
                        $$7.close();
                    }
                } catch (Exception var17) {
                    LOGGER.error("Couldn't read tag list {} from {} in data pack {}", new Object[] { $$5, $$4, $$6.sourcePackId(), var17 });
                }
            }
        }
        return $$1;
    }

    private Either<Collection<TagLoader.EntryWithSource>, Collection<T>> build(TagEntry.Lookup<T> tagEntryLookupT0, List<TagLoader.EntryWithSource> listTagLoaderEntryWithSource1) {
        Builder<T> $$2 = ImmutableSet.builder();
        List<TagLoader.EntryWithSource> $$3 = new ArrayList();
        for (TagLoader.EntryWithSource $$4 : listTagLoaderEntryWithSource1) {
            if (!$$4.entry().build(tagEntryLookupT0, $$2::add)) {
                $$3.add($$4);
            }
        }
        return $$3.isEmpty() ? Either.right($$2.build()) : Either.left($$3);
    }

    public Map<ResourceLocation, Collection<T>> build(Map<ResourceLocation, List<TagLoader.EntryWithSource>> mapResourceLocationListTagLoaderEntryWithSource0) {
        final Map<ResourceLocation, Collection<T>> $$1 = Maps.newHashMap();
        TagEntry.Lookup<T> $$2 = new TagEntry.Lookup<T>() {

            @Nullable
            @Override
            public T element(ResourceLocation p_216039_) {
                return (T) ((Optional) TagLoader.this.idToValue.apply(p_216039_)).orElse(null);
            }

            @Nullable
            @Override
            public Collection<T> tag(ResourceLocation p_216041_) {
                return (Collection<T>) $$1.get(p_216041_);
            }
        };
        DependencySorter<ResourceLocation, TagLoader.SortingEntry> $$3 = new DependencySorter<>();
        mapResourceLocationListTagLoaderEntryWithSource0.forEach((p_284685_, p_284686_) -> $$3.addEntry(p_284685_, new TagLoader.SortingEntry(p_284686_)));
        $$3.orderByDependencies((p_284682_, p_284683_) -> this.build($$2, p_284683_.entries).ifLeft(p_215977_ -> LOGGER.error("Couldn't load tag {} as it is missing following references: {}", p_284682_, p_215977_.stream().map(Objects::toString).collect(Collectors.joining(", ")))).ifRight(p_216001_ -> $$1.put(p_284682_, p_216001_)));
        return $$1;
    }

    public Map<ResourceLocation, Collection<T>> loadAndBuild(ResourceManager resourceManager0) {
        return this.build(this.load(resourceManager0));
    }

    public static record EntryWithSource(TagEntry f_216042_, String f_216043_) {

        private final TagEntry entry;

        private final String source;

        public EntryWithSource(TagEntry f_216042_, String f_216043_) {
            this.entry = f_216042_;
            this.source = f_216043_;
        }

        public String toString() {
            return this.entry + " (from " + this.source + ")";
        }
    }

    static record SortingEntry(List<TagLoader.EntryWithSource> f_283922_) implements DependencySorter.Entry<ResourceLocation> {

        private final List<TagLoader.EntryWithSource> entries;

        SortingEntry(List<TagLoader.EntryWithSource> f_283922_) {
            this.entries = f_283922_;
        }

        @Override
        public void visitRequiredDependencies(Consumer<ResourceLocation> p_285529_) {
            this.entries.forEach(p_285236_ -> p_285236_.entry.visitRequiredDependencies(p_285529_));
        }

        @Override
        public void visitOptionalDependencies(Consumer<ResourceLocation> p_285469_) {
            this.entries.forEach(p_284943_ -> p_284943_.entry.visitOptionalDependencies(p_285469_));
        }
    }
}