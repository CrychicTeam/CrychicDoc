package com.simibubi.create.foundation.gui.widget;

import com.simibubi.create.foundation.gui.TickableGuiEventListener;
import com.simibubi.create.foundation.utility.Components;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;

public abstract class AbstractSimiWidget extends AbstractWidget implements TickableGuiEventListener {

    public static final int HEADER_RGB = 5476833;

    public static final int HINT_RGB = 9877472;

    protected float z;

    protected boolean wasHovered = false;

    protected List<Component> toolTip = new LinkedList();

    protected BiConsumer<Integer, Integer> onClick = (_$, _$$) -> {
    };

    public int lockedTooltipX = -1;

    public int lockedTooltipY = -1;

    protected AbstractSimiWidget(int x, int y) {
        this(x, y, 16, 16);
    }

    protected AbstractSimiWidget(int x, int y, int width, int height) {
        this(x, y, width, height, Components.immutableEmpty());
    }

    protected AbstractSimiWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner() {
        return DefaultTooltipPositioner.INSTANCE;
    }

    public <T extends AbstractSimiWidget> T withCallback(BiConsumer<Integer, Integer> cb) {
        this.onClick = cb;
        return (T) this;
    }

    public <T extends AbstractSimiWidget> T withCallback(Runnable cb) {
        return this.withCallback((BiConsumer<Integer, Integer>) ((_$, _$$) -> cb.run()));
    }

    public <T extends AbstractSimiWidget> T atZLevel(float z) {
        this.z = z;
        return (T) this;
    }

    public List<Component> getToolTip() {
        return this.toolTip;
    }

    @Override
    public void tick() {
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.beforeRender(graphics, mouseX, mouseY, partialTicks);
        this.doRender(graphics, mouseX, mouseY, partialTicks);
        this.afterRender(graphics, mouseX, mouseY, partialTicks);
        this.wasHovered = this.m_198029_();
    }

    protected void beforeRender(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().pushPose();
    }

    protected void doRender(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    }

    protected void afterRender(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().popPose();
    }

    public void runCallback(double mouseX, double mouseY) {
        this.onClick.accept((int) mouseX, (int) mouseY);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.runCallback(mouseX, mouseY);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        this.m_168802_(pNarrationElementOutput);
    }
}