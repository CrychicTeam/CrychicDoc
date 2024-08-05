package org.violetmoon.zeta.recipe;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public interface IZetaCondition {

    ResourceLocation getID();

    boolean test(IZetaCondition.IContext var1);

    public interface IContext {

        default <T> Collection<Holder<T>> getTag(TagKey<T> key) {
            return (Collection<Holder<T>>) this.getAllTags(key.registry()).getOrDefault(key.location(), Set.of());
        }

        <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> var1);
    }
}