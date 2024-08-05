package net.minecraft.client.gui.screens;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ConfirmScreen extends Screen {

    private static final int MARGIN = 20;

    private final Component message;

    private MultiLineLabel multilineMessage = MultiLineLabel.EMPTY;

    protected Component yesButton;

    protected Component noButton;

    private int delayTicker;

    protected final BooleanConsumer callback;

    private final List<Button> exitButtons = Lists.newArrayList();

    public ConfirmScreen(BooleanConsumer booleanConsumer0, Component component1, Component component2) {
        this(booleanConsumer0, component1, component2, CommonComponents.GUI_YES, CommonComponents.GUI_NO);
    }

    public ConfirmScreen(BooleanConsumer booleanConsumer0, Component component1, Component component2, Component component3, Component component4) {
        super(component1);
        this.callback = booleanConsumer0;
        this.message = component2;
        this.yesButton = component3;
        this.noButton = component4;
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), this.message);
    }

    @Override
    protected void init() {
        super.init();
        this.multilineMessage = MultiLineLabel.create(this.f_96547_, this.message, this.f_96543_ - 50);
        int $$0 = Mth.clamp(this.messageTop() + this.messageHeight() + 20, this.f_96544_ / 6 + 96, this.f_96544_ - 24);
        this.exitButtons.clear();
        this.addButtons($$0);
    }

    protected void addButtons(int int0) {
        this.addExitButton(Button.builder(this.yesButton, p_169259_ -> this.callback.accept(true)).bounds(this.f_96543_ / 2 - 155, int0, 150, 20).build());
        this.addExitButton(Button.builder(this.noButton, p_169257_ -> this.callback.accept(false)).bounds(this.f_96543_ / 2 - 155 + 160, int0, 150, 20).build());
    }

    protected void addExitButton(Button button0) {
        this.exitButtons.add((Button) this.m_142416_(button0));
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, this.titleTop(), 16777215);
        this.multilineMessage.renderCentered(guiGraphics0, this.f_96543_ / 2, this.messageTop());
        super.render(guiGraphics0, int1, int2, float3);
    }

    private int titleTop() {
        int $$0 = (this.f_96544_ - this.messageHeight()) / 2;
        return Mth.clamp($$0 - 20 - 9, 10, 80);
    }

    private int messageTop() {
        return this.titleTop() + 20;
    }

    private int messageHeight() {
        return this.multilineMessage.getLineCount() * 9;
    }

    public void setDelay(int int0) {
        this.delayTicker = int0;
        for (Button $$1 : this.exitButtons) {
            $$1.f_93623_ = false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (--this.delayTicker == 0) {
            for (Button $$0 : this.exitButtons) {
                $$0.f_93623_ = true;
            }
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.callback.accept(false);
            return true;
        } else {
            return super.keyPressed(int0, int1, int2);
        }
    }
}