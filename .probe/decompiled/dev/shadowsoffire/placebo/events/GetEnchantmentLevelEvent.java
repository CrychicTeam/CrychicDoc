package dev.shadowsoffire.placebo.events;

import java.util.Map;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.Event;

public class GetEnchantmentLevelEvent extends Event {

    protected final ItemStack stack;

    protected final Map<Enchantment, Integer> enchantments;

    public GetEnchantmentLevelEvent(ItemStack stack, Map<Enchantment, Integer> enchantments) {
        this.stack = stack;
        this.enchantments = enchantments;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }
}