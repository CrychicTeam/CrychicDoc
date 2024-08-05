package journeymap.client.ui.theme;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import journeymap.client.properties.MiniMapProperties;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.texture.Texture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ThemeCompassPoints {

    final MutableComponent textNorth = Component.translatable("jm.minimap.compass.n");

    final MutableComponent textSouth = Component.translatable("jm.minimap.compass.s");

    final MutableComponent textEast = Component.translatable("jm.minimap.compass.e");

    final MutableComponent textWest = Component.translatable("jm.minimap.compass.w");

    final Point2D pointNorth;

    final Point2D pointSouth;

    final Point2D pointWest;

    final Point2D pointEast;

    final boolean showNorth;

    final boolean showSouth;

    final boolean showEast;

    final boolean showWest;

    final float fontScale;

    final float compassLabelHeight;

    final Theme.LabelSpec compassLabel;

    final Theme.ColorSpec compassPoint;

    final Texture compassPointTex;

    final int xOffset;

    final int yOffset;

    final double shiftVert;

    final double shiftHorz;

    final int labelShiftVert;

    private double x;

    private double y;

    public ThemeCompassPoints(int x, int y, int halfWidth, int halfHeight, Theme.Minimap.MinimapSpec minimapSpec, MiniMapProperties miniMapProperties, Texture compassPointTex, float labelHeight) {
        this.x = (double) x;
        this.y = (double) y;
        this.pointNorth = new Double((double) (x + halfWidth), (double) y);
        this.pointSouth = new Double((double) (x + halfWidth), (double) (y + halfHeight + halfHeight));
        this.pointWest = new Double((double) x, (double) (y + halfHeight));
        this.pointEast = new Double((double) (x + halfWidth + halfWidth), (double) (y + halfHeight));
        this.fontScale = miniMapProperties.compassFontScale.get();
        this.compassLabelHeight = labelHeight;
        this.compassLabel = minimapSpec.compassLabel;
        this.compassPoint = minimapSpec.compassPoint;
        this.compassPointTex = compassPointTex;
        if (this.compassPointTex != null) {
            this.shiftVert = minimapSpec.compassPointOffset * (double) this.fontScale;
            this.shiftHorz = minimapSpec.compassPointOffset * (double) this.fontScale;
            this.pointNorth.setLocation(this.pointNorth.getX(), this.pointNorth.getY() - this.shiftVert);
            this.pointSouth.setLocation(this.pointSouth.getX(), this.pointSouth.getY() + this.shiftVert);
            this.pointWest.setLocation(this.pointWest.getX() - this.shiftHorz, this.pointWest.getY());
            this.pointEast.setLocation(this.pointEast.getX() + this.shiftHorz, this.pointEast.getY());
            this.xOffset = (int) ((float) compassPointTex.getWidth() * this.fontScale / 2.0F);
            this.yOffset = (int) ((float) compassPointTex.getHeight() * this.fontScale / 2.0F);
        } else {
            this.xOffset = 0;
            this.yOffset = 0;
            this.shiftHorz = 0.0;
            this.shiftVert = 0.0;
        }
        this.labelShiftVert = 0;
        this.showNorth = minimapSpec.compassShowNorth;
        this.showSouth = minimapSpec.compassShowSouth;
        this.showEast = minimapSpec.compassShowEast;
        this.showWest = minimapSpec.compassShowWest;
    }

    public static float getCompassPointScale(float compassLabelHeight, Theme.Minimap.MinimapSpec minimapSpec, Texture compassPointTex) {
        return (compassLabelHeight + (float) minimapSpec.compassPointLabelPad) / ((float) compassPointTex.getHeight() * 1.0F);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void drawPoints(PoseStack poseStack, double rotation) {
        if (this.compassPointTex != null) {
            int color = this.compassPoint.getColor();
            float alpha = this.compassPoint.alpha;
            if (this.showNorth) {
                DrawUtil.drawColoredImage(poseStack, this.compassPointTex, color, alpha, this.pointNorth.getX() - (double) this.xOffset, this.pointNorth.getY() - (double) this.yOffset, this.fontScale, 0.0);
            }
            if (this.showSouth) {
                DrawUtil.drawColoredImage(poseStack, this.compassPointTex, color, alpha, this.pointSouth.getX() - (double) this.xOffset, this.pointSouth.getY() - (double) this.yOffset, this.fontScale, 180.0);
            }
            if (this.showWest) {
                DrawUtil.drawColoredImage(poseStack, this.compassPointTex, color, alpha, this.pointWest.getX() - (double) this.xOffset, this.pointWest.getY() - (double) this.yOffset, this.fontScale, -90.0);
            }
            if (this.showEast) {
                DrawUtil.drawColoredImage(poseStack, this.compassPointTex, color, alpha, this.pointEast.getX() - (double) this.xOffset, this.pointEast.getY() - (double) this.yOffset, this.fontScale, 90.0);
            }
        }
    }

    public void drawLabels(PoseStack poseStack, MultiBufferSource buffers, double rotation) {
        if (this.showNorth) {
            DrawUtil.drawBatchLabel(poseStack, this.textNorth, buffers, this.compassLabel, this.pointNorth.getX(), this.pointNorth.getY() + (double) this.labelShiftVert, DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, (double) this.fontScale, rotation);
        }
        if (this.showSouth) {
            DrawUtil.drawBatchLabel(poseStack, this.textSouth, buffers, this.compassLabel, this.pointSouth.getX(), this.pointSouth.getY() + (double) this.labelShiftVert, DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, (double) this.fontScale, rotation);
        }
        if (this.showWest) {
            DrawUtil.drawBatchLabel(poseStack, this.textWest, buffers, this.compassLabel, this.pointWest.getX(), this.pointWest.getY() + (double) this.labelShiftVert, DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, (double) this.fontScale, rotation);
        }
        if (this.showEast) {
            DrawUtil.drawBatchLabel(poseStack, this.textEast, buffers, this.compassLabel, this.pointEast.getX(), this.pointEast.getY() + (double) this.labelShiftVert, DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, (double) this.fontScale, rotation);
        }
    }
}