package dev.architectury.hooks.client.screen;

import dev.architectury.hooks.client.screen.forge.ScreenHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ScreenHooks {

    private ScreenHooks() {
    }

    @ExpectPlatform
    @Transformed
    public static List<NarratableEntry> getNarratables(Screen screen) {
        return ScreenHooksImpl.getNarratables(screen);
    }

    @ExpectPlatform
    @Transformed
    public static List<Renderable> getRenderables(Screen screen) {
        return ScreenHooksImpl.getRenderables(screen);
    }

    @ExpectPlatform
    @Transformed
    public static <T extends AbstractWidget & Renderable & NarratableEntry> T addRenderableWidget(Screen screen, T widget) {
        return ScreenHooksImpl.addRenderableWidget(screen, widget);
    }

    @ExpectPlatform
    @Transformed
    public static <T extends Renderable> T addRenderableOnly(Screen screen, T listener) {
        return ScreenHooksImpl.addRenderableOnly(screen, listener);
    }

    @ExpectPlatform
    @Transformed
    public static <T extends GuiEventListener & NarratableEntry> T addWidget(Screen screen, T listener) {
        return ScreenHooksImpl.addWidget(screen, listener);
    }
}