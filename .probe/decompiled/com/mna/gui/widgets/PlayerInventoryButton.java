package com.mna.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

public class PlayerInventoryButton extends ImageButton {

    private final AbstractContainerScreen<?> parentGui;

    private boolean isRecipeBookVisible = false;

    public PlayerInventoryButton(AbstractContainerScreen<?> parentGui, int xIn, int yIn, int widthIn, int heightIn, int textureOffsetX, int textureOffsetY, int yDiffText, ResourceLocation resource, Button.OnPress onPressed, Component text) {
        super(xIn, yIn, widthIn, heightIn, textureOffsetX, textureOffsetY, yDiffText, resource, 128, 128, onPressed, text);
        this.parentGui = parentGui;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.parentGui instanceof InventoryScreen) {
            boolean lastVisible = this.isRecipeBookVisible;
            this.isRecipeBookVisible = ((InventoryScreen) this.parentGui).getRecipeBookComponent().isVisible();
            if (lastVisible != this.isRecipeBookVisible) {
                Tuple<Integer, Integer> offsets = getOffsets(false);
                this.m_264152_(this.parentGui.getGuiLeft() + offsets.getA(), this.parentGui.f_96544_ / 2 + offsets.getB());
            }
        } else if (this.parentGui instanceof CreativeModeInventoryScreen gui) {
            boolean isInventoryTab = gui.isInventoryOpen();
            this.f_93623_ = isInventoryTab;
            if (!isInventoryTab) {
                return;
            }
        }
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }

    public static Tuple<Integer, Integer> getOffsets(boolean isCreative) {
        return isCreative ? new Tuple<>(75, -50) : new Tuple<>(30, -60);
    }
}