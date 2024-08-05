package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public abstract class EntityRenderer<T extends Entity> {

    protected static final float NAMETAG_SCALE = 0.025F;

    protected final EntityRenderDispatcher entityRenderDispatcher;

    private final Font font;

    protected float shadowRadius;

    protected float shadowStrength = 1.0F;

    protected EntityRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        this.entityRenderDispatcher = entityRendererProviderContext0.getEntityRenderDispatcher();
        this.font = entityRendererProviderContext0.getFont();
    }

    public final int getPackedLightCoords(T t0, float float1) {
        BlockPos $$2 = BlockPos.containing(t0.getLightProbePosition(float1));
        return LightTexture.pack(this.getBlockLightLevel(t0, $$2), this.getSkyLightLevel(t0, $$2));
    }

    protected int getSkyLightLevel(T t0, BlockPos blockPos1) {
        return t0.level().m_45517_(LightLayer.SKY, blockPos1);
    }

    protected int getBlockLightLevel(T t0, BlockPos blockPos1) {
        return t0.isOnFire() ? 15 : t0.level().m_45517_(LightLayer.BLOCK, blockPos1);
    }

    public boolean shouldRender(T t0, Frustum frustum1, double double2, double double3, double double4) {
        if (!t0.shouldRender(double2, double3, double4)) {
            return false;
        } else if (t0.noCulling) {
            return true;
        } else {
            AABB $$5 = t0.getBoundingBoxForCulling().inflate(0.5);
            if ($$5.hasNaN() || $$5.getSize() == 0.0) {
                $$5 = new AABB(t0.getX() - 2.0, t0.getY() - 2.0, t0.getZ() - 2.0, t0.getX() + 2.0, t0.getY() + 2.0, t0.getZ() + 2.0);
            }
            return frustum1.isVisible($$5);
        }
    }

    public Vec3 getRenderOffset(T t0, float float1) {
        return Vec3.ZERO;
    }

    public void render(T t0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        if (this.shouldShowName(t0)) {
            this.renderNameTag(t0, t0.getDisplayName(), poseStack3, multiBufferSource4, int5);
        }
    }

    protected boolean shouldShowName(T t0) {
        return t0.shouldShowName() && t0.hasCustomName();
    }

    public abstract ResourceLocation getTextureLocation(T var1);

    public Font getFont() {
        return this.font;
    }

    protected void renderNameTag(T t0, Component component1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4) {
        double $$5 = this.entityRenderDispatcher.distanceToSqr(t0);
        if (!($$5 > 4096.0)) {
            boolean $$6 = !t0.isDiscrete();
            float $$7 = t0.getNameTagOffsetY();
            int $$8 = "deadmau5".equals(component1.getString()) ? -10 : 0;
            poseStack2.pushPose();
            poseStack2.translate(0.0F, $$7, 0.0F);
            poseStack2.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack2.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f $$9 = poseStack2.last().pose();
            float $$10 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int $$11 = (int) ($$10 * 255.0F) << 24;
            Font $$12 = this.getFont();
            float $$13 = (float) (-$$12.width(component1) / 2);
            $$12.drawInBatch(component1, $$13, (float) $$8, 553648127, false, $$9, multiBufferSource3, $$6 ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, $$11, int4);
            if ($$6) {
                $$12.drawInBatch(component1, $$13, (float) $$8, -1, false, $$9, multiBufferSource3, Font.DisplayMode.NORMAL, 0, int4);
            }
            poseStack2.popPose();
        }
    }
}