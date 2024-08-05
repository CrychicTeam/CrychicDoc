package org.violetmoon.zeta.item.ext;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ItemExtensionFactory {

    IZetaItemExtensions getInternal(ItemStack var1);

    default IZetaItemExtensions get(ItemStack stack) {
        Item var3 = stack.getItem();
        return var3 instanceof IZetaItemExtensions ? (IZetaItemExtensions) var3 : this.getInternal(stack);
    }
}