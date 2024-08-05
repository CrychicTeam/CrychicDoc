package net.minecraftforge.client.gui;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.function.Function;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.fml.ModLoader;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class ClientTooltipComponentManager {

    private static ImmutableMap<Class<? extends TooltipComponent>, Function<TooltipComponent, ClientTooltipComponent>> FACTORIES;

    @Nullable
    public static ClientTooltipComponent createClientTooltipComponent(TooltipComponent component) {
        Function<TooltipComponent, ClientTooltipComponent> factory = (Function<TooltipComponent, ClientTooltipComponent>) FACTORIES.get(component.getClass());
        return factory != null ? (ClientTooltipComponent) factory.apply(component) : null;
    }

    @Internal
    public static void init() {
        HashMap<Class<? extends TooltipComponent>, Function<TooltipComponent, ClientTooltipComponent>> factories = new HashMap();
        RegisterClientTooltipComponentFactoriesEvent event = new RegisterClientTooltipComponentFactoriesEvent(factories);
        ModLoader.get().postEventWrapContainerInModOrder(event);
        FACTORIES = ImmutableMap.copyOf(factories);
    }

    private ClientTooltipComponentManager() {
    }
}