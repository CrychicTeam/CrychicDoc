package com.rekindled.embers.util;

import com.rekindled.embers.EmbersClientEvents;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;

public class HeatBarTooltip implements TooltipComponent {

    public FormattedCharSequence normalText;

    public float heat;

    public float maxHeat;

    public int barWidth;

    public HeatBarTooltip(FormattedCharSequence normalText, float heat, float maxHeat, int barWidth) {
        this.normalText = normalText;
        this.heat = heat;
        this.maxHeat = maxHeat;
        this.barWidth = barWidth;
    }

    public HeatBarTooltip(FormattedCharSequence normalText, float heat, float maxHeat) {
        this(normalText, heat, maxHeat, 96);
    }

    public static class HeatBarClientTooltip implements ClientTooltipComponent {

        HeatBarTooltip tooltip;

        public HeatBarClientTooltip(HeatBarTooltip tooltip) {
            this.tooltip = tooltip;
        }

        @Override
        public int getHeight() {
            return 10;
        }

        @Override
        public int getWidth(Font font) {
            return font.width(this.tooltip.normalText) + this.tooltip.barWidth - 24;
        }

        @Override
        public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
            font.drawInBatch(this.tooltip.normalText, (float) mouseX, (float) mouseY, -1, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
            int offset = font.width(this.tooltip.normalText) + 2;
            float x1 = (float) (x + offset + 3);
            float x2 = (float) (x + this.tooltip.barWidth - 3);
            float progress = this.tooltip.heat >= this.tooltip.maxHeat ? 1.0F : this.tooltip.heat / (this.tooltip.maxHeat + this.tooltip.maxHeat * 0.1F);
            x2 = x1 + (x2 - x1) * progress;
            for (float j = 0.0F; j < 10.0F; j++) {
                float coeff = j / 10.0F;
                float coeff2 = (j + 1.0F) / 10.0F;
                for (float k = 0.0F; k < 4.0F; k += 0.5F) {
                    float thick = k / 4.0F * (this.tooltip.heat >= this.tooltip.maxHeat ? (float) Math.sin((double) ((float) EmbersClientEvents.ticks * 0.5F)) * 2.0F + 3.0F : 1.0F);
                    RenderUtil.drawColorRectBatched(graphics.pose(), graphics.bufferSource(), x1 * (1.0F - coeff) + x2 * coeff, (float) y + k, 0.0F, (x2 - x1) / 10.0F, 8.0F - 2.0F * k, 1.0F, 0.25F, 0.0625F, Math.min(1.0F, thick * 0.25F + thick * EmberGenUtil.getEmberDensity(6L, (int) ((float) (EmbersClientEvents.ticks * 12) + 4.0F * (x1 * (1.0F - coeff) + x2 * coeff)), 4 * (int) ((float) y + k))), 1.0F, 0.25F, 0.0625F, Math.min(1.0F, thick * 0.25F + thick * EmberGenUtil.getEmberDensity(6L, (int) ((float) (EmbersClientEvents.ticks * 12) + 4.0F * (x1 * (1.0F - coeff2) + x2 * coeff2)), 4 * (int) ((float) y + k))), 1.0F, 0.25F, 0.0625F, Math.min(1.0F, thick * 0.25F + thick * EmberGenUtil.getEmberDensity(6L, (int) ((float) (EmbersClientEvents.ticks * 12) + 4.0F * (x1 * (1.0F - coeff2) + x2 * coeff2)), 4 * (int) ((double) y + (8.0 - (double) k)))), 1.0F, 0.25F, 0.0625F, Math.min(1.0F, thick * 0.25F + thick * EmberGenUtil.getEmberDensity(6L, (int) ((float) (EmbersClientEvents.ticks * 12) + 4.0F * (x1 * (1.0F - coeff) + x2 * coeff)), 4 * (int) ((double) y + (8.0 - (double) k)))));
                }
            }
            x1 = (float) (x + offset + 3);
            x2 = (float) (x + this.tooltip.barWidth - 3);
            float point = x1 + (x2 - x1) * progress;
            for (float k = 0.0F; k < 4.0F; k = (float) ((double) k + 0.5)) {
                float thick = (float) ((double) k / 4.0);
                RenderUtil.drawColorRectBatched(graphics.pose(), graphics.bufferSource(), point, (float) y + k, 0.0F, Math.min(x2 - point, (x2 - x1) / 10.0F), 8.0F - 2.0F * k, 1.0F, 0.25F, 0.0625F, 1.0F * Math.min(1.0F, thick * 0.25F + thick * EmberGenUtil.getEmberDensity(6L, (int) ((float) (EmbersClientEvents.ticks * 12) + 4.0F * point), 4 * (int) ((float) y + k))), 0.25F, 0.0625F, 0.015625F, 0.0F, 0.25F, 0.0625F, 0.015625F, 0.0F, 1.0F, 0.25F, 0.0625F, 1.0F * Math.min(1.0F, thick * 0.25F + thick * EmberGenUtil.getEmberDensity(6L, (int) ((float) (EmbersClientEvents.ticks * 12) + 4.0F * point), 4 * (int) ((double) y + (8.0 - (double) k)))));
            }
            x1 = (float) (x + offset + 3);
            x2 = (float) (x + this.tooltip.barWidth - 3);
            x1 = x2 - (x2 - x1) * (1.0F - progress);
            for (float j = 0.0F; j < 10.0F; j++) {
                float coeff = j / 10.0F;
                float coeff2 = (j + 1.0F) / 10.0F;
                for (float k = 0.0F; k < 4.0F; k += 0.5F) {
                    float thick = (float) ((double) k / 4.0);
                    RenderUtil.drawColorRectBatched(graphics.pose(), graphics.bufferSource(), x1 * (1.0F - coeff) + x2 * coeff, (float) y + k, 0.0F, (x2 - x1) / 10.0F, 8.0F - 2.0F * k, 0.25F, 0.0625F, 0.015625F, 0.75F * Math.min(1.0F, thick * 0.25F + thick * EmberGenUtil.getEmberDensity(6L, (int) ((float) (EmbersClientEvents.ticks * 12) + 4.0F * (x1 * (1.0F - coeff) + x2 * coeff)), 4 * (int) ((float) y + k))), 0.25F, 0.0625F, 0.015625F, 0.75F * Math.min(1.0F, thick * 0.25F + thick * EmberGenUtil.getEmberDensity(6L, (int) ((float) (EmbersClientEvents.ticks * 12) + 4.0F * (x1 * (1.0F - coeff2) + x2 * coeff2)), 4 * (int) ((float) y + k))), 0.25F, 0.0625F, 0.015625F, 0.75F * Math.min(1.0F, thick * 0.25F + thick * EmberGenUtil.getEmberDensity(6L, (int) ((float) (EmbersClientEvents.ticks * 12) + 4.0F * (x1 * (1.0F - coeff2) + x2 * coeff2)), 4 * (int) ((double) y + (8.0 - (double) k)))), 0.25F, 0.0625F, 0.015625F, 0.75F * Math.min(1.0F, thick * 0.25F + thick * EmberGenUtil.getEmberDensity(6L, (int) ((float) (EmbersClientEvents.ticks * 12) + 4.0F * (x1 * (1.0F - coeff) + x2 * coeff)), 4 * (int) ((double) y + (8.0 - (double) k)))));
                }
            }
            RenderUtil.drawHeatBarEnd(graphics.pose(), graphics.bufferSource(), (float) (offset + x), (float) (y - 1), 0.0F, 8.0F, 10.0F, 0.0F, 0.0F, 0.5F, 0.625F);
            RenderUtil.drawHeatBarEnd(graphics.pose(), graphics.bufferSource(), (float) (offset + x + this.tooltip.barWidth - 8 - 26), (float) (y - 1), 0.0F, 8.0F, 10.0F, 0.5F, 0.0F, 1.0F, 0.625F);
        }
    }
}