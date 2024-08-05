package de.keksuccino.konkrete.gui.content;

import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.mixin.client.IMixinEditBox;
import javax.annotation.Nullable;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

@Deprecated
public class AdvancedTextField extends ExtendedEditBox {

    @Deprecated
    public AdvancedTextField(Font font, int x, int y, int width, int height, boolean handleSelf, @Nullable CharacterFilter characterFilter) {
        super(font, x, y, width, height, Component.literal(""), handleSelf);
        this.handleSelf = handleSelf;
        this.characterFilter = characterFilter;
    }

    @Override
    public boolean isEditable() {
        return this.getAccessor().getIsEditableKonkrete();
    }

    public boolean isLeftClicked() {
        return this.isHoveredOrFocused() && MouseInput.isLeftMouseDown();
    }

    public IMixinEditBox getAccessor() {
        return (IMixinEditBox) this;
    }

    @Deprecated
    @Override
    public boolean isHoveredOrFocused() {
        return this.m_274382_();
    }

    @Deprecated
    protected void setResponderEntryValue(String text) {
        this.getAccessor().onValueChangeKonkrete(text);
    }

    @Deprecated
    public int getMaxStringLength() {
        return this.getAccessor().getMaxLengthKonkrete();
    }

    @Deprecated
    public int getSelectionEnd() {
        return this.getAccessor().getHightlightPosKonkrete();
    }

    @Deprecated
    public boolean isEnabled() {
        return this.isEditable();
    }
}