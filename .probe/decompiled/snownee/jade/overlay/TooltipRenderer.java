package snownee.jade.overlay;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.Jade;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.theme.Theme;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.ITooltipRenderer;
import snownee.jade.impl.Tooltip;
import snownee.jade.impl.config.WailaConfig;
import snownee.jade.util.ClientProxy;

public class TooltipRenderer implements ITooltipRenderer {

    private final Tooltip tooltip;

    private final boolean showIcon;

    private Vec2 totalSize;

    private IElement icon;

    private final int[] padding = new int[] { 4, 3, 1, 4 };

    private Rect2i realRect;

    private float realScale = 1.0F;

    public TooltipRenderer(Tooltip tooltip, boolean showIcon) {
        this.showIcon = showIcon;
        this.tooltip = tooltip;
        if (showIcon) {
            this.icon = RayTracing.INSTANCE.getIcon();
        }
    }

    @Override
    public int getPadding(int i) {
        return this.padding[i];
    }

    @Override
    public void setPadding(int i, int value) {
        this.padding[i] = value;
    }

    @Override
    public void recalculateSize() {
        float width = 0.0F;
        float height = 0.0F;
        for (Tooltip.Line line : this.tooltip.lines) {
            Vec2 size = line.getSize();
            width = Math.max(width, size.x);
            height += size.y;
        }
        float contentHeight = height;
        if (this.hasIcon()) {
            Vec2 size = this.icon.getCachedSize();
            this.padding[3] = (int) ((float) this.padding[3] + size.x + 3.0F);
            height = Math.max(height, size.y);
        }
        width += (float) (this.padding[3] + this.padding[1]);
        height += (float) (this.padding[0] + this.padding[2]);
        this.totalSize = new Vec2(width, height);
        if (this.hasIcon() && this.icon.getCachedSize().y > contentHeight) {
            this.padding[0] = (int) ((float) this.padding[0] + (this.icon.getCachedSize().y - contentHeight) / 2.0F);
        }
    }

    public void draw(GuiGraphics guiGraphics) {
        float x = (float) this.getPadding(3);
        float y = (float) this.getPadding(0);
        for (Tooltip.Line line : this.tooltip.lines) {
            Vec2 size = line.getSize();
            line.render(guiGraphics, x, y, this.totalSize.x - (float) this.getPadding(1), size.y);
            y += size.y;
        }
        if (this.tooltip.sneakyDetails) {
            Minecraft mc = Minecraft.getInstance();
            x = (this.totalSize.x - (float) mc.font.width("▾") + 1.0F) / 2.0F;
            float yOffset = OverlayRenderer.ticks / 5.0F % 8.0F - 2.0F;
            if (yOffset <= 4.0F) {
                y = this.totalSize.y - 6.0F + yOffset;
                float alpha = 1.0F - Math.abs(yOffset) / 2.0F;
                int alphaChannel = (int) (255.0F * Mth.clamp(alpha, 0.0F, 1.0F));
                if (alphaChannel > 4) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(x, y, 0.0F);
                    DisplayHelper.INSTANCE.drawText(guiGraphics, "▾", 0.0F, 0.0F, IThemeHelper.get().theme().infoColor & 16777215 | alphaChannel << 24);
                    guiGraphics.pose().popPose();
                }
            }
        }
        IElement icon = this.getIcon();
        if (icon != null) {
            Vec2 size = icon.getCachedSize();
            Vec2 offset = icon.getTranslation();
            float offsetY = offset.y;
            float min = (float) (this.getPadding(0) + this.getPadding(2)) + size.y;
            if (IWailaConfig.get().getOverlay().getIconMode() == IWailaConfig.IconMode.TOP && min < this.totalSize.y) {
                offsetY += (float) this.getPadding(0);
            } else {
                offsetY += (this.totalSize.y - size.y) / 2.0F;
            }
            float offsetX = (float) this.getPadding(3) + offset.x - size.x - 3.0F;
            Tooltip.drawBorder(guiGraphics, offsetX, offsetY, icon);
            icon.render(guiGraphics, offsetX, offsetY, offsetX + size.x, offsetY + size.y);
        }
    }

    @Override
    public Tooltip getTooltip() {
        return this.tooltip;
    }

    @Override
    public boolean hasIcon() {
        return this.showIcon && Jade.CONFIG.get().getOverlay().shouldShowIcon() && this.icon != null;
    }

    @Override
    public IElement getIcon() {
        return this.hasIcon() ? this.icon : null;
    }

    @Override
    public void setIcon(IElement icon) {
        this.icon = icon;
    }

    @Override
    public Rect2i getPosition() {
        Window window = Minecraft.getInstance().getWindow();
        IWailaConfig.IConfigOverlay overlay = Jade.CONFIG.get().getOverlay();
        int x = (int) ((float) window.getGuiScaledWidth() * overlay.tryFlip(overlay.getOverlayPosX()));
        int y = (int) ((float) window.getGuiScaledHeight() * (1.0F - overlay.getOverlayPosY()));
        int width = (int) this.totalSize.x;
        int height = (int) this.totalSize.y;
        return new Rect2i(x, y, width, height);
    }

    @Override
    public Vec2 getSize() {
        return this.totalSize;
    }

    @Override
    public void setSize(Vec2 totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public float getRealScale() {
        return this.realScale;
    }

    @Nullable
    @Override
    public Rect2i getRealRect() {
        return this.realRect;
    }

    @Override
    public void recalculateRealRect() {
        Rect2i position = this.getPosition();
        WailaConfig.ConfigOverlay overlay = Jade.CONFIG.get().getOverlay();
        if (!overlay.getSquare() || IThemeHelper.get().theme().backgroundTexture != null) {
            position.setWidth(position.getWidth() + 2);
            position.setHeight(position.getHeight() + 2);
            position.setPosition(position.getX() + 1, position.getY() + 1);
        }
        this.realScale = overlay.getOverlayScale();
        Window window = Minecraft.getInstance().getWindow();
        float thresholdHeight = (float) window.getGuiScaledHeight() * overlay.getAutoScaleThreshold();
        if (this.totalSize.y * this.realScale > thresholdHeight) {
            this.realScale = Math.max(this.realScale * 0.5F, thresholdHeight / this.totalSize.y);
        }
        position.setWidth((int) ((float) position.getWidth() * this.realScale));
        position.setHeight((int) ((float) position.getHeight() * this.realScale));
        position.setX((int) ((float) position.getX() - (float) position.getWidth() * overlay.tryFlip(overlay.getAnchorX())));
        position.setY((int) ((float) position.getY() - (float) position.getHeight() * overlay.getAnchorY()));
        IWailaConfig.BossBarOverlapMode mode = Jade.CONFIG.get().getGeneral().getBossBarOverlapMode();
        if (mode == IWailaConfig.BossBarOverlapMode.PUSH_DOWN) {
            Rect2i rect = ClientProxy.getBossBarRect();
            if (rect != null) {
                int tw = position.getWidth();
                int th = position.getHeight();
                int rw = rect.getWidth();
                int rh = rect.getHeight();
                int tx = position.getX();
                int ty = position.getY();
                int rx = rect.getX();
                int ry = rect.getY();
                rw += rx;
                rh += ry;
                tw += tx;
                th += ty;
                if (rw > tx && rh > ty && tw > rx && th > ry) {
                    position.setY(rect.getHeight());
                }
            }
        }
        this.realRect = position;
    }

    public void setPaddingFromTheme(Theme theme) {
        for (int i = 0; i < 4; i++) {
            this.setPadding(i, theme.padding[i]);
        }
        this.recalculateSize();
    }
}