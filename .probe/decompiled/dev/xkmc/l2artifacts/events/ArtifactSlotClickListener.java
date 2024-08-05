package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.common.ArtifactChestMenuPvd;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenu;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapItem;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapMenuPvd;
import dev.xkmc.l2screentracker.click.SlotClickHandler;
import dev.xkmc.l2screentracker.screen.base.ScreenTracker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ArtifactSlotClickListener extends SlotClickHandler {

    public static boolean canOpen(ItemStack stack) {
        return stack.getItem() instanceof ArtifactChestItem || stack.getItem() instanceof ArtifactSwapItem;
    }

    public ArtifactSlotClickListener() {
        super(new ResourceLocation("l2artifacts", "artifacts"));
    }

    public boolean isAllowed(ItemStack itemStack) {
        return canOpen(itemStack);
    }

    public void handle(ServerPlayer player, int index, int slot, int wid) {
        if (slot >= 0) {
            ItemStack stack = player.m_150109_().getItem(slot);
            if (stack.getItem() instanceof ArtifactChestItem) {
                new ArtifactChestMenuPvd(FilteredMenu::new, player, slot, stack).open();
            } else if (stack.getItem() instanceof ArtifactSwapItem) {
                ScreenTracker.onServerOpen(player);
                new ArtifactSwapMenuPvd(player, slot, stack).open();
            }
        }
    }
}