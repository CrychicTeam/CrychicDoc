package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelCaveCentipede;
import com.github.alexthe666.alexsmobs.entity.EntityCentipedeBody;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;

public class RenderCentipedeBody extends MobRenderer<EntityCentipedeBody, AdvancedEntityModel<EntityCentipedeBody>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/cave_centipede.png");

    public RenderCentipedeBody(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelCaveCentipede<>(1), 0.5F);
    }

    protected float getFlipDegrees(EntityCentipedeBody centipede) {
        return 180.0F;
    }

    protected void setupRotations(EntityCentipedeBody entity, PoseStack stack, float pitchIn, float yawIn, float partialTickTime) {
        float newYaw = entity.f_20885_;
        if (this.m_5936_(entity)) {
            newYaw += (float) (Math.cos((double) entity.f_19797_ * 3.25) * Math.PI * 0.4F);
        }
        Pose pose = entity.m_20089_();
        if (pose != Pose.SLEEPING) {
            stack.mulPose(Axis.YP.rotationDegrees(180.0F - newYaw));
            stack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
        }
        if (entity.f_20919_ > 0) {
            float f = ((float) entity.f_20919_ + partialTickTime - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }
            stack.translate(0.0F, f * 1.15F, 0.0F);
            stack.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees(entity)));
        } else if (entity.m_8077_()) {
            String s = ChatFormatting.stripFormatting(entity.m_7755_().getString());
            if ("Dinnerbone".equals(s) || "Grumm".equals(s)) {
                stack.translate(0.0, (double) (entity.m_20206_() + 0.1F), 0.0);
                stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            }
        }
    }

    public ResourceLocation getTextureLocation(EntityCentipedeBody entity) {
        return TEXTURE;
    }
}