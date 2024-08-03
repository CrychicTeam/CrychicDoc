package com.almostreliable.ponderjs.api;

import com.almostreliable.ponderjs.PonderJS;
import com.almostreliable.ponderjs.util.PonderPlatform;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.PonderTagRegistry;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public abstract class AbstractPonderBuilder<S extends AbstractPonderBuilder<S>> {

    protected Set<Item> items;

    public AbstractPonderBuilder(Set<Item> items) {
        this.items = items;
    }

    protected abstract S getSelf();

    protected ResourceLocation createTitleTranslationKey(String scene) {
        return PonderJS.appendKubeToId(scene);
    }

    public S tag(PonderTag... tags) {
        for (PonderTag tag : tags) {
            PonderTagRegistry.TagBuilder tagBuilder = PonderRegistry.TAGS.forTag(tag);
            this.items.forEach(tagBuilder::add);
        }
        return this.getSelf();
    }

    protected S addStoryBoard(ResourceLocation id, Item item, ResourceLocation schematic, PonderStoryBoardEntry.PonderStoryBoard scene) {
        PonderJS.NAMESPACES.add(id.getNamespace());
        PonderStoryBoardEntry entry = new PonderStoryBoardEntry(scene, id.getNamespace(), schematic, PonderPlatform.getItemName(item));
        PonderRegistry.addStoryBoard(entry);
        PonderJS.STORIES_MANAGER.add(entry);
        return this.getSelf();
    }

    protected S addNamedStoryBoard(ResourceLocation id, String displayName, Item item, ResourceLocation schematic, PonderStoryBoardEntry.PonderStoryBoard scene) {
        return this.addStoryBoard(id, item, schematic, (builder, util) -> {
            builder.title(id.getPath(), displayName);
            scene.program(builder, util);
        });
    }
}