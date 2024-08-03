package dev.xkmc.l2backpack;

import dev.xkmc.l2backpack.events.CapabilityEvents;
import dev.xkmc.l2complements.events.event.EnderPickupEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LCCompat {

    @SubscribeEvent
    public static void onEnderPickup(EnderPickupEvent event) {
        ItemStack stack = event.getStack();
        CapabilityEvents.tryInsertItem(event.getPlayer(), stack);
    }
}