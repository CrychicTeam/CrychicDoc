package org.violetmoon.zeta.client.event.load;

import java.util.function.Function;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

public interface ZTooltipComponents extends IZetaLoadEvent {

    <T extends TooltipComponent> void register(Class<T> var1, Function<? super T, ? extends ClientTooltipComponent> var2);

    default <T extends ClientTooltipComponent & TooltipComponent> void register(Class<T> type) {
        this.register(type, Function.identity());
    }
}