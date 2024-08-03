package io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab;

import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public interface ITab {

    @Nonnull
    IconData getIcon();

    int getColor();

    @Nullable
    Component getTooltip();
}