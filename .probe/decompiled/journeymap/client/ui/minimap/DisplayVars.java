package journeymap.client.ui.minimap;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import journeymap.client.io.ThemeLoader;
import journeymap.client.model.MapType;
import journeymap.client.properties.MiniMapProperties;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.option.LocationFormat;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemeCompassPoints;
import journeymap.client.ui.theme.ThemeLabelSource;
import journeymap.client.ui.theme.ThemeMinimapFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Tuple;

public class DisplayVars {

    final Position position;

    final Shape shape;

    final Orientation orientation;

    final double fontScale;

    final int displayWidth;

    final int displayHeight;

    final float terrainAlpha;

    final Window mainWindow;

    public final int minimapWidth;

    public final int minimapHeight;

    public final int textureX;

    public final int textureY;

    final int translateX;

    final int translateY;

    final double reticleSegmentLength;

    public final Double centerPoint;

    final boolean showCompass;

    final boolean showReticle;

    final List<Tuple<LabelVars, ThemeLabelSource.InfoSlot>> labels = new ArrayList(4);

    final Theme theme;

    final ThemeMinimapFrame minimapFrame;

    final ThemeCompassPoints minimapCompassPoints;

    final Theme.Minimap.MinimapSpec minimapSpec;

    final LocationFormat.LocationFormatKeys locationFormatKeys;

    final boolean locationFormatVerbose;

    final boolean frameRotates;

    public int marginX;

    public int marginY;

    DisplayVars.MapTypeStatus mapTypeStatus;

    DisplayVars.MapPresetStatus mapPresetStatus;

    float positionY;

    float positionX;

