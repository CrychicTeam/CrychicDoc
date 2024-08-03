package com.mna.gui.widgets;

import com.mna.ManaAndArtifice;
import com.mna.api.sound.SFX;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ImageItemStackButton extends ImageButton {

    final ItemStack iconStack;

    final boolean includeTooltip;

    final Consumer<List<Component>> toolTipSetter;

    int stackXOffset = 3;

    int stackYOffset = 1;

    boolean renderBackground = true;

    public ImageItemStackButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int hoverOffset, ResourceLocation textureFile, int texWidth, int texHeight, Button.OnPress clickHandler, Consumer<List<Component>> toolTipSetter, ItemStack displayStack) {
        this(x, y, width, height, xTexStart, yTexStart, hoverOffset, textureFile, texWidth, texHeight, clickHandler, toolTipSetter, displayStack, true);
    }

    public ImageItemStackButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int hoverOffset, ResourceLocation textureFile, int texWidth, int texHeight, Button.OnPress clickHandler, Consumer<List<Component>> toolTipSetter, ItemStack displayStack, boolean displayFullTooltip) {
        super(x, y, width, height, xTexStart, yTexStart, hoverOffset, textureFile, texWidth, texHeight, clickHandler, displayStack.getHoverName());
        this.iconStack = displayStack;
        this.includeTooltip = displayFullTooltip;
        this.toolTipSetter = toolTipSetter;
        this.stackXOffset = width == 18 ? 1 : 3;
        this.stackYOffset = width == 18 ? 3 : 1;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        if (this.renderBackground) {
            super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
        pGuiGraphics.renderItem(this.iconStack, this.m_252754_() + this.stackXOffset, this.m_252907_() + this.stackYOffset);
        if (this.f_93623_ && this.f_93622_) {
            List<Component> lines;
            if (this.includeTooltip) {
                lines = this.iconStack.getTooltipLines(ManaAndArtifice.instance.proxy.getClientPlayer(), TooltipFlag.Default.f_256752_);
            } else {
                lines = new ArrayList();
                lines.add(this.iconStack.getHoverName());
            }
            List<Component> tt = new ArrayList();
            for (Component comp : lines) {
                if (!this.includeTooltip) {
                    String unformatted = ChatFormatting.stripFormatting(comp.getString());
                    MutableComponent stc = Component.literal(unformatted);
                    stc.withStyle(ChatFormatting.ITALIC);
                    tt.add(stc);
                } else {
                    tt.add(comp);
                }
            }
            if (this.toolTipSetter != null) {
                this.toolTipSetter.accept(tt);
            }
        }
    }

    @Override
    public void playDownSound(SoundManager soundHandler) {
        soundHandler.play(SimpleSoundInstance.forUI(SFX.Gui.PAGE_FLIP, (float) (0.8 + Math.random() * 0.4)));
    }

    public ImageItemStackButton setRenderBackground(boolean render) {
        this.renderBackground = render;
        return this;
    }

    public ImageItemStackButton setAffinityOffset(int x, int y) {
        this.stackXOffset = x;
        this.stackYOffset = y;
        return this;
    }
}