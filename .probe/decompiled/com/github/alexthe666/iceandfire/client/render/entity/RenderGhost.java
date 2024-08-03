package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelGhost;
import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderGhost extends MobRenderer<EntityGhost, ModelGhost> {

    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_white.png");

    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_blue.png");

    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_green.png");

    public static final ResourceLocation TEXTURE_SHOPPING_LIST = new ResourceLocation("iceandfire:textures/models/ghost/haunted_shopping_list.png");

    public RenderGhost(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelGhost(0.0F), 0.55F);
    }

    public static ResourceLocation getGhostOverlayForType(int ghost) {
        switch(ghost) {
            case -1:
                return TEXTURE_SHOPPING_LIST;
            case 0:
            default:
                return TEXTURE_0;
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
        }
    }

    public void render(@NotNull EntityGhost entityIn, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        this.f_114477_ = 0.0F;
        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre<>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn))) {
            matrixStackIn.pushPose();
            ((ModelGhost) this.f_115290_).f_102608_ = this.m_115342_(entityIn, partialTicks);
            boolean shouldSit = entityIn.m_20159_() && entityIn.m_20202_() != null && entityIn.m_20202_().shouldRiderSit();
            ((ModelGhost) this.f_115290_).f_102609_ = shouldSit;
            ((ModelGhost) this.f_115290_).f_102610_ = entityIn.m_6162_();
            float f = Mth.rotLerp(partialTicks, entityIn.f_20884_, entityIn.f_20883_);
            float f1 = Mth.rotLerp(partialTicks, entityIn.f_20886_, entityIn.f_20885_);
            float f2 = f1 - f;
            if (shouldSit && entityIn.m_20202_() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entityIn.m_20202_();
                f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
                f2 = f1 - f;
                float f3 = Mth.wrapDegrees(f2);
                if (f3 < -85.0F) {
                    f3 = -85.0F;
                }
                if (f3 >= 85.0F) {
                    f3 = 85.0F;
                }
                f = f1 - f3;
                if (f3 * f3 > 2500.0F) {
                    f += f3 * 0.2F;
                }
                f2 = f1 - f;
            }
            float f6 = Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_());
            if (entityIn.m_20089_() == Pose.SLEEPING) {
                Direction direction = entityIn.m_21259_();
                if (direction != null) {
                    float f4 = entityIn.m_20236_(Pose.STANDING) - 0.1F;
                    matrixStackIn.translate((double) ((float) (-direction.getStepX()) * f4), 0.0, (double) ((float) (-direction.getStepZ()) * f4));
                }
            }
            float f7 = this.m_6930_(entityIn, partialTicks);
            this.m_7523_(entityIn, matrixStackIn, f7, f, partialTicks);
            matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
            this.scale(entityIn, matrixStackIn, partialTicks);
            matrixStackIn.translate(0.0, -1.501F, 0.0);
            float f8 = 0.0F;
            float f5 = 0.0F;
            if (!shouldSit && entityIn.m_6084_()) {
                f8 = entityIn.f_267362_.speed();
                f5 = entityIn.f_267362_.position();
                if (entityIn.m_6162_()) {
                    f5 *= 3.0F;
                }
                if (f8 > 1.0F) {
                    f8 = 1.0F;
                }
            }
            ((ModelGhost) this.f_115290_).m_6839_(entityIn, f5, f8, partialTicks);
            ((ModelGhost) this.f_115290_).setupAnim(entityIn, f5, f8, f7, f2, f6);
            float alphaForRender = this.getAlphaForRender(entityIn, partialTicks);
            RenderType rendertype = entityIn.isDaytimeMode() ? IafRenderType.getGhostDaytime(this.getTextureLocation(entityIn)) : IafRenderType.getGhost(this.getTextureLocation(entityIn));
            if (rendertype != null && !entityIn.m_20145_()) {
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
                int i = m_115338_(entityIn, this.m_6931_(entityIn, partialTicks));
                if (entityIn.isHauntedShoppingList()) {
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(0.0F, 0.8F + Mth.sin(((float) entityIn.f_19797_ + partialTicks) * 0.15F) * 0.1F, 0.0F);
                    matrixStackIn.scale(0.6F, 0.6F, 0.6F);
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
                    matrixStackIn.pushPose();
                    PoseStack.Pose matrixstack$entry = matrixStackIn.last();
                    Matrix4f matrix4f = matrixstack$entry.pose();
                    Matrix3f matrix3f = matrixstack$entry.normal();
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255.0F), -1, -2, 0, 1.0F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255.0F), 1, -2, 0, 0.5F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255.0F), 1, 2, 0, 0.5F, 1.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255.0F), -1, 2, 0, 1.0F, 1.0F, 0, 1, 0, 240);
                    matrixStackIn.popPose();
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
                    matrixStackIn.pushPose();
                    matrixstack$entry = matrixStackIn.last();
                    matrix4f = matrixstack$entry.pose();
                    matrix3f = matrixstack$entry.normal();
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255.0F), -1, -2, 0, 0.0F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255.0F), 1, -2, 0, 0.5F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255.0F), 1, 2, 0, 0.5F, 1.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255.0F), -1, 2, 0, 0.0F, 1.0F, 0, 1, 0, 240);
                    matrixStackIn.popPose();
                    matrixStackIn.popPose();
                } else {
                    ((ModelGhost) this.f_115290_).m_7695_(matrixStackIn, ivertexbuilder, 240, i, 1.0F, 1.0F, 1.0F, alphaForRender);
                }
            }
            if (!entityIn.m_5833_()) {
                for (RenderLayer<EntityGhost, ModelGhost> layerrenderer : this.f_115291_) {
                    layerrenderer.render(matrixStackIn, bufferIn, packedLightIn, entityIn, f5, f8, partialTicks, f7, f2, f6);
                }
            }
            matrixStackIn.popPose();
            RenderNameTagEvent renderNameplateEvent = new RenderNameTagEvent(entityIn, entityIn.m_5446_(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
            MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
            if (renderNameplateEvent.getResult() != Result.DENY && (renderNameplateEvent.getResult() == Result.ALLOW || this.m_6512_(entityIn))) {
                this.m_7649_(entityIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
            }
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post<>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn));
        }
    }

    protected float getFlipDegrees(@NotNull EntityGhost ghost) {
        return 0.0F;
    }

    public float getAlphaForRender(EntityGhost entityIn, float partialTicks) {
        return entityIn.isDaytimeMode() ? Mth.clamp((float) (101 - Math.min(entityIn.getDaytimeCounter(), 100)) / 100.0F, 0.0F, 1.0F) : Mth.clamp((Mth.sin(((float) entityIn.f_19797_ + partialTicks) * 0.1F) + 1.0F) * 0.5F + 0.1F, 0.0F, 1.0F);
    }

    public void scale(@NotNull EntityGhost LivingEntityIn, @NotNull PoseStack stack, float partialTickTime) {
    }

    @NotNull
    public ResourceLocation getTextureLocation(EntityGhost ghost) {
        switch(ghost.getColor()) {
            case -1:
                return TEXTURE_SHOPPING_LIST;
            case 0:
            default:
                return TEXTURE_0;
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
        }
    }

    public void drawVertex(Matrix4f stack, Matrix3f normal, VertexConsumer builder, int packedRed, int alphaInt, int x, int y, int z, float u, float v, int lightmap, int lightmap3, int lightmap2, int lightmap4) {
        builder.vertex(stack, (float) x, (float) y, (float) z).color(255, 255, 255, alphaInt).uv(u, v).overlayCoords(packedRed).uv2(lightmap4).normal(normal, (float) lightmap, (float) lightmap2, (float) lightmap3).endVertex();
    }
}