package net.minecraftforge.event.entity.item;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class ItemTossEvent extends ItemEvent {

    private final Player player;

    public ItemTossEvent(ItemEntity entityItem, Player player) {
        super(entityItem);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
}