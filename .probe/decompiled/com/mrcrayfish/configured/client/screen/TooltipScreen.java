package com.mrcrayfish.configured.client.screen;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public abstract class TooltipScreen extends Screen {

    private static final List<Component> DUMMY_TOOLTIP = ImmutableList.of(Component.empty());

    @Nullable
    public List<FormattedCharSequence> tooltipText;

    @Nullable
    public Integer tooltipOutlineColour;

    protected TooltipScreen(Component title) {
        super(title);
    }

    protected void resetTooltip() {
        this.tooltipText = null;
        this.tooltipOutlineColour = null;
    }

    public void setActiveTooltip(@Nullable List<FormattedCharSequence> tooltip) {
        this.resetTooltip();
        this.tooltipText = tooltip;
    }

    public void setActiveTooltip(Component text) {
        this.resetTooltip();
        this.tooltipText = this.f_96541_.font.split(text, 200);
    }

    public void setActiveTooltip(Component text, int outlineColour) {
        this.resetTooltip();
        this.tooltipText = this.f_96541_.font.split(text, 200);
        this.tooltipOutlineColour = outlineColour;
    }

    protected void drawTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.tooltipText != null) {
            this.m_257959_(this.tooltipText);
        }
    }

    public static record ListMenuTooltipComponent(FormattedCharSequence text) implements TooltipComponent {

        public ClientTextTooltip asClientTextTooltip() {
            return new ClientTextTooltip(this.text);
        }
    }
}