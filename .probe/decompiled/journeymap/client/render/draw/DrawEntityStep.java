package journeymap.client.render.draw;

import com.google.common.cache.CacheLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.lang.ref.WeakReference;
import journeymap.client.JourneymapClient;
import journeymap.client.data.DataCache;
import journeymap.client.model.EntityDTO;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.texture.Texture;
import journeymap.client.ui.minimap.EntityDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;

public class DrawEntityStep implements DrawStep {

    static final Integer labelBg = 0;

    static final int labelBgAlpha = 180;

    static final Integer labelFg = 16777215;

    static final int labelFgAlpha = 225;

    boolean useDots;

    int elevationOffset;

    int color;

    boolean hideSneaks;

    boolean hideSpectators;

    boolean showHeading = true;

    boolean showName = true;

    Minecraft minecraft = Minecraft.getInstance();

    Texture entityTexture;

    Texture locatorTexture;

    WeakReference<LivingEntity> entityLivingRef;

    Component customName;

    Component playerTeamName;

    Point2D screenPosition;

    EntityDTO entityDTO;

    float drawScale = 1.0F;

    private DrawEntityStep(LivingEntity entityLiving) {
        this.entityLivingRef = new WeakReference(entityLiving);
        this.hideSneaks = JourneymapClient.getInstance().getCoreProperties().hideSneakingEntities.get();
        this.hideSpectators = JourneymapClient.getInstance().getCoreProperties().hideSpectators.get();
    }

    public void update(EntityDisplay entityDisplay, Texture locatorTexture, Texture entityTexture, int color, boolean showHeading, boolean showName, float entityDrawScale) {
        LivingEntity entityLiving = (LivingEntity) this.entityLivingRef.get();
        this.entityDTO = DataCache.INSTANCE.getEntityDTO(entityLiving);
        if (showName && entityLiving != null) {
            this.customName = this.entityDTO.getCustomName();
        }
        this.useDots = entityDisplay.isDots();
        this.color = color;
        this.locatorTexture = locatorTexture;
        this.entityTexture = entityTexture;
        this.drawScale = entityDrawScale;
        this.showHeading = showHeading;
        this.showName = showName;
        if (entityLiving instanceof Player) {
            Team team = entityLiving.m_5647_();
            if (team != null && showName) {
                this.playerTeamName = PlayerTeam.formatNameForTeam(entityLiving.m_5647_(), entityLiving.m_7755_());
            } else {
                this.playerTeamName = null;
            }
        }
    }

    @Override
    public void draw(GuiGraphics graphics, MultiBufferSource buffers, DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
        if (pass != DrawStep.Pass.Tooltip) {
            LivingEntity entityLiving = (LivingEntity) this.entityLivingRef.get();
            if (pass == DrawStep.Pass.Object) {
                if (entityLiving == null || !entityLiving.isAlive() || entityLiving.m_20177_(this.minecraft.player) || this.hideSneaks && entityLiving.m_6144_() || this.hideSpectators && entityLiving.m_5833_()) {
                    this.screenPosition = null;
                    return;
                }
                this.screenPosition = gridRenderer.getPixel(entityLiving.m_20185_(), entityLiving.m_20189_());
            }
            if (this.screenPosition != null) {
                double heading = (double) entityLiving.m_146908_();
                double drawX = this.screenPosition.getX() + xOffset;
                double drawY = this.screenPosition.getY() + yOffset;
                float alpha = 1.0F;
                if (entityLiving.m_20186_() > this.minecraft.player.m_20186_()) {
                    alpha = 1.0F - Math.max(0.1F, (float) ((entityLiving.m_20186_() - this.minecraft.player.m_20186_()) / 32.0));
                } else if (entityLiving.m_20186_() < this.minecraft.player.m_20186_()) {
                    alpha = 1.0F - Math.max(0.1F, (float) ((this.minecraft.player.m_20186_() - entityLiving.m_20186_()) / 32.0));
                }
                if (entityLiving instanceof Player) {
                    this.drawPlayer(graphics.pose(), pass, buffers, drawX, drawY, gridRenderer, alpha, heading, fontScale, rotation);
                } else {
                    this.drawCreature(graphics.pose(), pass, buffers, drawX, drawY, gridRenderer, alpha, heading, fontScale, rotation);
                }
            }
        }
    }

