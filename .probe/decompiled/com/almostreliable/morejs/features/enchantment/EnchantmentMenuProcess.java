package com.almostreliable.morejs.features.enchantment;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class EnchantmentMenuProcess {

    private final EnchantmentMenu menu;

    private final Int2ObjectOpenHashMap<List<EnchantmentInstance>> enchantments = new Int2ObjectOpenHashMap();

    private boolean freezeBroadcast = false;

    private ItemStack currentItem = ItemStack.EMPTY;

    private Boolean itemIsEnchantable = null;

    private EnchantmentState state = EnchantmentState.IDLE;

    private Player player;

    public EnchantmentMenuProcess(EnchantmentMenu menu) {
        this.menu = menu;
    }

    public boolean isFreezeBroadcast() {
        return this.freezeBroadcast;
    }

    public void setFreezeBroadcast(boolean freezeBroadcast) {
        this.freezeBroadcast = freezeBroadcast;
    }

    public boolean matchesCurrentItem(ItemStack item) {
        return !this.currentItem.isEmpty() && ItemStack.matches(this.currentItem, item);
    }

    public void setCurrentItem(ItemStack currentItem) {
        this.currentItem = currentItem;
        if (currentItem.isEmpty()) {
            this.itemIsEnchantable = null;
        }
    }

    public void clearEnchantments() {
        this.enchantments.clear();
    }

    public boolean storeItemIsEnchantable(@Nullable Boolean override, ItemStack item) {
        this.itemIsEnchantable = override != null ? override : item.isEnchantable();
        return this.itemIsEnchantable;
    }

    public boolean isItemEnchantable(ItemStack item) {
        return this.itemIsEnchantable == null ? item.isEnchantable() : this.itemIsEnchantable;
    }

    public void setEnchantments(int index, List<EnchantmentInstance> enchantments) {
        this.enchantments.put(index, new ArrayList(enchantments));
    }

    private String formatEnchantments(List<EnchantmentInstance> enchantments) {
        return (String) enchantments.stream().map(i -> i.enchantment.getFullname(i.level).toString()).collect(Collectors.joining(","));
    }

    public List<EnchantmentInstance> getEnchantments(int index) {
        return (List<EnchantmentInstance>) this.enchantments.computeIfAbsent(index, $ -> new ArrayList());
    }

    public EnchantmentState getState() {
        return this.state;
    }

    public void setState(EnchantmentState storeEnchantments) {
        this.state = storeEnchantments;
    }

    public EnchantmentMenu getMenu() {
        return this.menu;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void prepareEvent(ItemStack item) {
        this.setCurrentItem(item);
        this.clearEnchantments();
        this.setFreezeBroadcast(true);
        this.setState(EnchantmentState.STORE_ENCHANTMENTS);
    }

    public void abortEvent(ItemStack item) {
        this.setCurrentItem(item);
        this.clearEnchantments();
        this.setState(EnchantmentState.IDLE);
    }

    public ItemStack getCurrentItem() {
        return this.currentItem;
    }
}