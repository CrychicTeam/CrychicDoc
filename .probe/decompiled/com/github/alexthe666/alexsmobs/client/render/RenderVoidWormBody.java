package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelVoidWormBody;
import com.github.alexthe666.alexsmobs.client.model.ModelVoidWormTail;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerVoidWormGlow;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWormPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;

public class RenderVoidWormBody extends LivingEntityRenderer<EntityVoidWormPart, EntityModel<EntityVoidWormPart>> {

    private static final ResourceLocation TEXTURE_BODY = new ResourceLocation("alexsmobs:textures/entity/void_worm/void_worm_body.png");

    private static final ResourceLocation TEXTURE_BODY_HURT = new ResourceLocation("alexsmobs:textures/entity/void_worm/void_worm_body_hurt.png");

    private static final ResourceLocation TEXTURE_BODY_GLOW = new ResourceLocation("alexsmobs:textures/entity/void_worm/void_worm_body_glow.png");

    private static final ResourceLocation TEXTURE_TAIL = new ResourceLocation("alexsmobs:textures/entity/void_worm/void_worm_tail.png");

    private static final ResourceLocation TEXTURE_TAIL_HURT = new ResourceLocation("alexsmobs:textures/entity/void_worm/void_worm_tail_hurt.png");

    private static final ResourceLocation TEXTURE_TAIL_GLOW = new ResourceLocation("alexsmobs:textures/entity/void_worm/void_worm_tail_glow.png");

    private final ModelVoidWormBody bodyModel = new ModelVoidWormBody(0.0F);

    private final ModelVoidWormTail tailModel = new ModelVoidWormTail(0.0F);

    public RenderVoidWormBody(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelVoidWormBody(0.0F), 1.0F);
        this.m_115326_(new LayerVoidWormGlow(this, renderManagerIn.getResourceManager(), new ModelVoidWormBody(0.0F)) {

            @Override
            public ResourceLocation getGlowTexture(LivingEntity worm) {
                return ((EntityVoidWormPart) worm).isTail() ? RenderVoidWormBody.TEXTURE_TAIL_GLOW : RenderVoidWormBody.TEXTURE_BODY_GLOW;
            }

            @Override
            public boolean isGlowing(LivingEntity worm) {
                return !((EntityVoidWormPart) worm).isHurt();
            }

            @Override
            public float getAlpha(LivingEntity livingEntity) {
                EntityVoidWormPart worm = (EntityVoidWormPart) livingEntity;
                return (float) Mth.clamp(((double) worm.m_21223_() - worm.getHealthThreshold()) / ((double) worm.m_21233_() - worm.getHealthThreshold()), 0.0, 1.0);
            }
        });
    }

    public boolean shouldRender(EntityVoidWormPart worm, Frustum camera, double camX, double camY, double camZ) {
        return worm.getPortalTicks() <= 0 && super.m_5523_(worm, camera, camX, camY, camZ);
    }

    public ResourceLocation getTextureLocation(EntityVoidWormPart entity) {
        if (entity.isHurt()) {
            return entity.isTail() ? TEXTURE_TAIL_HURT : TEXTURE_BODY_HURT;
        } else {
            return entity.isTail() ? TEXTURE_TAIL : TEXTURE_BODY;
        }
    }

    protected void setupRotations(EntityVoidWormPart entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        Pose pose = entityLiving.m_20089_();
        if (pose != Pose.SLEEPING) {
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - entityLiving.getWormYaw(partialTicks)));
        }
        if (entityLiving.f_20919_ > 0) {
            float f = ((float) entityLiving.f_20919_ + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(f * this.m_6441_(entityLiving)));
        }
    }

    protected void scale(EntityVoidWormPart entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        this.f_115290_ = (EntityModel) (entitylivingbaseIn.isTail() ? this.tailModel : this.bodyModel);
        matrixStackIn.scale(entitylivingbaseIn.getWormScale(), entitylivingbaseIn.getWormScale(), entitylivingbaseIn.getWormScale());
    }

    protected boolean shouldShowName(EntityVoidWormPart entity) {
        return super.shouldShowName(entity) && (entity.m_6052_() || entity.m_8077_() && entity == this.f_114476_.crosshairPickEntity);
    }
}