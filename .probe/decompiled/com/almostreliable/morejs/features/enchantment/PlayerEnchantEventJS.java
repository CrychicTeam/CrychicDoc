package com.almostreliable.morejs.features.enchantment;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

public class PlayerEnchantEventJS extends EnchantmentTableServerEventJS {

    private final int clickedButton;

    public PlayerEnchantEventJS(int clickedButton, ItemStack item, ItemStack secondItem, Level level, BlockPos pos, Player player, EnchantmentMenuProcess state) {
        super(item, secondItem, level, pos, player, state);
        this.clickedButton = clickedButton;
    }

    public int getClickedButton() {
        return this.clickedButton;
    }

    public int getCosts() {
        return this.menu.costs[this.clickedButton];
    }

    public List<ResourceLocation> getEnchantmentIds() {
        return this.state.getEnchantments(this.clickedButton).stream().map(ei -> ei.enchantment).map(BuiltInRegistries.ENCHANTMENT::m_7981_).toList();
    }

    public List<EnchantmentInstance> getEnchantments() {
        return this.state.getEnchantments(this.clickedButton);
    }
}