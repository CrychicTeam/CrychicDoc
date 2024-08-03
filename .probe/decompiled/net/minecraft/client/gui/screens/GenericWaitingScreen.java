package net.minecraft.client.gui.screens;

import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class GenericWaitingScreen extends Screen {

    private static final int TITLE_Y = 80;

    private static final int MESSAGE_Y = 120;

    private static final int MESSAGE_MAX_WIDTH = 360;

    @Nullable
    private final Component messageText;

    private final Component buttonLabel;

    private final Runnable buttonCallback;

    @Nullable
    private MultiLineLabel message;

    private Button button;

    private int disableButtonTicks;

    public static GenericWaitingScreen createWaiting(Component component0, Component component1, Runnable runnable2) {
        return new GenericWaitingScreen(component0, null, component1, runnable2, 0);
    }

    public static GenericWaitingScreen createCompleted(Component component0, Component component1, Component component2, Runnable runnable3) {
        return new GenericWaitingScreen(component0, component1, component2, runnable3, 20);
    }

    protected GenericWaitingScreen(Component component0, @Nullable Component component1, Component component2, Runnable runnable3, int int4) {
        super(component0);
        this.messageText = component1;
        this.buttonLabel = component2;
        this.buttonCallback = runnable3;
        this.disableButtonTicks = int4;
    }

    @Override
    protected void init() {
        super.init();
        if (this.messageText != null) {
            this.message = MultiLineLabel.create(this.f_96547_, this.messageText, 360);
        }
        int $$0 = 150;
        int $$1 = 20;
        int $$2 = this.message != null ? this.message.getLineCount() : 1;
        int $$3 = Math.max($$2, 5) * 9;
        int $$4 = Math.min(120 + $$3, this.f_96544_ - 40);
        this.button = (Button) this.m_142416_(Button.builder(this.buttonLabel, p_239908_ -> this.onClose()).bounds((this.f_96543_ - 150) / 2, $$4, 150, 20).build());
    }

    @Override
    public void tick() {
        if (this.disableButtonTicks > 0) {
            this.disableButtonTicks--;
        }
        this.button.f_93623_ = this.disableButtonTicks == 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 80, 16777215);
        if (this.message == null) {
            String $$4 = LoadingDotsText.get(Util.getMillis());
            guiGraphics0.drawCenteredString(this.f_96547_, $$4, this.f_96543_ / 2, 120, 10526880);
        } else {
            this.message.renderCentered(guiGraphics0, this.f_96543_ / 2, 120);
        }
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.message != null && this.button.f_93623_;
    }

    @Override
    public void onClose() {
        this.buttonCallback.run();
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(this.f_96539_, this.messageText != null ? this.messageText : CommonComponents.EMPTY);
    }
}