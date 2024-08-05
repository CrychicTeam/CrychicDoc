package net.minecraftforge.registries.tags;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

public interface ITagManager<V> extends Iterable<ITag<V>> {

    @NotNull
    ITag<V> getTag(@NotNull TagKey<V> var1);

    @NotNull
    Optional<IReverseTag<V>> getReverseTag(@NotNull V var1);

    boolean isKnownTagName(@NotNull TagKey<V> var1);

    @NotNull
    Stream<ITag<V>> stream();

    @NotNull
    Stream<TagKey<V>> getTagNames();

    @NotNull
    TagKey<V> createTagKey(@NotNull ResourceLocation var1);

    @NotNull
    TagKey<V> createOptionalTagKey(@NotNull ResourceLocation var1, @NotNull Set<? extends Supplier<V>> var2);

    void addOptionalTagDefaults(@NotNull TagKey<V> var1, @NotNull Set<? extends Supplier<V>> var2);
}