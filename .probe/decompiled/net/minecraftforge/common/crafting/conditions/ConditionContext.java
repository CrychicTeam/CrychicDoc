package net.minecraftforge.common.crafting.conditions;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagManager;

public class ConditionContext implements ICondition.IContext {

    private final TagManager tagManager;

    private Map<ResourceKey<?>, Map<ResourceLocation, Collection<Holder<?>>>> loadedTags = null;

    public ConditionContext(TagManager tagManager) {
        this.tagManager = tagManager;
    }

    @Override
    public <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> registry) {
        if (this.loadedTags == null) {
            List<TagManager.LoadResult<?>> tags = this.tagManager.getResult();
            if (tags.isEmpty()) {
                throw new IllegalStateException("Tags have not been loaded yet.");
            }
            this.loadedTags = new IdentityHashMap();
            for (TagManager.LoadResult<?> loadResult : tags) {
                Map<ResourceLocation, Collection<? extends Holder<?>>> map = Collections.unmodifiableMap(loadResult.tags());
                this.loadedTags.put(loadResult.key(), map);
            }
        }
        return (Map<ResourceLocation, Collection<Holder<T>>>) this.loadedTags.getOrDefault(registry, Collections.emptyMap());
    }
}