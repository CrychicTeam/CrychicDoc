package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelAnaconda;
import com.github.alexthe666.alexsmobs.entity.EntityAnacondaPart;
import com.github.alexthe666.alexsmobs.entity.util.AnacondaPartIndex;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;

public class RenderAnacondaPart extends LivingEntityRenderer<EntityAnacondaPart, AdvancedEntityModel<EntityAnacondaPart>> {

    private final ModelAnaconda<EntityAnacondaPart> neckModel = new ModelAnaconda<>(AnacondaPartIndex.NECK);

    private final ModelAnaconda<EntityAnacondaPart> bodyModel = new ModelAnaconda<>(AnacondaPartIndex.BODY);

    private final ModelAnaconda<EntityAnacondaPart> tailModel = new ModelAnaconda<>(AnacondaPartIndex.TAIL);

    public RenderAnacondaPart(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelAnaconda<>(AnacondaPartIndex.NECK), 0.3F);
    }

    protected void setupRotations(EntityAnacondaPart entity, PoseStack stack, float pitchIn, float yawIn, float partialTickTime) {
        float newYaw = entity.f_20885_;
        if (this.m_5936_(entity)) {
            newYaw += (float) (Math.cos((double) entity.f_19797_ * 3.25) * Math.PI * 0.4F);
        }
        Pose pose = entity.m_20089_();
        if (pose != Pose.SLEEPING) {
            stack.mulPose(Axis.YP.rotationDegrees(180.0F - newYaw));
            stack.mulPose(Axis.XP.rotationDegrees(entity.m_146909_()));
        }
        if (entity.f_20919_ > 0) {
            float f = ((float) entity.f_20919_ + partialTickTime - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }
            stack.mulPose(Axis.ZP.rotationDegrees(f * this.m_6441_(entity)));
        } else if (entity.m_8077_()) {
            String s = ChatFormatting.stripFormatting(entity.m_7755_().getString());
            if ("Dinnerbone".equals(s) || "Grumm".equals(s)) {
                stack.translate(0.0, (double) (entity.m_20206_() + 0.1F), 0.0);
                stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            }
        }
    }

    protected boolean shouldShowName(EntityAnacondaPart entity) {
        return super.shouldShowName(entity) && (entity.m_6052_() || entity.m_8077_() && entity == this.f_114476_.crosshairPickEntity);
    }

    protected void scale(EntityAnacondaPart entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        this.f_115290_ = this.getModelForType(entitylivingbaseIn.getPartType());
        matrixStackIn.scale(entitylivingbaseIn.m_6134_(), entitylivingbaseIn.m_6134_(), entitylivingbaseIn.m_6134_());
    }

    private AdvancedEntityModel<EntityAnacondaPart> getModelForType(AnacondaPartIndex partType) {
        switch(partType) {
            case BODY:
                return this.bodyModel;
            case NECK:
                return this.neckModel;
            case TAIL:
                return this.tailModel;
            default:
                return this.bodyModel;
        }
    }

    public ResourceLocation getTextureLocation(EntityAnacondaPart entity) {
        return RenderAnaconda.getAnacondaTexture(entity.isYellow(), entity.isShedding());
    }
}