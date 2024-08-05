package net.minecraftforge.common.crafting.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.crafting.CraftingHelper;

public interface ICondition {

    static boolean shouldRegisterEntry(JsonElement json) {
        if (json instanceof JsonObject obj && obj.has("forge:conditions")) {
            return CraftingHelper.processConditions(obj, "forge:conditions", ICondition.IContext.TAGS_INVALID);
        }
        return true;
    }

    ResourceLocation getID();

    boolean test(ICondition.IContext var1);

    public interface IContext {

        ICondition.IContext EMPTY = new ICondition.IContext() {

            @Override
            public <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> registry) {
                return Collections.emptyMap();
            }
        };

        ICondition.IContext TAGS_INVALID = new ICondition.IContext() {

            @Override
            public <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> registry) {
                throw new UnsupportedOperationException("Usage of tag-based conditions is not permitted in this context!");
            }
        };

        default <T> Collection<Holder<T>> getTag(TagKey<T> key) {
            return (Collection<Holder<T>>) this.getAllTags(key.registry()).getOrDefault(key.location(), Set.of());
        }

        <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> var1);
    }
}