package org.violetmoon.zeta.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import org.violetmoon.zeta.item.ext.IZetaItemExtensions;

public class ZetaArmorItem extends ArmorItem implements IZetaItemExtensions {

    public ZetaArmorItem(ArmorMaterial mat, ArmorItem.Type type, Item.Properties props) {
        super(mat, type, props);
    }
}