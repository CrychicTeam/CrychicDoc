package com.simibubi.create.content.redstone.thresholdSwitch;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ThresholdSwitchScreen extends AbstractSimiScreen {

    private ScrollInput offBelow;

    private ScrollInput onAbove;

    private IconButton confirmButton;

    private IconButton flipSignals;

    private final Component invertSignal = Lang.translateDirect("gui.threshold_switch.invert_signal");

    private final ItemStack renderedItem = new ItemStack((ItemLike) AllBlocks.THRESHOLD_SWITCH.get());

    private AllGuiTextures background = AllGuiTextures.STOCKSWITCH;

    private ThresholdSwitchBlockEntity blockEntity;

    private int lastModification;

    private LerpedFloat cursor;

    private LerpedFloat cursorLane;

    public ThresholdSwitchScreen(ThresholdSwitchBlockEntity be) {
        super(Lang.translateDirect("gui.threshold_switch.title"));
        this.blockEntity = be;
        this.lastModification = -1;
    }

    @Override
    protected void init() {
        this.setWindowSize(this.background.width, this.background.height);
        this.setWindowOffset(-20, 0);
        super.init();
        int x = this.guiLeft;
        int y = this.guiTop;
        this.cursor = LerpedFloat.linear().startWithValue((double) this.blockEntity.getLevelForDisplay());
        this.cursorLane = LerpedFloat.linear().startWithValue(this.blockEntity.getState() ? 1.0 : 0.0);
        this.offBelow = new ScrollInput(x + 36, y + 42, 102, 18).withRange(0, 100).titled(Components.empty()).calling(state -> {
            this.lastModification = 0;
            this.offBelow.titled(Lang.translateDirect("gui.threshold_switch.move_to_upper_at", state));
            if (this.onAbove.getState() <= state) {
                this.onAbove.setState(state + 1);
                this.onAbove.onChanged();
            }
        }).setState((int) (this.blockEntity.offWhenBelow * 100.0F));
        this.onAbove = new ScrollInput(x + 36, y + 20, 102, 18).withRange(1, 101).titled(Components.empty()).calling(state -> {
            this.lastModification = 0;
            this.onAbove.titled(Lang.translateDirect("gui.threshold_switch.move_to_lower_at", state));
            if (this.offBelow.getState() >= state) {
                this.offBelow.setState(state - 1);
                this.offBelow.onChanged();
            }
        }).setState((int) (this.blockEntity.onWhenAbove * 100.0F));
        this.onAbove.onChanged();
        this.offBelow.onChanged();
        this.m_142416_(this.onAbove);
        this.m_142416_(this.offBelow);
        this.confirmButton = new IconButton(x + this.background.width - 33, y + this.background.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(() -> this.m_7379_());
        this.m_142416_(this.confirmButton);
        this.flipSignals = new IconButton(x + this.background.width - 62, y + this.background.height - 24, AllIcons.I_FLIP);
        this.flipSignals.withCallback(() -> this.send(!this.blockEntity.isInverted()));
        this.flipSignals.setToolTip(this.invertSignal);
        this.m_142416_(this.flipSignals);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.guiLeft;
        int y = this.guiTop;
        this.background.render(graphics, x, y);
        AllGuiTextures.STOCKSWITCH_POWERED_LANE.render(graphics, x + 37, y + (this.blockEntity.isInverted() ? 20 : 42));
        AllGuiTextures.STOCKSWITCH_UNPOWERED_LANE.render(graphics, x + 37, y + (this.blockEntity.isInverted() ? 42 : 20));
        graphics.drawString(this.f_96547_, this.f_96539_, x + (this.background.width - 8) / 2 - this.f_96547_.width(this.f_96539_) / 2, y + 4, 5841956, false);
        AllGuiTextures sprite = AllGuiTextures.STOCKSWITCH_INTERVAL;
        float lowerBound = (float) this.offBelow.getState();
        float upperBound = (float) this.onAbove.getState();
        sprite.bind();
        graphics.blit(sprite.location, (int) ((float) x + upperBound) + 37, y + 20, (int) ((float) sprite.startX + upperBound), sprite.startY, (int) ((float) sprite.width - upperBound), sprite.height);
        graphics.blit(sprite.location, x + 37, y + 42, sprite.startX, sprite.startY, (int) lowerBound, sprite.height);
        AllGuiTextures.STOCKSWITCH_ARROW_UP.render(graphics, (int) ((float) x + lowerBound + 36.0F) - 2, y + 37);
        AllGuiTextures.STOCKSWITCH_ARROW_DOWN.render(graphics, (int) ((float) x + upperBound + 36.0F) - 3, y + 19);
        if (this.blockEntity.currentLevel != -1.0F) {
            AllGuiTextures cursor = AllGuiTextures.STOCKSWITCH_CURSOR;
            PoseStack ms = graphics.pose();
            ms.pushPose();
            ms.translate(Math.min(99.0F, this.cursor.getValue(partialTicks) * (float) sprite.width), this.cursorLane.getValue(partialTicks) * 22.0F, 0.0F);
            cursor.render(graphics, x + 34, y + 21);
            ms.popPose();
        }
        GuiGameElement.of(this.renderedItem).<GuiGameElement.GuiRenderBuilder>at((float) (x + this.background.width + 6), (float) (y + this.background.height - 56), -200.0F).scale(5.0).render(graphics);
    }

    @Override
    public void tick() {
        super.tick();
        this.cursor.chase((double) this.blockEntity.getLevelForDisplay(), 0.25, LerpedFloat.Chaser.EXP);
        this.cursor.tickChaser();
        this.cursorLane.chase(this.blockEntity.getState() ? 1.0 : 0.0, 0.25, LerpedFloat.Chaser.EXP);
        this.cursorLane.tickChaser();
        if (this.lastModification >= 0) {
            this.lastModification++;
        }
        if (this.lastModification >= 20) {
            this.lastModification = -1;
            this.send(this.blockEntity.isInverted());
        }
    }

    @Override
    public void removed() {
        this.send(this.blockEntity.isInverted());
    }

    protected void send(boolean invert) {
        AllPackets.getChannel().sendToServer(new ConfigureThresholdSwitchPacket(this.blockEntity.m_58899_(), (float) this.offBelow.getState() / 100.0F, (float) this.onAbove.getState() / 100.0F, invert));
    }
}