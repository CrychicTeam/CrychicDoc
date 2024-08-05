package snownee.jade.api.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.overlay.DisplayHelper;
import snownee.jade.overlay.ProgressTracker;
import snownee.jade.overlay.WailaTickHandler;

public class BoxStyle implements IBoxStyle {

    public static final BoxStyle DEFAULT = new BoxStyle();

    public int bgColor = 0;

    public int borderColor;

    public float borderWidth = 0.0F;

    public boolean roundCorner;

    public int progressColor = 0;

    public float progress;

    private ResourceLocation tag;

    private Object track;

    public BoxStyle() {
        if (DEFAULT != null) {
            this.borderColor = DEFAULT.borderColor;
        }
    }

    @Override
    public float borderWidth() {
        return this.borderWidth;
    }

    @Override
    public void tag(ResourceLocation tag) {
        this.tag = tag;
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float w, float h) {
        if (this.bgColor != 0) {
            DisplayHelper.fill(guiGraphics, x + this.borderWidth, y + this.borderWidth, x + w - this.borderWidth, y + h - this.borderWidth, this.bgColor);
        }
        DisplayHelper.INSTANCE.drawBorder(guiGraphics, x, y, x + w, y + h, this.borderWidth, this.borderColor, !this.roundCorner);
        if (this.progressColor != 0) {
            float left = this.roundCorner ? x + this.borderWidth : x;
            float width = this.roundCorner ? w - this.borderWidth * 2.0F : w;
            float top = y + h - Math.max(this.borderWidth, 0.5F);
            float progress = this.progress;
            if (this.track == null && this.tag != null) {
                this.track = WailaTickHandler.instance().progressTracker.createInfo(this.tag, progress, false, 0.0F);
            }
            if (this.track != null) {
                progress = ((ProgressTracker.TrackInfo) this.track).tick(Minecraft.getInstance().getDeltaFrameTime());
            }
            DisplayHelper.INSTANCE.drawGradientProgress(guiGraphics, left, top, width, y + h - top, progress, this.progressColor);
        }
    }

    static {
        DEFAULT.borderWidth = 1.0F;
    }
}