package de.keksuccino.konkrete.gui.screens.popup;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.ExtendedEditBox;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.KeyboardData;
import de.keksuccino.konkrete.input.KeyboardHandler;
import de.keksuccino.konkrete.localization.Locals;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TextInputPopup extends Popup {

    protected Consumer<String> callback;

    protected String input = null;

    protected ExtendedEditBox textField;

    protected AdvancedButton doneButton;

    protected Color color;

    protected int width = 250;

    protected String title = "";

    public TextInputPopup(Color color, String title, CharacterFilter filter, int alpha) {
        super(alpha);
        this.init(color, title, filter, null);
    }

    public TextInputPopup(Color color, String title, CharacterFilter filter, int backgroundAlpha, Consumer<String> callback) {
        super(backgroundAlpha);
        this.init(color, title, filter, callback);
    }

    protected void init(Color color, String title, CharacterFilter filter, Consumer<String> callback) {
        this.textField = new ExtendedEditBox(Minecraft.getInstance().font, 0, 0, 200, 20, Component.empty(), true);
        this.textField.setCharacterFilter(filter);
        this.textField.m_94190_(true);
        this.textField.m_93692_(false);
        this.textField.m_94199_(1000);
        this.doneButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.done"), true, press -> {
            this.input = this.textField.m_94155_();
            this.setDisplayed(false);
            if (this.callback != null) {
                this.callback.accept(this.input);
            }
        });
        this.addButton(this.doneButton);
        if (title != null) {
            this.title = title;
        }
        this.color = color;
        this.callback = callback;
        KeyboardHandler.addKeyPressedListener(this::onEnterPressed);
        KeyboardHandler.addKeyPressedListener(this::onEscapePressed);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, Screen renderIn) {
        super.render(graphics, mouseX, mouseY, renderIn);
        if (this.isDisplayed()) {
            int height = 100;
            RenderSystem.enableBlend();
            graphics.fill(renderIn.width / 2 - this.width / 2, renderIn.height / 2 - height / 2, renderIn.width / 2 + this.width / 2, renderIn.height / 2 + height / 2, this.color.getRGB());
            RenderSystem.disableBlend();
            graphics.drawCenteredString(Minecraft.getInstance().font, Component.literal(this.title), renderIn.width / 2, renderIn.height / 2 - height / 2 + 10, Color.WHITE.getRGB());
            this.textField.m_252865_(renderIn.width / 2 - this.textField.m_5711_() / 2);
            this.textField.m_253211_(renderIn.height / 2 - this.textField.m_93694_() / 2);
            this.textField.m_87963_(graphics, mouseX, mouseY, Minecraft.getInstance().getFrameTime());
            this.doneButton.setX(renderIn.width / 2 - this.doneButton.getWidth() / 2);
            this.doneButton.setY(renderIn.height / 2 + height / 2 - this.doneButton.m_93694_() - 5);
            this.renderButtons(graphics, mouseX, mouseY);
        }
    }

    public void setText(String text) {
        this.textField.m_94144_("");
        this.textField.insertText(text);
    }

    public String getInput() {
        return this.input;
    }

    public void onEnterPressed(KeyboardData d) {
        if (d.keycode == 257 && this.isDisplayed()) {
            this.input = this.textField.m_94155_();
            this.setDisplayed(false);
            if (this.callback != null) {
                this.callback.accept(this.input);
            }
        }
    }

    public void onEscapePressed(KeyboardData d) {
        if (d.keycode == 256 && this.isDisplayed()) {
            this.setDisplayed(false);
            if (this.callback != null) {
                this.callback.accept(null);
            }
        }
    }
}