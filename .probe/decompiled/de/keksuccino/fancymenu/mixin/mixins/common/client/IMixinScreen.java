package de.keksuccino.fancymenu.mixin.mixins.common.client;

import java.util.List;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ Screen.class })
public interface IMixinScreen {

    @Accessor("children")
    List<GuiEventListener> getChildrenFancyMenu();

    @Accessor("renderables")
    List<Renderable> getRenderablesFancyMenu();

    @Accessor("narratables")
    List<NarratableEntry> getNarratablesFancyMenu();

    @Invoker("removeWidget")
    void invokeRemoveWidgetFancyMenu(GuiEventListener var1);

    @Accessor("initialized")
    boolean get_initialized_FancyMenu();
}