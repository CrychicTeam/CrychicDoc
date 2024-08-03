package fuzs.puzzleslib.api.event.v1.entity.player;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface AnvilRepairCallback {

    EventInvoker<AnvilRepairCallback> EVENT = EventInvoker.lookup(AnvilRepairCallback.class);

    void onAnvilRepair(Player var1, ItemStack var2, ItemStack var3, ItemStack var4, MutableFloat var5);
}