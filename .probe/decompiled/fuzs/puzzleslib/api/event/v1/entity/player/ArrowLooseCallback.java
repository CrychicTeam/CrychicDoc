package fuzs.puzzleslib.api.event.v1.entity.player;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface ArrowLooseCallback {

    EventInvoker<ArrowLooseCallback> EVENT = EventInvoker.lookup(ArrowLooseCallback.class);

    EventResult onArrowLoose(Player var1, ItemStack var2, Level var3, MutableInt var4, boolean var5);
}