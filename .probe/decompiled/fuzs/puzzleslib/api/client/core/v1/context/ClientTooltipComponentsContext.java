package fuzs.puzzleslib.api.client.core.v1.context;

import java.util.function.Function;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

@FunctionalInterface
public interface ClientTooltipComponentsContext {

    <T extends TooltipComponent> void registerClientTooltipComponent(Class<T> var1, Function<? super T, ? extends ClientTooltipComponent> var2);
}