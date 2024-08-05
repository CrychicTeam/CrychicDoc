package com.mna.gui.widgets;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class AttributeButton extends ImageButton {

    private List<MutableComponent> tooltips;

    private final Consumer<Component> addTooltipLineCallback;

    public AttributeButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int p_i51135_9_, int p_i51135_10_, Button.OnPress onPressIn, String[] tooltips, Consumer<Component> addTooltipLineCallback) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, p_i51135_9_, p_i51135_10_, onPressIn);
        this.tooltips = (List<MutableComponent>) Arrays.asList(tooltips).stream().map(s -> Component.translatable(s)).collect(Collectors.toList());
        this.addTooltipLineCallback = addTooltipLineCallback;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (this.m_198029_() && this.addTooltipLineCallback != null) {
            if (this.tooltips.size() > 0) {
                this.addTooltipLineCallback.accept(((MutableComponent) this.tooltips.get(0)).withStyle(ChatFormatting.AQUA));
            }
            if (this.tooltips.size() > 1) {
                this.tooltips.stream().skip(1L).forEach(tooltip -> this.addTooltipLineCallback.accept(tooltip.withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY)));
            }
        }
    }
}