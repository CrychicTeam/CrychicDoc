package org.violetmoon.zeta.client.config.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.ValueDefinition;

public abstract class AbstractEditBoxInputScreen<T> extends AbstractInputScreen<T> {

    protected EditBox input;

    protected int VALID_COLOR = 14737632;

    protected int INVALID_COLOR = 14496546;

    public AbstractEditBoxInputScreen(ZetaClient zc, Screen parent, ChangeSet changes, ValueDefinition<T> valueDef) {
        super(zc, parent, changes, valueDef);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.f_96547_, Component.literal(this.def.getTranslatedDisplayName(x$0 -> I18n.get(x$0))).withStyle(ChatFormatting.BOLD), this.f_96543_ / 2, 20, 16777215);
        guiGraphics.drawCenteredString(this.f_96547_, I18n.get("quark.gui.config.defaultvalue", this.def.defaultValue), this.f_96543_ / 2, 30, 16777215);
        this.input.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void init() {
        super.init();
        this.input = new EditBox(this.f_96547_, this.f_96543_ / 2 - 100, 60, 200, 20, Component.literal(""));
        this.input.setMaxLength(this.maxStringLength());
        this.input.setResponder(this::onEdit);
        this.forceUpdateWidgetsTo(this.get());
        this.m_264313_(this.input);
        this.m_7787_(this.input);
    }

    protected void onEdit(String newString) {
        T parsed = this.fromString(newString);
        if (parsed != null && this.def.validate(parsed) && newString.length() < this.maxStringLength()) {
            this.set(parsed);
            this.input.setTextColor(this.VALID_COLOR);
            this.updateButtonStatus(true);
        } else {
            this.input.setTextColor(this.INVALID_COLOR);
            this.updateButtonStatus(false);
        }
    }

    @Override
    protected void forceUpdateWidgetsTo(T value) {
        String asString = this.toString(value);
        T roundtrip = this.fromString(asString);
        if (roundtrip == null) {
            this.input.setValue(this.toString(this.def.defaultValue));
        } else {
            this.input.setValue(asString);
        }
        this.m_264313_(this.input);
    }

    protected String toString(T thing) {
        return thing.toString();
    }

    protected int maxStringLength() {
        return 256;
    }

    @Nullable
    protected abstract T fromString(String var1);
}