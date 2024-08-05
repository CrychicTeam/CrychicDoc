package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ItemTooltipCallback {

    EventInvoker<ItemTooltipCallback> EVENT = EventInvoker.lookup(ItemTooltipCallback.class);

    void onItemTooltip(ItemStack var1, @Nullable Player var2, List<Component> var3, TooltipFlag var4);
}