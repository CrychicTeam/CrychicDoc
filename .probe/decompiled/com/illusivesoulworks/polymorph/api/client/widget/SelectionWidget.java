package com.illusivesoulworks.polymorph.api.client.widget;

import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public class SelectionWidget implements Renderable, GuiEventListener {

    private final Consumer<ResourceLocation> onSelect;

    private final AbstractContainerScreen<?> containerScreen;

    private final List<OutputWidget> outputWidgets = new ArrayList();

    private int xOffset;

    private int yOffset;

    private OutputWidget hoveredButton;

    private boolean active = false;

    private int x;

    private int y;

    private int lastX;

    private int lastY;

    public SelectionWidget(int x, int y, int xOffset, int yOffset, Consumer<ResourceLocation> onSelect, AbstractContainerScreen<?> containerScreen) {
        this.setPosition(x, y);
        this.onSelect = onSelect;
        this.containerScreen = containerScreen;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.updateButtonPositions();
    }

    public void setOffsets(int x, int y) {
        this.xOffset = x;
        this.yOffset = y;
    }

    public void highlightButton(ResourceLocation resourceLocation) {
        this.outputWidgets.forEach(widget -> widget.setHighlighted(widget.getResourceLocation().equals(resourceLocation)));
    }

    private void updateButtonPositions() {
        int size = this.outputWidgets.size();
        int xOffset = (int) (-25.0 * Math.floor((double) ((float) size / 2.0F)));
        if (size % 2 == 0) {
            xOffset += 13;
        }
        int[] pos = new int[] { this.x + xOffset, this.y };
        this.outputWidgets.forEach(widget -> {
            widget.m_264152_(pos[0], pos[1]);
            pos[0] += 25;
        });
    }

    public List<OutputWidget> getOutputWidgets() {
        return this.outputWidgets;
    }

    public void setRecipeList(Set<IRecipePair> recipeList) {
        this.outputWidgets.clear();
        recipeList.forEach(data -> {
            if (!data.getOutput().isEmpty()) {
                this.outputWidgets.add(new OutputWidget(data));
            }
        });
        this.updateButtonPositions();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }

    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null && this.hoveredButton != null) {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 501.0F);
            guiGraphics.renderTooltip(mc.font, this.hoveredButton.getOutput(), mouseX, mouseY);
            poseStack.popPose();
        }
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isActive()) {
            int x = Services.CLIENT_PLATFORM.getScreenLeft(this.containerScreen) + this.xOffset;
            int y = Services.CLIENT_PLATFORM.getScreenTop(this.containerScreen) + this.yOffset;
            if (this.lastX != x || this.lastY != y) {
                this.setPosition(x, y);
                this.lastX = x;
                this.lastY = y;
            }
            this.hoveredButton = null;
            this.outputWidgets.forEach(button -> {
                button.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
                if (button.f_93624_ && button.m_198029_()) {
                    this.hoveredButton = button;
                }
            });
            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isActive()) {
            for (OutputWidget widget : this.outputWidgets) {
                if (widget.m_6375_(mouseX, mouseY, button)) {
                    this.onSelect.accept(widget.getResourceLocation());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setFocused(boolean var1) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }
}