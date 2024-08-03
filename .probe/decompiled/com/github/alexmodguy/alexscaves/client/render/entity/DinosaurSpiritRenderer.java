package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.GrottoceratopsModel;
import com.github.alexmodguy.alexscaves.client.model.SubterranodonModel;
import com.github.alexmodguy.alexscaves.client.model.TremorsaurusModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.item.DinosaurSpiritEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class DinosaurSpiritRenderer extends EntityRenderer<DinosaurSpiritEntity> {

    private static final ResourceLocation SUBTERRANODON_TEXTURE = new ResourceLocation("alexscaves", "textures/entity/subterranodon.png");

    private static final ResourceLocation TREMORSAURUS_TEXTURE = new ResourceLocation("alexscaves", "textures/entity/tremorsaurus.png");

    private static final ResourceLocation GROTTOCERATOPS_TEXTURE = new ResourceLocation("alexscaves", "textures/entity/grottoceratops.png");

    private static final SubterranodonModel SUBTERRANODON_MODEL = new SubterranodonModel();

    private static final TremorsaurusModel TREMORSAURUS_MODEL = new TremorsaurusModel();

    private static final GrottoceratopsModel GROTTOCERATOPS_MODEL = new GrottoceratopsModel();

    public DinosaurSpiritRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(DinosaurSpiritEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        poseStack.pushPose();
        poseStack.translate(0.0, 1.5, 0.0);
        if (entityIn.getDinosaurType() == DinosaurSpiritEntity.DinosaurType.GROTTOCERATOPS) {
            Player player = entityIn.getUsingPlayer();
            if (player != null) {
                Vec3 playerPos = player.m_20318_(partialTicks);
                Vec3 dinoPos = entityIn.m_20318_(partialTicks);
                double d1 = playerPos.z - dinoPos.z;
                double d2 = playerPos.x - dinoPos.x;
                float f = -((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI);
                poseStack.mulPose(Axis.YP.rotationDegrees(-f));
            }
        } else {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityIn.m_5675_(partialTicks)));
        }
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.XN.rotationDegrees(entityIn.m_5686_(partialTicks)));
        float ghostAlpha = entityIn.getFadeIn(partialTicks);
        switch(entityIn.getDinosaurType()) {
            case SUBTERRANODON:
                {
                    boolean prevBaby = SUBTERRANODON_MODEL.f_102610_;
                    SUBTERRANODON_MODEL.f_102610_ = false;
                    VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getRedGhost(SUBTERRANODON_TEXTURE));
                    SUBTERRANODON_MODEL.animateSpirit(entityIn, partialTicks);
                    SUBTERRANODON_MODEL.renderToBuffer(poseStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, ghostAlpha);
                    SUBTERRANODON_MODEL.f_102610_ = prevBaby;
                    break;
                }
            case GROTTOCERATOPS:
                {
                    boolean prevBaby = GROTTOCERATOPS_MODEL.f_102610_;
                    GROTTOCERATOPS_MODEL.f_102610_ = false;
                    VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getRedGhost(GROTTOCERATOPS_TEXTURE));
                    GROTTOCERATOPS_MODEL.animateSpirit(entityIn, partialTicks);
                    GROTTOCERATOPS_MODEL.renderSpiritToBuffer(poseStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, ghostAlpha);
                    GROTTOCERATOPS_MODEL.f_102610_ = prevBaby;
                    break;
                }
            case TREMORSAURUS:
                {
                    boolean prevBaby = TREMORSAURUS_MODEL.f_102610_;
                    TREMORSAURUS_MODEL.f_102610_ = false;
                    VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getRedGhost(TREMORSAURUS_TEXTURE));
                    TREMORSAURUS_MODEL.animateSpirit(entityIn, partialTicks);
                    TREMORSAURUS_MODEL.renderSpiritToBuffer(poseStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, ghostAlpha);
                    TREMORSAURUS_MODEL.f_102610_ = prevBaby;
                }
        }
        poseStack.popPose();
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(DinosaurSpiritEntity entity) {
        return SUBTERRANODON_TEXTURE;
    }
}