package com.simibubi.create.content.equipment.goggles;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class GoggleConfigScreen extends AbstractSimiScreen {

    private int offsetX;

    private int offsetY;

    private final List<Component> tooltip;

    public GoggleConfigScreen() {
        Component componentSpacing = Components.literal("    ");
        this.tooltip = new ArrayList();
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay1")));
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay2").withStyle(ChatFormatting.GRAY)));
        this.tooltip.add(Components.immutableEmpty());
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay3")));
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay4")));
        this.tooltip.add(Components.immutableEmpty());
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay5").withStyle(ChatFormatting.GRAY)));
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay6").withStyle(ChatFormatting.GRAY)));
        this.tooltip.add(Components.immutableEmpty());
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay7")));
        this.tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.config.overlay8")));
    }

    @Override
    protected void init() {
        this.f_96543_ = this.f_96541_.getWindow().getGuiScaledWidth();
        this.f_96544_ = this.f_96541_.getWindow().getGuiScaledHeight();
        this.offsetX = AllConfigs.client().overlayOffsetX.get();
        this.offsetY = AllConfigs.client().overlayOffsetY.get();
    }

    @Override
    public void removed() {
        AllConfigs.client().overlayOffsetX.set(Integer.valueOf(this.offsetX));
        AllConfigs.client().overlayOffsetY.set(Integer.valueOf(this.offsetY));
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        this.updateOffset(x, y);
        return true;
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        this.updateOffset(p_mouseDragged_1_, p_mouseDragged_3_);
        return true;
    }

    private void updateOffset(double windowX, double windowY) {
        this.offsetX = (int) (windowX - (double) (this.f_96543_ / 2));
        this.offsetY = (int) (windowY - (double) (this.f_96544_ / 2));
        int titleLinesCount = 1;
        int tooltipTextWidth = 0;
        for (FormattedText textLine : this.tooltip) {
            int textLineWidth = this.f_96541_.font.width(textLine);
            if (textLineWidth > tooltipTextWidth) {
                tooltipTextWidth = textLineWidth;
            }
        }
        int tooltipHeight = 8;
        if (this.tooltip.size() > 1) {
            tooltipHeight += (this.tooltip.size() - 1) * 10;
            if (this.tooltip.size() > titleLinesCount) {
                tooltipHeight += 2;
            }
        }
        this.offsetX = Mth.clamp(this.offsetX, -(this.f_96543_ / 2) - 5, this.f_96543_ / 2 - tooltipTextWidth - 20);
        this.offsetY = Mth.clamp(this.offsetY, -(this.f_96544_ / 2) + 17, this.f_96544_ / 2 - tooltipHeight + 5);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int posX = this.f_96543_ / 2 + this.offsetX;
        int posY = this.f_96544_ / 2 + this.offsetY;
        graphics.renderComponentTooltip(this.f_96547_, this.tooltip, posX, posY);
        ItemStack item = AllItems.GOGGLES.asStack();
        GuiGameElement.of(item).<RenderElement>at((float) (posX + 10), (float) (posY - 16), 450.0F).render(graphics);
    }
}