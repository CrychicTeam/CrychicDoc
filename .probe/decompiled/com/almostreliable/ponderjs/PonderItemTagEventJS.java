package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.mixin.PonderTagRegistryAccessor;
import com.almostreliable.ponderjs.util.PonderPlatform;
import com.google.common.collect.Multimap;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.PonderTagRegistry;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class PonderItemTagEventJS extends EventJS {

    public void createTag(String id, ItemStack displayItem, String title, String description, @Nullable Ingredient ingredient) {
        PonderJS.getTagByName(id).ifPresent(tag -> {
            throw new IllegalArgumentException("Tag " + id + " already exists");
        });
        ResourceLocation idWithNamespace = PonderJS.appendKubeToId(id);
        PonderTag ponderTag = new PonderTag(idWithNamespace).item(displayItem.getItem()).defaultLang(title, description);
        PonderRegistry.TAGS.listTag(ponderTag);
        if (ingredient != null) {
            this.add(ponderTag, ingredient);
        }
        PonderJS.NAMESPACES.add(idWithNamespace.getNamespace());
    }

    public void createTag(String id, ItemStack displayItem, String title, String description) {
        this.createTag(id, displayItem, title, description, null);
    }

    public void removeTag(PonderTag... tags) {
        for (PonderTag tag : tags) {
            Set<ResourceLocation> items = PonderRegistry.TAGS.getItems(tag);
            PonderRegistry.TAGS.getListedTags().remove(tag);
            this.remove(tag, items);
        }
    }

    public void add(PonderTag tag, Ingredient ingredient) {
        if (!ingredient.isEmpty()) {
            PonderTagRegistry.TagBuilder tagBuilder = PonderRegistry.TAGS.forTag(tag);
            for (ItemStack item : ingredient.getItems()) {
                tagBuilder.add(item.getItem());
            }
        }
    }

    public void remove(PonderTag tag, Ingredient ingredient) {
        if (!ingredient.isEmpty()) {
            Set<ResourceLocation> ids = (Set<ResourceLocation>) Arrays.stream(ingredient.getItems()).map(ItemStack::m_41720_).map(PonderPlatform::getItemName).collect(Collectors.toSet());
            this.remove(tag, ids);
        }
    }

    private void remove(PonderTag tag, Set<ResourceLocation> items) {
        Multimap<ResourceLocation, PonderTag> tagMap = ((PonderTagRegistryAccessor) PonderRegistry.TAGS).getTags();
        for (ResourceLocation item : items) {
            Collection<PonderTag> tagsForItem = tagMap.get(item);
            if (tagsForItem.remove(tag)) {
                ConsoleJS.CLIENT.info("Removed ponder tag " + tag.getId() + " from item " + item);
            }
        }
    }
}