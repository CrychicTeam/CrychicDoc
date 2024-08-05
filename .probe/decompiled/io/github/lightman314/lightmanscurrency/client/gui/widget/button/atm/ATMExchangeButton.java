package io.github.lightman314.lightmanscurrency.client.gui.widget.button.atm;

import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.coins.atm.data.ATMExchangeButtonData;
import io.github.lightman314.lightmanscurrency.api.money.coins.atm.icons.ATMIconData;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.ATMScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

public class ATMExchangeButton extends EasyButton {

    public static final int HEIGHT = 18;

    public final ATMExchangeButtonData data;

    public boolean selected = false;

    public ATMExchangeButton(ScreenPosition corner, ATMExchangeButtonData data, Consumer<String> commandHandler) {
        super(corner.offset(data.position), data.width, 18, b -> commandHandler.accept(data.command));
        this.data = data;
    }

    public ATMExchangeButton withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        int yOffset = this.f_93622_ != this.selected ? 18 : 0;
        if (this.f_93623_) {
            gui.resetColor();
        } else {
            gui.setColor(0.5F, 0.5F, 0.5F);
        }
        gui.blit(ATMScreen.BUTTON_TEXTURE, 0, 0, 0, yOffset, 2, 18);
        int xPos = 2;
        while (xPos < this.m_5711_() - 2) {
            int xSize = Math.min(this.m_5711_() - 2 - xPos, 252);
            gui.blit(ATMScreen.BUTTON_TEXTURE, xPos, 0, 2, yOffset, xSize, 18);
            xPos += xSize;
        }
        gui.blit(ATMScreen.BUTTON_TEXTURE, this.m_5711_() - 2, 0, 254, yOffset, 2, 18);
        UnmodifiableIterator var8 = this.data.getIcons().iterator();
        while (var8.hasNext()) {
            ATMIconData icon = (ATMIconData) var8.next();
            try {
                icon.render(this, gui, this.f_93622_);
            } catch (Throwable var7) {
                LightmansCurrency.LogError("Error rendering ATM Conversion Button icon.", var7);
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}