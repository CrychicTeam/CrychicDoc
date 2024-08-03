package dev.xkmc.l2complements.events;

import dev.xkmc.l2complements.content.item.misc.WarpStone;
import dev.xkmc.l2screentracker.click.writable.ClickedPlayerSlotResult;
import dev.xkmc.l2screentracker.click.writable.WritableStackClickHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class L2ComplementsClick extends WritableStackClickHandler {

    public L2ComplementsClick(ResourceLocation rl) {
        super(rl);
    }

    protected void handle(ServerPlayer player, ClickedPlayerSlotResult result) {
        if (result.stack().getItem() instanceof WarpStone stone) {
            stone.use(player, result.stack());
            result.container().update();
        }
    }

    public boolean isAllowed(ItemStack stack) {
        return stack.getItem() instanceof WarpStone;
    }
}