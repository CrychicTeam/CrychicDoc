package dev.xkmc.l2backpack.content.common;

import dev.xkmc.l2backpack.content.bag.AbstractBag;
import dev.xkmc.l2backpack.content.remote.player.EnderBackpackItem;
import java.util.Optional;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record InvTooltip(TooltipInvItem item, ItemStack stack) implements TooltipComponent {

    public static Optional<TooltipComponent> get(BaseBagItem item, ItemStack stack) {
        if (Screen.hasShiftDown()) {
            return Optional.empty();
        } else {
            ListTag list = BaseBagItem.getListTag(stack);
            return !list.isEmpty() ? Optional.of(new InvTooltip(item, stack)) : Optional.empty();
        }
    }

    public static Optional<TooltipComponent> get(EnderBackpackItem item, ItemStack stack) {
        return Screen.hasShiftDown() ? Optional.empty() : Optional.of(new InvTooltip(item, stack));
    }

    public static Optional<TooltipComponent> get(AbstractBag item, ItemStack stack) {
        return Screen.hasAltDown() ? Optional.of(new InvTooltip(item, stack)) : Optional.empty();
    }
}