package dev.xkmc.l2weaponry.init.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2weaponry.init.data.TagGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class LWItemBuilder<T extends Item, P> extends ItemBuilder<T, P> {

    protected LWItemBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Item.Properties, T> factory) {
        super(owner, parent, name, callback, factory);
    }

    public LWItemBuilder<T, P> optionalTag(boolean optional, TagKey<Item> tag) {
        if (optional) {
            TagGen.addItem(tag, new ResourceLocation(this.getOwner().getModid(), this.getName()));
        } else {
            this.tag(new TagKey[] { tag });
        }
        return this;
    }
}