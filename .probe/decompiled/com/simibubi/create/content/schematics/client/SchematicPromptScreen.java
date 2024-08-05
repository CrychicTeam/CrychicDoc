package com.simibubi.create.content.schematics.client;

import com.simibubi.create.AllItems;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class SchematicPromptScreen extends AbstractSimiScreen {

    private AllGuiTextures background;

    private final Component convertLabel = Lang.translateDirect("schematicAndQuill.convert");

    private final Component abortLabel = Lang.translateDirect("action.discard");

    private final Component confirmLabel = Lang.translateDirect("action.saveToFile");

    private EditBox nameField;

    private IconButton confirm;

    private IconButton abort;

    private IconButton convert;

    public SchematicPromptScreen() {
        super(Lang.translateDirect("schematicAndQuill.title"));
        this.background = AllGuiTextures.SCHEMATIC_PROMPT;
    }

    @Override
    public void init() {
        this.setWindowSize(this.background.width, this.background.height);
        super.init();
        int x = this.guiLeft;
        int y = this.guiTop;
        this.nameField = new EditBox(this.f_96547_, x + 49, y + 26, 131, 10, Components.immutableEmpty());
        this.nameField.setTextColor(-1);
        this.nameField.setTextColorUneditable(-1);
        this.nameField.setBordered(false);
        this.nameField.setMaxLength(35);
        this.nameField.setFocused(true);
        this.m_7522_(this.nameField);
        this.m_142416_(this.nameField);
        this.abort = new IconButton(x + 7, y + 53, AllIcons.I_TRASH);
        this.abort.withCallback(() -> {
            CreateClient.SCHEMATIC_AND_QUILL_HANDLER.discard();
            this.m_7379_();
        });
        this.abort.setToolTip(this.abortLabel);
        this.m_142416_(this.abort);
        this.confirm = new IconButton(x + 158, y + 53, AllIcons.I_CONFIRM);
        this.confirm.withCallback(() -> this.confirm(false));
        this.confirm.setToolTip(this.confirmLabel);
        this.m_142416_(this.confirm);
        this.convert = new IconButton(x + 180, y + 53, AllIcons.I_SCHEMATIC);
        this.convert.withCallback(() -> this.confirm(true));
        this.convert.setToolTip(this.convertLabel);
        this.m_142416_(this.convert);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.guiLeft;
        int y = this.guiTop;
        this.background.render(graphics, x, y);
        graphics.drawCenteredString(this.f_96547_, this.f_96539_, x + (this.background.width - 8) / 2, y + 3, 16777215);
        GuiGameElement.of(AllItems.SCHEMATIC.asStack()).<RenderElement>at((float) (x + 22), (float) (y + 23), 0.0F).render(graphics);
        GuiGameElement.of(AllItems.SCHEMATIC_AND_QUILL.asStack()).scale(3.0).<RenderElement>at((float) (x + this.background.width + 6), (float) (y + this.background.height - 40), -200.0F).render(graphics);
    }

    @Override
    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (keyCode == 257) {
            this.confirm(false);
            return true;
        } else if (keyCode == 256 && this.m_6913_()) {
            this.m_7379_();
            return true;
        } else {
            return this.nameField.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
        }
    }

    private void confirm(boolean convertImmediately) {
        CreateClient.SCHEMATIC_AND_QUILL_HANDLER.saveSchematic(this.nameField.getValue(), convertImmediately);
        this.m_7379_();
    }
}