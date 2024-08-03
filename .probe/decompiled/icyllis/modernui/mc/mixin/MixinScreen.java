package icyllis.modernui.mc.mixin;

import icyllis.modernui.mc.BlurHandler;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Screen.class })
public class MixinScreen {

    @Redirect(method = { "renderBackground" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(IIIIII)V"))
    private void renderBackgroundInWorld(@Nonnull GuiGraphics gr, int x1, int y1, int x2, int y2, int color1, int color2) {
        BlurHandler.INSTANCE.drawScreenBackground(gr, x1, y1, x2, y2);
    }
}