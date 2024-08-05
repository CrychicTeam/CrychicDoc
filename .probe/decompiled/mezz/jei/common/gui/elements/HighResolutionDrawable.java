package mezz.jei.common.gui.elements;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

public class HighResolutionDrawable implements IDrawable {

    private final IDrawable drawable;

    private final int scale;

    public HighResolutionDrawable(IDrawable drawable, int scale) {
        int width = drawable.getWidth();
        int height = drawable.getHeight();
        Preconditions.checkArgument(width % scale == 0, String.format("drawable width %s must be divisible by the scale %s", width, scale));
        Preconditions.checkArgument(height % scale == 0, String.format("drawable height %s must be divisible by the scale %s", height, scale));
        this.drawable = drawable;
        this.scale = scale;
    }

    @Override
    public int getWidth() {
        return this.drawable.getWidth() / this.scale;
    }

    @Override
    public int getHeight() {
        return this.drawable.getHeight() / this.scale;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate((float) xOffset, (float) yOffset, 0.0F);
        poseStack.scale(1.0F / (float) this.scale, 1.0F / (float) this.scale, 1.0F);
        this.drawable.draw(guiGraphics);
        poseStack.popPose();
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.scale(1.0F / (float) this.scale, 1.0F / (float) this.scale, 1.0F);
        this.drawable.draw(guiGraphics);
        poseStack.popPose();
    }
}