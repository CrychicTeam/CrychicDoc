package dev.shadowsoffire.placebo.events;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeItemStack;

public class PlaceboEventFactory {

    public static InteractionResult onItemUse(ItemStack stack, UseOnContext ctx) {
        ItemUseEvent event = new ItemUseEvent(ctx);
        MinecraftForge.EVENT_BUS.post(event);
        return event.isCanceled() ? event.getCancellationResult() : null;
    }

    public static int getEnchantmentLevelSpecific(int level, IForgeItemStack stack, Enchantment ench) {
        HashMap<Enchantment, Integer> map = new HashMap();
        map.put(ench, level);
        GetEnchantmentLevelEvent event = new GetEnchantmentLevelEvent((ItemStack) stack, map);
        MinecraftForge.EVENT_BUS.post(event);
        return (Integer) event.getEnchantments().get(ench);
    }

    public static Map<Enchantment, Integer> getEnchantmentLevel(Map<Enchantment, Integer> enchantments, IForgeItemStack stack) {
        Map<Enchantment, Integer> var3 = new HashMap(enchantments);
        GetEnchantmentLevelEvent event = new GetEnchantmentLevelEvent((ItemStack) stack, var3);
        MinecraftForge.EVENT_BUS.post(event);
        return var3;
    }
}