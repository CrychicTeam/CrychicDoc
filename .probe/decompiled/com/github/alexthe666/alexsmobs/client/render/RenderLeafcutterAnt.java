package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelLeafcutterAnt;
import com.github.alexthe666.alexsmobs.client.model.ModelLeafcutterAntQueen;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerLeafcutterAntLeaf;
import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;

public class RenderLeafcutterAnt extends MobRenderer<EntityLeafcutterAnt, AdvancedEntityModel<EntityLeafcutterAnt>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/leafcutter_ant.png");

    private static final ResourceLocation TEXTURE_QUEEN = new ResourceLocation("alexsmobs:textures/entity/leafcutter_ant_queen.png");

    private static final ResourceLocation TEXTURE_ANGRY = new ResourceLocation("alexsmobs:textures/entity/leafcutter_ant_angry.png");

    private static final ResourceLocation TEXTURE_QUEEN_ANGRY = new ResourceLocation("alexsmobs:textures/entity/leafcutter_ant_queen_angry.png");

    private final ModelLeafcutterAnt modelAnt = new ModelLeafcutterAnt();

    private final ModelLeafcutterAntQueen modelQueen = new ModelLeafcutterAntQueen();

    public RenderLeafcutterAnt(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelLeafcutterAnt(), 0.25F);
        this.m_115326_(new LayerLeafcutterAntLeaf(this));
    }

    protected void setupRotations(EntityLeafcutterAnt entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        if (this.m_5936_(entityLiving)) {
            rotationYaw += (float) (Math.cos((double) entityLiving.f_19797_ * 3.25) * Math.PI * 0.4F);
        }
        float trans = entityLiving.m_6162_() ? 0.25F : 0.5F;
        Pose pose = entityLiving.m_20089_();
        if (pose != Pose.SLEEPING) {
            float progresso = 1.0F - (entityLiving.prevAttachChangeProgress + (entityLiving.attachChangeProgress - entityLiving.prevAttachChangeProgress) * partialTicks);
            if (entityLiving.getAttachmentFacing() == Direction.DOWN) {
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
                matrixStackIn.translate(0.0, (double) trans, 0.0);
                if (entityLiving.f_19855_ < entityLiving.m_20186_()) {
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F * (1.0F - progresso)));
                } else {
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F * (1.0F - progresso)));
                }
                matrixStackIn.translate(0.0, (double) (-trans), 0.0);
            } else if (entityLiving.getAttachmentFacing() == Direction.UP) {
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
                matrixStackIn.translate(0.0, (double) (-trans), 0.0);
            } else {
                matrixStackIn.translate(0.0, (double) trans, 0.0);
                switch(entityLiving.getAttachmentFacing()) {
                    case NORTH:
                        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F * progresso));
                        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(0.0F));
                        break;
                    case SOUTH:
                        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
                        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F * progresso));
                        break;
                    case WEST:
                        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
                        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F - 90.0F * progresso));
                        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-90.0F));
                        break;
                    case EAST:
                        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
                        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F * progresso - 90.0F));
                        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(90.0F));
                }
                if (entityLiving.m_20184_().y <= -0.001F) {
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(-180.0F));
                }
                matrixStackIn.translate(0.0, (double) (-trans), 0.0);
            }
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
        } else if (pose != Pose.SLEEPING && entityLiving.m_8077_()) {
            String s = ChatFormatting.stripFormatting(entityLiving.m_7755_().getString());
            if ("Dinnerbone".equals(s) || "Grumm".equals(s)) {
                matrixStackIn.translate(0.0, (double) (entityLiving.m_20206_() + 0.1F), 0.0);
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
            }
        }
    }

    protected void scale(EntityLeafcutterAnt entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        this.f_115290_ = (EntityModel) (entitylivingbaseIn.isQueen() ? this.modelQueen : this.modelAnt);
    }

    public ResourceLocation getTextureLocation(EntityLeafcutterAnt entity) {
        if (entity.getRemainingPersistentAngerTime() > 0) {
            return entity.isQueen() ? TEXTURE_QUEEN_ANGRY : TEXTURE_ANGRY;
        } else {
            return entity.isQueen() ? TEXTURE_QUEEN : TEXTURE;
        }
    }
}