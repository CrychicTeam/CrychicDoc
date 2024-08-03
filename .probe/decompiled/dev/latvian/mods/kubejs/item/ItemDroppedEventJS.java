package dev.latvian.mods.kubejs.item;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Info("Invoked when a player drops an item.\n")
public class ItemDroppedEventJS extends PlayerEventJS {

    private final Player player;

    private final ItemEntity entity;

    public ItemDroppedEventJS(Player player, ItemEntity entity) {
        this.player = player;
        this.entity = entity;
    }

    @Info("The player that dropped the item.")
    @Override
    public Player getEntity() {
        return this.player;
    }

    @Info("The item entity that was spawned when dropping.")
    public ItemEntity getItemEntity() {
        return this.entity;
    }

    @Info("The item that was dropped.")
    public ItemStack getItem() {
        return this.entity.getItem();
    }
}