package it.hurts.sskirillss.nerb.mixin;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Screen.class })
public class ScreenMixin {

    @Inject(method = { "addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;" }, at = { @At("HEAD") }, cancellable = true)
    public <T extends GuiEventListener & Renderable & NarratableEntry> void onWidgetAdded(T widget, CallbackInfoReturnable<T> cir) {
        if (widget instanceof ImageButton image && image.resourceLocation.equals(new ResourceLocation("textures/gui/recipe_button.png"))) {
            cir.setReturnValue(null);
        }
    }
}