package net.minecraft.client.gui.screens.multiplayer;

import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class WarningScreen extends Screen {

    private final Component content;

    @Nullable
    private final Component check;

    private final Component narration;

    @Nullable
    protected Checkbox stopShowing;

    private MultiLineLabel message = MultiLineLabel.EMPTY;

    protected WarningScreen(Component component0, Component component1, Component component2) {
        this(component0, component1, null, component2);
    }

    protected WarningScreen(Component component0, Component component1, @Nullable Component component2, Component component3) {
        super(component0);
        this.content = component1;
        this.check = component2;
        this.narration = component3;
    }

    protected abstract void initButtons(int var1);

    @Override
    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.f_96547_, this.content, this.f_96543_ - 100);
        int $$0 = (this.message.getLineCount() + 1) * this.getLineHeight();
        if (this.check != null) {
            int $$1 = this.f_96547_.width(this.check);
            this.stopShowing = new Checkbox(this.f_96543_ / 2 - $$1 / 2 - 8, 76 + $$0, $$1 + 24, 20, this.check, false);
            this.m_142416_(this.stopShowing);
        }
        this.initButtons($$0);
    }

    @Override
    public Component getNarrationMessage() {
        return this.narration;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.renderTitle(guiGraphics0);
        int $$4 = this.f_96543_ / 2 - this.message.getWidth() / 2;
        this.message.renderLeftAligned(guiGraphics0, $$4, 70, this.getLineHeight(), 16777215);
        super.render(guiGraphics0, int1, int2, float3);
    }

    protected void renderTitle(GuiGraphics guiGraphics0) {
        guiGraphics0.drawString(this.f_96547_, this.f_96539_, 25, 30, 16777215);
    }

    protected int getLineHeight() {
        return 9 * 2;
    }
}