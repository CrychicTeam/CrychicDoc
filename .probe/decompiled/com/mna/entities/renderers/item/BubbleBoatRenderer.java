package com.mna.entities.renderers.item;

import com.mna.entities.constructs.BubbleBoat;
import com.mna.entities.models.BubbleBoatModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BubbleBoatRenderer extends EntityRenderer<BubbleBoat> {

    private static final ResourceLocation BUBBLE_BOAT_TEXTURE = new ResourceLocation("mna", "textures/entity/bubble_boat.png");

    private static final ResourceLocation BRIMSTONE_BOAT_TEXTURE = new ResourceLocation("mna", "textures/entity/brimstone_boat.png");

    protected final BubbleBoatModel<BubbleBoat> modelBoat = new BubbleBoatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BubbleBoatModel.LAYER_LOCATION));

    public BubbleBoatRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.f_114477_ = 0.8F;
    }

    public void render(BubbleBoat entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0, -2.0, 0.0);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        float f = (float) entityIn.m_38385_() - partialTicks;
        float f1 = entityIn.m_38384_() - partialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }
        if (f > 0.0F) {
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float) entityIn.m_38386_()));
        }
        float f2 = entityIn.m_38352_(partialTicks);
        if (!Mth.equal(f2, 0.0F)) {
            Quaternionf quat = new Quaternionf();
            quat = quat.rotateAxis(entityIn.m_38352_(partialTicks), new Vector3f(1.0F, 0.0F, 1.0F));
            matrixStackIn.mulPose(quat);
        }
        matrixStackIn.scale(3.0F, 3.0F, 3.0F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
        this.modelBoat.setupAnim(entityIn, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.modelBoat.m_103119_(this.getTextureLocation(entityIn)));
        this.modelBoat.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(BubbleBoat entity) {
        return entity.isBrimstone() ? BRIMSTONE_BOAT_TEXTURE : BUBBLE_BOAT_TEXTURE;
    }
}