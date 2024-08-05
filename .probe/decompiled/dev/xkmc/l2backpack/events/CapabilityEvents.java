package dev.xkmc.l2backpack.events;

import dev.xkmc.l2backpack.compat.CuriosCompat;
import dev.xkmc.l2backpack.content.capability.PickupModeCap;
import dev.xkmc.l2backpack.content.capability.PickupTrace;
import dev.xkmc.l2backpack.content.remote.common.WorldStorageCapability;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "l2backpack", bus = Bus.FORGE)
public class CapabilityEvents {

    @SubscribeEvent
    public static void onAttachLevelCapabilities(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject() instanceof ServerLevel level && level.m_46472_() == Level.OVERWORLD) {
            event.addCapability(new ResourceLocation("l2backpack", "world_storage"), new WorldStorageCapability(level));
        }
    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack stack = event.getItem().getItem();
            ItemStack copy = stack.copy();
            int count = stack.getCount();
            tryInsertItem(player, stack);
            if (count != stack.getCount()) {
                copy.shrink(stack.getCount());
                player.take(event.getItem(), copy.getCount());
                CriteriaTriggers.INVENTORY_CHANGED.trigger(player, player.m_150109_(), copy);
            }
        }
    }

    public static void tryInsertItem(ServerPlayer player, ItemStack stack) {
        ItemStack chest = player.m_6844_(EquipmentSlot.CHEST);
        chest.getCapability(PickupModeCap.TOKEN).resolve().ifPresent(cap -> cap.doPickup(stack, new PickupTrace(false, player)));
        if (!stack.isEmpty()) {
            CuriosCompat.getSlot(player, e -> {
                if (stack.isEmpty()) {
                    return false;
                } else {
                    e.getCapability(PickupModeCap.TOKEN).resolve().ifPresent(cap -> cap.doPickup(stack, new PickupTrace(false, player)));
                    return false;
                }
            });
        }
    }
}