package fuzs.puzzleslib.api.client.init.v1;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface DynamicItemDecorator {

    boolean renderItemDecorations(GuiGraphics var1, Font var2, ItemStack var3, int var4, int var5);
}