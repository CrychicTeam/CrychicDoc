package de.keksuccino.konkrete.gui.screens.popup;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.input.KeyboardData;
import de.keksuccino.konkrete.input.KeyboardHandler;
import de.keksuccino.konkrete.localization.Locals;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class YesNoPopup extends Popup {

    private List<String> text;

    private AdvancedButton confirmButton;

    private AdvancedButton cancelButton;

    private int width;

    private Color color = new Color(76, 0, 128);

    private Consumer<Boolean> callback;

    public YesNoPopup(int width, @Nullable Color color, int backgroundAlpha, @Nullable Consumer<Boolean> callback, @Nonnull String... text) {
        super(backgroundAlpha);
        this.setNotificationText(text);
        this.width = width;
        this.confirmButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.yesno.confirm"), true, press -> {
            this.setDisplayed(false);
            if (this.callback != null) {
                this.callback.accept(true);
            }
        });
        this.addButton(this.confirmButton);
        this.cancelButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.yesno.cancel"), true, press -> {
            this.setDisplayed(false);
            if (this.callback != null) {
                this.callback.accept(false);
            }
        });
        this.addButton(this.cancelButton);
        if (color != null) {
            this.color = color;
        }
        this.callback = callback;
        KeyboardHandler.addKeyPressedListener(this::onEnterPressed);
        KeyboardHandler.addKeyPressedListener(this::onEscapePressed);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, Screen renderIn) {
        super.render(graphics, mouseX, mouseY, renderIn);
        if (this.isDisplayed()) {
            int height = 50;
            for (int i = 0; i < this.text.size(); i++) {
                height += 10;
            }
            RenderSystem.enableBlend();
            graphics.fill(renderIn.width / 2 - this.width / 2, renderIn.height / 2 - height / 2, renderIn.width / 2 + this.width / 2, renderIn.height / 2 + height / 2, this.color.getRGB());
            RenderSystem.disableBlend();
            int i = 0;
            for (String s : this.text) {
                graphics.drawCenteredString(Minecraft.getInstance().font, Component.literal(s), renderIn.width / 2, renderIn.height / 2 - height / 2 + 10 + i, Color.WHITE.getRGB());
                i += 10;
            }
            this.confirmButton.setX(renderIn.width / 2 - this.confirmButton.getWidth() - 20);
            this.confirmButton.setY(renderIn.height / 2 + height / 2 - this.confirmButton.m_93694_() - 5);
            this.cancelButton.setX(renderIn.width / 2 + 20);
            this.cancelButton.setY(renderIn.height / 2 + height / 2 - this.cancelButton.m_93694_() - 5);
            this.renderButtons(graphics, mouseX, mouseY);
        }
    }

    public void setNotificationText(String... text) {
        if (text != null) {
            List<String> l = new ArrayList();
            for (String s : text) {
                if (s.contains("%n%")) {
                    for (String s2 : s.split("%n%")) {
                        l.add(s2);
                    }
                } else {
                    l.add(s);
                }
            }
            this.text = l;
        }
    }

    public void onEnterPressed(KeyboardData d) {
        if (d.keycode == 257 && this.isDisplayed()) {
            this.setDisplayed(false);
            if (this.callback != null) {
                this.callback.accept(true);
            }
        }
    }

    public void onEscapePressed(KeyboardData d) {
        if (d.keycode == 256 && this.isDisplayed()) {
            this.setDisplayed(false);
            if (this.callback != null) {
                this.callback.accept(false);
            }
        }
    }
}