package org.violetmoon.quark.api;

import net.minecraft.world.item.ItemStack;
import org.violetmoon.quark.content.tools.base.RuneColor;

public interface IRuneColorProvider {

    RuneColor getRuneColor(ItemStack var1);
}