    private void drawEntityTooltip(PoseStack stack, GridRenderer gridRenderer, double drawX, double drawY, int imageWidth, int imageHeight) {
        if (gridRenderer.fullscreen != null && this.entityDTO != null && this.entityDTO.getEntityToolTips() != null && this.isMouseOverEntity(gridRenderer, drawX, drawY, imageWidth, imageHeight)) {
            gridRenderer.fullscreen.queueToolTip(this.entityDTO.getEntityToolTips());
        }
        boolean specialDebug = false;
        if (specialDebug) {
            double thick = 2.0;
            double x = drawX - (double) (imageWidth / 2);
            double y = drawY - (double) (imageHeight / 2);
            DrawUtil.drawRectangle(stack, x - thick * thick, y - thick * thick, (double) imageWidth + thick * 4.0, thick, 0, 0.6F);
            DrawUtil.drawRectangle(stack, x - thick, y - thick, (double) imageWidth + thick * thick, thick, 16777215, 0.6F);
            DrawUtil.drawRectangle(stack, x - thick * thick, y - thick, thick, (double) imageHeight + thick * thick, 0, 0.6F);
            DrawUtil.drawRectangle(stack, x - thick, y, thick, (double) imageHeight, 16777215, 0.6F);
            DrawUtil.drawRectangle(stack, x + (double) imageWidth, y, thick, (double) imageHeight, 16777215, 0.6F);
            DrawUtil.drawRectangle(stack, x + (double) imageWidth + thick, y - thick, thick, (double) imageHeight + thick * thick, 0, 0.6F);
            DrawUtil.drawRectangle(stack, x - thick, y + (double) imageHeight, (double) imageWidth + thick * thick, thick, 16777215, 0.6F);
            DrawUtil.drawRectangle(stack, x - thick * thick, y + (double) imageHeight + thick, (double) imageWidth + thick * 4.0, thick, 0, 0.6F);
        }
    }

    private boolean isMouseOverEntity(GridRenderer gridRenderer, double drawX, double drawY, int imageWidth, int imageHeight) {
        double mouseX = (double) gridRenderer.mouseX * gridRenderer.fullscreen.getScaleFactor();
        double mouseY = (double) gridRenderer.mouseY * gridRenderer.fullscreen.getScaleFactor();
        double x = drawX - (double) (imageWidth / 2);
        double y = drawY - (double) (imageHeight / 2);
        return mouseX >= x && mouseX <= x + (double) imageWidth && mouseY >= y && mouseY <= y + (double) imageHeight;
    }

    private void drawPlayer(PoseStack poseStack, DrawStep.Pass pass, MultiBufferSource buffers, double drawX, double drawY, GridRenderer gridRenderer, float alpha, double heading, double fontScale, double rotation) {
        LivingEntity entityLiving = (LivingEntity) this.entityLivingRef.get();
        if (entityLiving != null) {
            if (pass == DrawStep.Pass.Object) {
                if (this.locatorTexture != null) {
                    DrawUtil.drawColoredEntity(poseStack, drawX, drawY, this.locatorTexture, this.color, alpha, this.drawScale, this.showHeading ? heading : -rotation);
                    this.drawEntityTooltip(poseStack, gridRenderer, drawX, drawY, this.locatorTexture.getWidth(), this.locatorTexture.getHeight());
                }
                if (this.entityTexture != null) {
                    if (this.useDots) {
                        boolean flip = false;
                        this.elevationOffset = (int) (DataCache.getPlayer().posY - entityLiving.m_20186_());
                        if (this.elevationOffset < -1 || this.elevationOffset > 1) {
                            flip = this.elevationOffset < -1;
                            DrawUtil.drawColoredEntity(poseStack, drawX, drawY, this.entityTexture, this.color, alpha, this.drawScale, flip ? -rotation + 180.0 : -rotation);
                            this.drawEntityTooltip(poseStack, gridRenderer, drawX, drawY, this.entityTexture.getWidth(), this.entityTexture.getHeight());
                        }
                    } else {
                        DrawUtil.drawColoredEntity(poseStack, drawX, drawY, this.entityTexture, this.color, alpha, this.drawScale, -rotation);
                        this.drawEntityTooltip(poseStack, gridRenderer, drawX, drawY, this.entityTexture.getWidth(), this.entityTexture.getHeight());
                    }
                }
            }
            if (pass == DrawStep.Pass.Text) {
                int labelOffset = this.entityTexture == null ? 0 : (rotation == 0.0 ? -this.entityTexture.getHeight() / 2 : this.entityTexture.getHeight() / 2);
                Point2D labelPoint = gridRenderer.shiftWindowPosition((double) ((int) drawX), (double) ((int) drawY), 0, -labelOffset);
                if (this.playerTeamName != null && this.showName) {
                    DrawUtil.drawBatchLabel(poseStack, this.playerTeamName, buffers, labelPoint.getX(), labelPoint.getY(), DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, 0, 0.8F, 16777215, 1.0F, fontScale, false, rotation);
                } else if (this.showName) {
                    DrawUtil.drawBatchLabel(poseStack, entityLiving.m_7755_(), buffers, labelPoint.getX(), labelPoint.getY(), DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, 0, 0.8F, 65280, 1.0F, fontScale, false, rotation);
                }
            }
        }
    }

