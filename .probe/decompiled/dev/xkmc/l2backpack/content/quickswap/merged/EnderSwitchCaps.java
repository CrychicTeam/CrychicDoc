package dev.xkmc.l2backpack.content.quickswap.merged;

import dev.xkmc.l2backpack.content.capability.PickupTrace;
import dev.xkmc.l2backpack.content.common.BaseBagInvWrapper;
import dev.xkmc.l2backpack.content.remote.player.EnderBackpackCaps;
import net.minecraft.world.item.ItemStack;

public class EnderSwitchCaps extends BaseBagInvWrapper {

    private final EnderBackpackCaps ender;

    public EnderSwitchCaps(ItemStack stack) {
        super(stack);
        this.ender = new EnderBackpackCaps(stack);
    }

    @Override
    public int doPickup(ItemStack stack, PickupTrace trace) {
        return super.doPickup(stack, trace) + this.ender.doPickup(stack, trace);
    }
}