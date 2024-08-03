package dev.latvian.mods.kubejs.item.ingredient;

import com.google.common.collect.Iterables;
import dev.architectury.extensions.injected.InjectedRegistryEntryExtension;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;

public interface TagContext {

    TagContext EMPTY = new TagContext() {

        @Override
        public <T> boolean isEmpty(TagKey<T> tag) {
            return true;
        }

        @Override
        public <T> Iterable<Holder<T>> getTag(TagKey<T> tag) {
            KubeJS.LOGGER.warn("Tried to get tag {} from an empty tag context!", tag.location());
            return List.of();
        }
    };

    MutableObject<TagContext> INSTANCE = new MutableObject(EMPTY);

    static TagContext usingRegistry(RegistryAccess registryAccess) {
        return new TagContext() {

            @NotNull
            private <T> Registry<T> registry(TagKey<T> tag) {
                return registryAccess.registryOrThrow(tag.registry());
            }

            @Override
            public boolean areTagsBound() {
                return true;
            }

            @Override
            public <T> Iterable<Holder<T>> getTag(TagKey<T> tag) {
                return this.<T>registry(tag).getTagOrEmpty(tag);
            }

            @Override
            public <T> boolean contains(TagKey<T> tag, T value) {
                if (value instanceof InjectedRegistryEntryExtension<?> ext) {
                    Holder<T> holder = UtilsJS.cast(ext.arch$holder());
                    return holder.is(tag);
                } else {
                    Registry<T> reg = this.registry(tag);
                    return (Boolean) reg.getResourceKey(value).flatMap(reg::m_203636_).map(holderx -> holderx.is(tag)).orElseGet(() -> TagContext.super.contains(tag, value));
                }
            }
        };
    }

    static TagContext fromLoadResult(List<TagManager.LoadResult<?>> results) {
        final Map<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, Collection<Holder<?>>>> tags = (Map<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, Collection<Holder<?>>>>) results.stream().collect(Collectors.toMap(result -> UtilsJS.cast(result.key()), result -> UtilsJS.cast(result.tags())));
        if (!tags.containsKey(Registries.ITEM)) {
            ConsoleJS.getCurrent(ConsoleJS.SERVER).warn("Failed to load item tags during recipe event! Using replaceInput etc. will not work!");
            return EMPTY;
        } else {
            return new TagContext() {

                @Override
                public <T> Iterable<Holder<T>> getTag(TagKey<T> tag) {
                    return UtilsJS.cast(((Map) tags.get(tag.registry())).getOrDefault(tag.location(), Set.of()));
                }
            };
        }
    }

    default <T> boolean isEmpty(TagKey<T> tag) {
        return Iterables.isEmpty(this.getTag(tag));
    }

    default <T> boolean contains(TagKey<T> tag, T value) {
        if (this.isEmpty(tag)) {
            return false;
        } else {
            for (Holder<T> holder : this.getTag(tag)) {
                if (holder.value().equals(value)) {
                    return true;
                }
            }
            return false;
        }
    }

    default boolean areTagsBound() {
        return false;
    }

    <T> Iterable<Holder<T>> getTag(TagKey<T> var1);

    default Collection<ItemStack> patchIngredientTags(TagKey<Item> tag) {
        Iterable<Holder<Item>> c = this.getTag(tag);
        ArrayList<ItemStack> stacks = new ArrayList(c instanceof Collection<?> cl ? cl.size() : 3);
        for (Holder<Item> holder : c) {
            stacks.add(new ItemStack(holder.value()));
        }
        return (Collection<ItemStack>) (stacks.isEmpty() ? List.of() : stacks);
    }
}