package dev.xkmc.modulargolems.init.material;

import net.minecraft.world.item.Item;

public interface IGolemWeaponMaterial {

    int getDamage();

    String getName();

    Item getIngot();

    default Item.Properties modify(Item.Properties prop) {
        if (this.fireResistant()) {
            prop = prop.fireResistant();
        }
        return prop;
    }

    boolean fireResistant();
}