package net.minecraft.client.gui.screens.inventory.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;

public interface ClientTooltipComponent {

    static ClientTooltipComponent create(FormattedCharSequence formattedCharSequence0) {
        return new ClientTextTooltip(formattedCharSequence0);
    }

    static ClientTooltipComponent create(TooltipComponent tooltipComponent0) {
        if (tooltipComponent0 instanceof BundleTooltip) {
            return new ClientBundleTooltip((BundleTooltip) tooltipComponent0);
        } else {
            throw new IllegalArgumentException("Unknown TooltipComponent");
        }
    }

    int getHeight();

    int getWidth(Font var1);

    default void renderText(Font font0, int int1, int int2, Matrix4f matrixF3, MultiBufferSource.BufferSource multiBufferSourceBufferSource4) {
    }

    default void renderImage(Font font0, int int1, int int2, GuiGraphics guiGraphics3) {
    }
}