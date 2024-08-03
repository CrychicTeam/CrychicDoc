package dev.xkmc.l2weaponry.init.materials;

import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public interface LegendaryToolFactory<T extends Item> {

    T get(Tier var1, int var2, float var3, Item.Properties var4, ExtraToolConfig var5);
}