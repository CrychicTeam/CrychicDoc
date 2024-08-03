package se.mickelus.tetra.blocks.scroll;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class ScrollItemColor implements ItemColor {

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        return tintIndex == 1 ? ScrollData.readRibbonFast(itemStack) : 16777215;
    }
}