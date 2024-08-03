package dev.xkmc.l2complements.content.enchantment.core;

import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import java.util.Map.Entry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public interface SourceModifierEnchantment {

    static void modifySource(ItemStack stack, CreateSourceEvent event) {
        for (Entry<Enchantment, Integer> ent : stack.getAllEnchantments().entrySet()) {
            if (ent.getKey() instanceof SourceModifierEnchantment mod) {
                mod.modify(event, stack, (Integer) ent.getValue());
            }
        }
    }

    void modify(CreateSourceEvent var1, ItemStack var2, int var3);
}