package dev.architectury.registry.fuel.forge;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FuelRegistryImpl {

    private static final Object2IntMap<ItemLike> ITEMS = new Object2IntLinkedOpenHashMap();

    public static void register(int time, ItemLike... items) {
        for (ItemLike item : items) {
            ITEMS.put(item, time);
        }
    }

    public static int get(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null);
    }

    @SubscribeEvent
    public static void event(FurnaceFuelBurnTimeEvent event) {
        if (!event.getItemStack().isEmpty()) {
            int time = ITEMS.getOrDefault(event.getItemStack().getItem(), Integer.MIN_VALUE);
            if (time != Integer.MIN_VALUE) {
                event.setBurnTime(time);
            }
        }
    }

    static {
        MinecraftForge.EVENT_BUS.register(FuelRegistryImpl.class);
    }
}