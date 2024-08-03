package com.mna.enchantments.framework;

import com.mna.items.base.IManaRepairable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class EnchantmentEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Inventory inv = event.player.getInventory();
        int numSlots = event.player.getInventory().getContainerSize();
        for (int i = 0; i < numSlots; i++) {
            ItemStack invStack = inv.getItem(i);
            if (invStack.isRepairable() && invStack.getEnchantmentLevel(EnchantmentInit.MANA_REPAIR.get()) > 0) {
                IManaRepairable.tickRepair(invStack, event.player, 0.25F, 0.01F, i);
            }
        }
    }
}