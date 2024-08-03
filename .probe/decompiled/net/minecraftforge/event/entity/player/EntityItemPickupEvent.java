package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event.HasResult;

@Cancelable
@HasResult
public class EntityItemPickupEvent extends PlayerEvent {

    private final ItemEntity item;

    public EntityItemPickupEvent(Player player, ItemEntity item) {
        super(player);
        this.item = item;
    }

    public ItemEntity getItem() {
        return this.item;
    }
}