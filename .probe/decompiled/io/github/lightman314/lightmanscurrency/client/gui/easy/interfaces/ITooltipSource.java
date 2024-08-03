package io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import java.util.List;
import net.minecraft.network.chat.Component;

public interface ITooltipSource {

    List<Component> getTooltipText(int var1, int var2);

    default void renderTooltip(EasyGuiGraphics gui) {
        List<Component> tooltips = this.getTooltipText(gui.mousePos.x, gui.mousePos.y);
        if (tooltips != null && !tooltips.isEmpty()) {
            gui.renderComponentTooltip(tooltips);
        }
    }
}