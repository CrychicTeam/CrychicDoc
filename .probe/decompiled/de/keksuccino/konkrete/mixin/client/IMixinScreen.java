package de.keksuccino.konkrete.mixin.client;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ Screen.class })
public interface IMixinScreen {

    @Accessor("font")
    void setFontKonkrete(Font var1);

    @Accessor("renderables")
    List<Renderable> getRenderablesKonkrete();

    @Accessor("children")
    List<GuiEventListener> getChildrenKonkrete();
}