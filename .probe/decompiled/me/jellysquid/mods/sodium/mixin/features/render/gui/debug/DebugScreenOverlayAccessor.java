package me.jellysquid.mods.sodium.mixin.features.render.gui.debug;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ DebugScreenOverlay.class })
public interface DebugScreenOverlayAccessor {

    @Invoker("renderLines")
    void invokeRenderLines(GuiGraphics var1, List<String> var2, boolean var3);
}