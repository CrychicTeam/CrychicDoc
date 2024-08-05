package org.violetmoon.zeta.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.violetmoon.zeta.module.IDisableable;
import org.violetmoon.zeta.registry.CreativeTabManager;

public interface IZetaItem extends IDisableable<IZetaItem> {

    default Item getItem() {
        return (Item) this;
    }

    default Item setCreativeTab(ResourceKey<CreativeModeTab> tab) {
        Item i = this.getItem();
        CreativeTabManager.addToCreativeTab(tab, i);
        return i;
    }

    default Item setCreativeTab(ResourceKey<CreativeModeTab> tab, ItemLike parent, boolean behindParent) {
        Item i = this.getItem();
        CreativeTabManager.addToCreativeTabNextTo(tab, i, parent, behindParent);
        return i;
    }
}