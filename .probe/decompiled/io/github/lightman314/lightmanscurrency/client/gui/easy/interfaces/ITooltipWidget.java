package io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces;

import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public interface ITooltipWidget extends ITooltipSource {

    List<Component> getTooltipText();

    @Override
    default List<Component> getTooltipText(int mouseX, int mouseY) {
        if (this instanceof AbstractWidget widget && widget.isMouseOver((double) mouseX, (double) mouseY)) {
            return this.getTooltipText();
        }
        return null;
    }
}