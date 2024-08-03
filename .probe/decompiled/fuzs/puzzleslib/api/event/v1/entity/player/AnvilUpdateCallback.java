package fuzs.puzzleslib.api.event.v1.entity.player;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface AnvilUpdateCallback {

    EventInvoker<AnvilUpdateCallback> EVENT = EventInvoker.lookup(AnvilUpdateCallback.class);

    EventResult onAnvilUpdate(ItemStack var1, ItemStack var2, MutableValue<ItemStack> var3, String var4, MutableInt var5, MutableInt var6, Player var7);
}