    private void drawCreature(PoseStack poseStack, DrawStep.Pass pass, MultiBufferSource buffers, double drawX, double drawY, GridRenderer gridRenderer, float alpha, double heading, double fontScale, double rotation) {
        LivingEntity entityLiving = (LivingEntity) this.entityLivingRef.get();
        if (entityLiving != null) {
            if (pass == DrawStep.Pass.Object && this.locatorTexture != null) {
                DrawUtil.drawColoredEntity(poseStack, drawX, drawY, this.locatorTexture, this.color, alpha, this.drawScale, this.showHeading ? heading : -rotation);
                this.drawEntityTooltip(poseStack, gridRenderer, drawX, drawY, (int) ((float) this.locatorTexture.getWidth() * (this.drawScale / 2.0F)), (int) ((float) this.locatorTexture.getHeight() * (this.drawScale / 2.0F)));
            }
            int labelOffset = this.entityTexture == null ? 8 : (rotation == 0.0 ? this.entityTexture.getHeight() : -this.entityTexture.getHeight());
            if (pass == DrawStep.Pass.Text && this.showName && this.customName != null) {
                labelOffset = this.useDots ? (int) ((double) labelOffset * (double) this.drawScale * 0.4) : labelOffset;
                Point2D labelPoint = gridRenderer.shiftWindowPosition(drawX, drawY, 0, Math.max(labelOffset, 16));
                int color = this.customName.getStyle().getColor() != null ? this.customName.getStyle().getColor().getValue() : 16777215;
                DrawUtil.drawBatchLabel(poseStack, this.customName, buffers, labelPoint.getX(), labelPoint.getY(), DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, labelBg, 180.0F, color, 225.0F, fontScale, false, rotation);
            }
            if (pass == DrawStep.Pass.Object && this.entityTexture != null) {
                if (this.useDots) {
                    boolean flip = false;
                    this.elevationOffset = (int) (DataCache.getPlayer().posY - entityLiving.m_20186_());
                    if (this.elevationOffset < -1 || this.elevationOffset > 1) {
                        flip = this.elevationOffset < -1;
                        DrawUtil.drawColoredEntity(poseStack, drawX, drawY, this.entityTexture, this.color, alpha, this.drawScale, flip ? -rotation + 180.0 : -rotation);
                        this.drawEntityTooltip(poseStack, gridRenderer, drawX, drawY, this.entityTexture.getWidth(), this.entityTexture.getHeight());
                    }
                } else {
                    if (this.drawScale > 1.0F) {
                        this.entityTexture = this.entityTexture.getScaledImage(this.drawScale);
                        this.drawScale = 1.0F;
                    }
                    DrawUtil.drawEntity(poseStack, drawX, drawY, -rotation, this.entityTexture, alpha, this.drawScale, 0.0);
                    this.drawEntityTooltip(poseStack, gridRenderer, drawX, drawY, this.entityTexture.getWidth(), this.entityTexture.getHeight());
                }
            }
        }
    }

    public Double getPosition(double xOffset, double yOffset, GridRenderer gridRenderer, boolean forceUpdate) {
        double x = this.entityDTO.posX;
        double z = this.entityDTO.posZ;
        double halfBlock = Math.pow(2.0, (double) gridRenderer.getZoom()) / 2.0;
        Double pixel = gridRenderer.getBlockPixelInGrid(x, z);
        pixel.setLocation(pixel.getX() + halfBlock + xOffset, pixel.getY() + halfBlock + yOffset);
        return pixel;
    }

    @Override
    public int getDisplayOrder() {
        return this.customName != null ? 1 : 0;
    }

    @Override
    public String getModId() {
        return "journeymap";
    }

    public static class SimpleCacheLoader extends CacheLoader<LivingEntity, DrawEntityStep> {

        public DrawEntityStep load(LivingEntity entityLiving) throws Exception {
            return new DrawEntityStep(entityLiving);
        }
    }
}