package de.keksuccino.konkrete.gui.content;

import de.keksuccino.konkrete.gui.content.handling.AdvancedWidgetsHandler;
import de.keksuccino.konkrete.gui.content.handling.IAdvancedWidgetBase;
import de.keksuccino.konkrete.input.CharData;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.KeyboardData;
import de.keksuccino.konkrete.mixin.client.IMixinScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtendedEditBox extends EditBox implements IAdvancedWidgetBase {

    protected boolean handleSelf = false;

    protected CharacterFilter characterFilter;

    public ExtendedEditBox(Font font, int x, int y, int width, int height, Component hint, boolean handleSelf) {
        super(font, x, y, width, height, hint);
        this.handleSelf = handleSelf;
    }

    public ExtendedEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox editBox, Component hint, boolean handleSelf) {
        super(font, x, y, width, height, editBox, hint);
        this.handleSelf = handleSelf;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.handleSelf) {
            AdvancedWidgetsHandler.handleWidget(this);
            Screen s = Minecraft.getInstance().screen;
            if (s != null && !((IMixinScreen) s).getChildrenKonkrete().contains(this)) {
                ((IMixinScreen) s).getChildrenKonkrete().add(this);
            }
        }
        super.m_88315_(graphics, mouseX, mouseY, partial);
    }

    @Nullable
    public CharacterFilter getCharacterFilter() {
        return this.characterFilter;
    }

    public void setCharacterFilter(@Nullable CharacterFilter characterFilter) {
        this.characterFilter = characterFilter;
    }

    @Override
    public void onTick() {
        this.m_94120_();
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        return !this.handleSelf ? super.keyPressed(int0, int1, int2) : false;
    }

    @Override
    public void onKeyPress(KeyboardData d) {
        if (this.handleSelf) {
            super.keyPressed(d.keycode, d.scancode, d.modfiers);
        }
    }

    @Override
    public void onKeyReleased(KeyboardData d) {
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        return this.handleSelf || this.characterFilter != null && !this.characterFilter.isAllowed(character) ? false : super.charTyped(character, modifiers);
    }

    @Override
    public void insertText(String textToWrite) {
        if (this.characterFilter != null) {
            textToWrite = this.characterFilter.filterForAllowedChars(textToWrite);
        }
        super.insertText(textToWrite);
    }

    @Override
    public void onCharTyped(CharData d) {
        if (this.handleSelf && (this.characterFilter == null || this.characterFilter.isAllowed(d.typedChar))) {
            super.charTyped(d.typedChar, d.modfiers);
        }
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
    }
}