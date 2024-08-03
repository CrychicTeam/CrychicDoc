package fuzs.puzzleslib.api.event.v1.entity.player;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public interface ItemTouchCallback {

    EventInvoker<ItemTouchCallback> EVENT = EventInvoker.lookup(ItemTouchCallback.class);

    EventResult onItemTouch(Player var1, ItemEntity var2);
}