package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.trader;

import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.traders.menu.customer.ITraderMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.customer.ITraderScreen;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class TraderClientTab extends EasyTab {

    protected final ITraderScreen screen;

    protected final ITraderMenu menu;

    protected final Font font;

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.BLANK;
    }

    @Override
    public final int getColor() {
        return 16777215;
    }

    @Nullable
    @Override
    public final Component getTooltip() {
        return EasyText.empty();
    }

    protected TraderClientTab(@Nonnull ITraderScreen screen) {
        super(screen);
        this.screen = screen;
        this.menu = this.screen.getMenu();
        this.font = this.screen.getFont();
    }
}