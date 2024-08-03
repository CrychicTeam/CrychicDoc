package fuzs.puzzleslib.api.event.v1.entity.player;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface ItemTossCallback {

    EventInvoker<ItemTossCallback> EVENT = EventInvoker.lookup(ItemTossCallback.class);

    EventResult onItemToss(ItemEntity var1, Player var2);
}