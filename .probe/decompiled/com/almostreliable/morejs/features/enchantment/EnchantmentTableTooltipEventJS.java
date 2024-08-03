package com.almostreliable.morejs.features.enchantment;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

public class EnchantmentTableTooltipEventJS extends EnchantmentTableEventJS {

    private final int slot;

    private final List<Object> components;

    @Nullable
    EnchantmentInstance clue;

    public EnchantmentTableTooltipEventJS(ItemStack item, ItemStack secondItem, Level level, Player player, EnchantmentMenu menu, int slot, List<Object> components) {
        super(item, secondItem, level, player, menu);
        this.slot = slot;
        this.components = components;
    }

    public List<Object> getLines() {
        return this.components;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getRequiredLevel() {
        return this.menu.costs[this.slot];
    }

    public EnchantmentInstance getClue() {
        if (this.clue == null) {
            Enchantment enchantment = Enchantment.byId(this.menu.enchantClue[this.slot]);
            if (enchantment == null) {
                throw new IllegalStateException("Enchantment not found for id: " + this.menu.enchantClue[this.slot]);
            }
            this.clue = new EnchantmentInstance(enchantment, this.menu.levelClue[this.slot]);
        }
        return this.clue;
    }

    public ResourceLocation getClueId() {
        return BuiltInRegistries.ENCHANTMENT.getKey(this.getClue().enchantment);
    }
}