package dev.architectury.registry.client.gui;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.client.gui.forge.ClientTooltipComponentRegistryImpl;
import java.util.function.Function;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientTooltipComponentRegistry {

    private ClientTooltipComponentRegistry() {
    }

    @ExpectPlatform
    @Transformed
    public static <T extends TooltipComponent> void register(Class<T> clazz, Function<? super T, ? extends ClientTooltipComponent> factory) {
        ClientTooltipComponentRegistryImpl.register(clazz, factory);
    }
}