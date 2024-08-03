package io.github.lightman314.lightmanscurrency.client.colors;

import io.github.lightman314.lightmanscurrency.common.items.TicketItem;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TicketColor implements ItemColor {

    @Override
    public int getColor(@NotNull ItemStack itemStack, int color) {
        if (color == 0) {
            return TicketItem.GetTicketColor(itemStack);
        } else {
            return color == 1 ? 16777215 - TicketItem.GetTicketColor(itemStack) : 16777215;
        }
    }
}