package io.github.apace100.origins.mixin;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ GuiGraphics.class })
public interface DrawContextAccessor {

    @Invoker("renderTooltipInternal")
    void invokeDrawTooltip(Font var1, List<ClientTooltipComponent> var2, int var3, int var4, ClientTooltipPositioner var5);
}