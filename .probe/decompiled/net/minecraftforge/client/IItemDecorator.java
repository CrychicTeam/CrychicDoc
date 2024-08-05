package net.minecraftforge.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public interface IItemDecorator {

    boolean render(GuiGraphics var1, Font var2, ItemStack var3, int var4, int var5);
}