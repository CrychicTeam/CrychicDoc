package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelSugarGlider;
import com.github.alexthe666.alexsmobs.entity.EntitySugarGlider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;

public class RenderSugarGlider extends MobRenderer<EntitySugarGlider, ModelSugarGlider> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/sugar_glider.png");

    public RenderSugarGlider(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelSugarGlider(), 0.35F);
    }

    private Direction rotate(Direction attachmentFacing) {
        return attachmentFacing.getAxis() == Direction.Axis.Y ? Direction.UP : attachmentFacing;
    }

    protected void setupRotations(EntitySugarGlider entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entityLiving.m_20159_()) {
            super.m_7523_(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        } else {
            if (this.m_5936_(entityLiving)) {
                rotationYaw += (float) (Math.cos((double) entityLiving.f_19797_ * 3.25) * Math.PI * 0.4F);
            }
            float trans = entityLiving.m_6162_() ? 0.2F : 0.4F;
            Pose pose = entityLiving.m_20089_();
            if (pose != Pose.SLEEPING) {
                float prevProg = entityLiving.prevAttachChangeProgress + (entityLiving.attachChangeProgress - entityLiving.prevAttachChangeProgress) * partialTicks;
                float yawMul = 0.0F;
                if (entityLiving.prevAttachDir == entityLiving.getAttachmentFacing() && entityLiving.getAttachmentFacing().getAxis() == Direction.Axis.Y) {
                    yawMul = 1.0F;
                }
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - yawMul * rotationYaw));
                if (entityLiving.getAttachmentFacing() == Direction.DOWN) {
                    matrixStackIn.translate(0.0, (double) trans, 0.0);
                    if (entityLiving.f_19855_ <= entityLiving.m_20186_()) {
                        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F * prevProg));
                    } else {
                        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F * prevProg));
                    }
                    matrixStackIn.translate(0.0, (double) (-trans), 0.0);
                }
                matrixStackIn.translate(0.0, (double) trans, 0.0);
                Quaternionf current = this.rotate(entityLiving.getAttachmentFacing()).getRotation();
                current.mul(1.0F - prevProg);
                matrixStackIn.mulPose(current);
                matrixStackIn.translate(0.0, (double) (-trans), 0.0);
            }
            if (entityLiving.f_20919_ > 0) {
                float f = ((float) entityLiving.f_20919_ + partialTicks - 1.0F) / 20.0F * 1.6F;
                f = Mth.sqrt(f);
                if (f > 1.0F) {
                    f = 1.0F;
                }
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(f * this.m_6441_(entityLiving)));
            } else if (entityLiving.m_21209_()) {
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F - entityLiving.m_146909_()));
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(((float) entityLiving.f_19797_ + partialTicks) * -75.0F));
            } else if (entityLiving.m_8077_()) {
                String s = ChatFormatting.stripFormatting(entityLiving.m_7755_().getString());
                if ("Dinnerbone".equals(s) || "Grumm".equals(s)) {
                    matrixStackIn.translate(0.0, (double) (entityLiving.m_20206_() + 0.1F), 0.0);
                    matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
                }
            }
        }
    }

    protected void scale(EntitySugarGlider mob, PoseStack matrixStackIn, float partialTickTime) {
        if (mob.m_20159_() && mob.m_20202_() != null && mob.m_20202_() instanceof Player) {
            Player mount = (Player) mob.m_20202_();
            EntityRenderer playerRender = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(mount);
            if ((Minecraft.getInstance().player != mount || Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON) && playerRender instanceof LivingEntityRenderer && ((LivingEntityRenderer) playerRender).getModel() instanceof HumanoidModel) {
                matrixStackIn.translate(0.0F, 0.5F, 0.0F);
                ((HumanoidModel) ((LivingEntityRenderer) playerRender).getModel()).head.translateAndRotate(matrixStackIn);
                matrixStackIn.translate(0.0F, -0.5F, 0.0F);
            }
        }
    }

    public ResourceLocation getTextureLocation(EntitySugarGlider entity) {
        return TEXTURE;
    }
}