    DisplayVars(Minecraft mc, MiniMapProperties miniMapProperties) {
        this.mainWindow = mc.getWindow();
        this.showCompass = miniMapProperties.showCompass.get();
        this.showReticle = miniMapProperties.showReticle.get();
        this.position = miniMapProperties.position.get();
        this.orientation = miniMapProperties.orientation.get();
        this.displayWidth = mc.getWindow().getScreenWidth();
        this.displayHeight = mc.getWindow().getScreenHeight();
        this.terrainAlpha = Math.max(0.0F, Math.min(1.0F, (float) miniMapProperties.terrainAlpha.get().intValue() / 100.0F));
        this.locationFormatKeys = new LocationFormat().getFormatKeys(miniMapProperties.locationFormat.get());
        this.locationFormatVerbose = miniMapProperties.locationFormatVerbose.get();
        this.theme = ThemeLoader.getCurrentTheme();
        this.positionY = miniMapProperties.positionY.get();
        this.positionX = miniMapProperties.positionX.get();
        switch((Shape) miniMapProperties.shape.get()) {
            case Rectangle:
                if (this.theme.minimap.square != null) {
                    this.shape = Shape.Rectangle;
                    this.minimapSpec = this.theme.minimap.square;
                    double ratio = (double) mc.getWindow().getScreenWidth() * 1.0 / (double) mc.getWindow().getScreenHeight();
                    this.minimapHeight = miniMapProperties.getSize();
                    this.minimapWidth = (int) ((double) this.minimapHeight * ratio);
                    this.reticleSegmentLength = (double) this.minimapWidth / 1.5;
                    break;
                }
            case Circle:
                if (this.theme.minimap.circle != null) {
                    this.shape = Shape.Circle;
                    this.minimapSpec = this.theme.minimap.circle;
                    this.minimapWidth = miniMapProperties.getSize();
                    this.minimapHeight = miniMapProperties.getSize();
                    this.reticleSegmentLength = (double) (this.minimapHeight / 2);
                    break;
                }
            case Square:
            default:
                this.shape = Shape.Square;
                this.minimapSpec = this.theme.minimap.square;
                this.minimapWidth = miniMapProperties.getSize();
                this.minimapHeight = miniMapProperties.getSize();
                this.reticleSegmentLength = Math.sqrt((double) (this.minimapHeight * this.minimapHeight + this.minimapWidth * this.minimapWidth)) / 2.0;
        }
        this.fontScale = (double) miniMapProperties.fontScale.get().floatValue();
        Font fontRenderer = mc.font;
        int topInfoLabelsHeight = this.getInfoLabelAreaHeight(fontRenderer, this.minimapSpec.labelTop, (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(miniMapProperties.info1Label.get()), (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(miniMapProperties.info2Label.get()));
        int bottomInfoLabelsHeight = this.getInfoLabelAreaHeight(fontRenderer, this.minimapSpec.labelBottom, (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(miniMapProperties.info3Label.get()), (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(miniMapProperties.info4Label.get()));
        float compassFontScale = miniMapProperties.compassFontScale.get();
        float compassLabelHeight = 0.0F;
        if (this.showCompass) {
            compassLabelHeight = (float) DrawUtil.getLabelHeight(fontRenderer, this.minimapSpec.compassLabel.shadow) * compassFontScale;
        }
        this.minimapFrame = new ThemeMinimapFrame(this.theme, this.minimapSpec, miniMapProperties, this.minimapWidth, this.minimapHeight);
        this.marginX = this.marginY = this.minimapSpec.margin;
        if (this.showCompass) {
            boolean compassExists = this.minimapSpec.compassPoint != null && this.minimapSpec.compassPoint.width > 0;
            double compassPointMargin;
            if (compassExists) {
                Texture compassPointTex = this.minimapFrame.getCompassPoint();
                float compassPointScale = ThemeCompassPoints.getCompassPointScale(compassLabelHeight, this.minimapSpec, compassPointTex);
                compassPointMargin = (double) ((float) (compassPointTex.getWidth() / 2) * compassPointScale);
            } else {
                compassPointMargin = (double) compassLabelHeight;
            }
            this.marginX = (int) Math.max((double) this.marginX, Math.ceil(compassPointMargin));
            this.marginY = (int) Math.max((double) this.marginY, Math.ceil(compassPointMargin) + (double) (compassLabelHeight / 2.0F));
        }
        if (!this.minimapSpec.labelBottomInside) {
            this.marginY += bottomInfoLabelsHeight;
        }
        int halfWidth = this.minimapWidth / 2;
        int halfHeight = this.minimapHeight / 2;
        int screenHeight = mc.getWindow().getScreenHeight();
        int screenWidth = mc.getWindow().getScreenWidth();
        double scale = mc.getWindow().getGuiScale();
        switch(this.position) {
            case BottomRight:
                if (!this.minimapSpec.labelBottomInside) {
                    this.marginY += bottomInfoLabelsHeight;
                }
                this.textureX = screenWidth - this.minimapWidth - this.marginX;
                this.textureY = screenHeight - this.minimapHeight - this.marginY;
                this.translateX = screenWidth / 2 - halfWidth - this.marginX;
                this.translateY = screenHeight / 2 - halfHeight - this.marginY;
                break;
            case TopLeft:
                if (!this.minimapSpec.labelTopInside) {
                    this.marginY = Math.max(this.marginY, topInfoLabelsHeight + 2 * this.minimapSpec.margin);
                }
                this.textureX = this.marginX;
                this.textureY = this.marginY;
                this.translateX = -(screenWidth / 2) + halfWidth + this.marginX;
                this.translateY = -(screenHeight / 2) + halfHeight + this.marginY;
                break;
            case BottomLeft:
                if (!this.minimapSpec.labelBottomInside) {
                    this.marginY += bottomInfoLabelsHeight;
                }
                this.textureX = this.marginX;
                this.textureY = screenHeight - this.minimapHeight - this.marginY;
                this.translateX = -(screenWidth / 2) + halfWidth + this.marginX;
                this.translateY = screenHeight / 2 - halfHeight - this.marginY;
                break;
            case TopCenter:
                if (!this.minimapSpec.labelTopInside) {
                    this.marginY = Math.max(this.marginY, topInfoLabelsHeight + 2 * this.minimapSpec.margin);
                }
                this.textureX = (screenWidth - this.minimapWidth) / 2;
                this.textureY = this.marginY;
                this.translateX = 0;
                this.translateY = -(screenHeight / 2) + halfHeight + this.marginY;
                break;
            case Center:
                this.textureX = (screenWidth - this.minimapWidth) / 2;
                this.textureY = (screenHeight - this.minimapHeight) / 2;
                this.translateX = 0;
                this.translateY = 0;
                break;
            case Custom:
                this.textureX = (int) ((float) screenWidth * this.positionX);
                this.textureY = (int) ((float) screenHeight * this.positionY);
                this.translateX = this.textureX - screenWidth / 2 + halfWidth;
                this.translateY = this.textureY - screenHeight / 2 + halfHeight;
                break;
            case TopRight:
            default:
                if (!this.minimapSpec.labelTopInside) {
                    this.marginY = Math.max(this.marginY, topInfoLabelsHeight + 2 * this.minimapSpec.margin);
                }
                this.textureX = screenWidth - this.minimapWidth - this.marginX;
                this.textureY = this.marginY;
                this.translateX = screenWidth / 2 - halfWidth - this.marginX;
                this.translateY = -(screenHeight / 2) + halfHeight + this.marginY;
        }
        this.minimapFrame.setPosition((double) this.textureX, (double) this.textureY);
        this.centerPoint = new Double((double) (this.textureX + halfWidth), (double) (this.textureY + halfHeight));
        this.minimapCompassPoints = new ThemeCompassPoints(this.textureX, this.textureY, halfWidth, halfHeight, this.minimapSpec, miniMapProperties, this.minimapFrame.getCompassPoint(), compassLabelHeight);
        if (this.shape == Shape.Circle) {
            this.frameRotates = ((Theme.Minimap.MinimapCircle) this.minimapSpec).rotates;
        } else {
            this.frameRotates = false;
        }
        int centerX = (int) Math.floor((double) (this.textureX + this.minimapWidth / 2));
        if (topInfoLabelsHeight > 0) {
            int startY = this.minimapSpec.labelTopInside ? this.textureY + this.minimapSpec.margin : this.textureY - this.minimapSpec.margin - topInfoLabelsHeight;
            this.positionLabels(fontRenderer, centerX, startY, this.minimapSpec.labelTop, (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(miniMapProperties.info1Label.get()), (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(miniMapProperties.info2Label.get()));
        }
        if (bottomInfoLabelsHeight > 0) {
            int startY = this.textureY + this.minimapHeight;
            startY += this.minimapSpec.labelBottomInside ? -this.minimapSpec.margin - bottomInfoLabelsHeight : this.minimapSpec.margin;
            this.positionLabels(fontRenderer, centerX, startY, this.minimapSpec.labelBottom, (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(miniMapProperties.info3Label.get()), (ThemeLabelSource.InfoSlot) ThemeLabelSource.values.get(miniMapProperties.info4Label.get()));
        }
        ThemeLabelSource.resetCaches();
    }

    public Shape getShape() {
        return this.shape;
    }

    public int getInfoLabelAreaHeight(Font fontRenderer, Theme.LabelSpec labelSpec, ThemeLabelSource.InfoSlot... themeLabelSources) {
        int labelHeight = this.getInfoLabelHeight(fontRenderer, labelSpec);
        int areaHeight = 0;
        for (ThemeLabelSource.InfoSlot themeLabelSource : themeLabelSources) {
            areaHeight += themeLabelSource.isShown() ? labelHeight : 0;
        }
        return areaHeight;
    }

    private int getInfoLabelHeight(Font fontRenderer, Theme.LabelSpec labelSpec) {
        return (int) ((double) (DrawUtil.getLabelHeight(fontRenderer, labelSpec.shadow) + labelSpec.margin) * this.fontScale);
    }

    private void positionLabels(Font fontRenderer, int centerX, int startY, Theme.LabelSpec labelSpec, ThemeLabelSource.InfoSlot... themeLabelSources) {
        int labelHeight = this.getInfoLabelHeight(fontRenderer, labelSpec);
        int labelY = startY;
        for (ThemeLabelSource.InfoSlot themeLabelSource : themeLabelSources) {
            if (themeLabelSource.isShown()) {
                LabelVars labelVars = new LabelVars(this, (double) centerX, (double) labelY, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, this.fontScale, labelSpec);
                Tuple<LabelVars, ThemeLabelSource.InfoSlot> tuple = new Tuple<>(labelVars, themeLabelSource);
                this.labels.add(tuple);
                labelY += labelHeight;
            }
        }
    }

    public void drawInfoLabels(PoseStack poseStack, MultiBufferSource buffers, long currentTimeMillis) {
        for (Tuple<LabelVars, ThemeLabelSource.InfoSlot> label : this.labels) {
            label.getA().draw(poseStack, buffers, label.getB().getLabelText(currentTimeMillis));
        }
    }

    DisplayVars.MapPresetStatus getMapPresetStatus(MapType mapType, int miniMapId) {
        if (this.mapPresetStatus == null || !mapType.equals(this.mapPresetStatus.mapType) || miniMapId != this.mapPresetStatus.miniMapId) {
            this.mapPresetStatus = new DisplayVars.MapPresetStatus(mapType, miniMapId);
        }
        return this.mapPresetStatus;
    }

    DisplayVars.MapTypeStatus getMapTypeStatus(MapType mapType) {
        if (this.mapTypeStatus == null || !mapType.equals(this.mapTypeStatus.mapType)) {
            this.mapTypeStatus = new DisplayVars.MapTypeStatus(mapType);
        }
        return this.mapTypeStatus;
    }

    class MapPresetStatus {

        private int miniMapId;

        private int scale = 4;

        private MapType mapType;

        private String name;

        private Integer color;

        MapPresetStatus(MapType mapType, int miniMapId) {
            this.miniMapId = miniMapId;
            this.mapType = mapType;
            this.color = 16777215;
            this.name = Integer.toString(miniMapId);
        }

        void draw(GuiGraphics graphics, Double mapCenter, float alpha, double rotation) {
            DrawUtil.drawLabel(graphics, this.name, mapCenter.getX(), mapCenter.getY() + 8.0, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, 0, 0.0F, this.color, alpha, (double) this.scale, true, rotation);
        }
    }

    class MapTypeStatus {

        private MapType mapType;

        private String name;

        private Texture tex;

        private Integer color;

        private Integer opposite;

        private double x;

        private double y;

        private float bgScale;

        private float scaleHeightOffset;

        MapTypeStatus(MapType mapType) {
            this.mapType = mapType;
            this.name = mapType.isUnderground() ? "caves" : mapType.name();
            this.tex = TextureCache.getThemeTexture(DisplayVars.this.theme, String.format("icon/%s.png", this.name));
            this.color = 16777215;
            this.opposite = 4210752;
            this.bgScale = 1.15F;
            this.scaleHeightOffset = ((float) this.tex.getHeight() * this.bgScale - (float) this.tex.getHeight()) / 2.0F;
        }

        void draw(PoseStack poseStack, Double mapCenter, float alpha, double rotation) {
            this.x = mapCenter.getX() - (double) (this.tex.getWidth() / 2);
            this.y = mapCenter.getY() - (double) this.tex.getHeight() - 8.0;
            DrawUtil.drawColoredImage(poseStack, this.tex, this.opposite, alpha, mapCenter.getX() - (double) ((float) this.tex.getWidth() * this.bgScale / 2.0F), mapCenter.getY() - (double) ((float) this.tex.getHeight() * this.bgScale) + (double) this.scaleHeightOffset - 8.0, this.bgScale, rotation);
            DrawUtil.drawColoredImage(poseStack, this.tex, this.color, alpha, this.x, this.y, 1.0F, 0.0);
        }
    }
}