package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelAncientDart;
import com.github.alexthe666.alexsmobs.entity.EntityTossedItem;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;

public class RenderTossedItem extends EntityRenderer<EntityTossedItem> {

    public static final ResourceLocation DART_TEXTURE = new ResourceLocation("alexsmobs:textures/entity/ancient_dart.png");

    public static final ModelAncientDart DART_MODEL = new ModelAncientDart();

    public RenderTossedItem(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    public ResourceLocation getTextureLocation(EntityTossedItem entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    public void render(EntityTossedItem entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        if (entityIn.isDart()) {
            matrixStackIn.translate(0.0, -0.15F, 0.0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) - 180.0F));
            matrixStackIn.pushPose();
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
            matrixStackIn.translate(0.0F, 0.5F, 0.0F);
            matrixStackIn.scale(1.0F, 1.0F, 1.0F);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(DART_MODEL.m_103119_(DART_TEXTURE));
            DART_MODEL.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        } else {
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) - 90.0F));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
            matrixStackIn.translate(0.0F, 0.5F, 0.0F);
            matrixStackIn.scale(1.0F, 1.0F, 1.0F);
            matrixStackIn.mulPose(new Quaternionf().rotateZ(Maths.rad((double) (-((float) entityIn.f_19797_ + partialTicks) * 30.0F))));
            matrixStackIn.translate(0.0F, -0.15F, 0.0F);
            Minecraft.getInstance().getItemRenderer().renderStatic(entityIn.m_7846_(), ItemDisplayContext.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entityIn.m_9236_(), 0);
        }
        matrixStackIn.popPose();
    }
}