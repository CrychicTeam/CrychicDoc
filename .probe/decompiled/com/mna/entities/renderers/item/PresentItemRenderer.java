package com.mna.entities.renderers.item;

import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;

public class PresentItemRenderer extends ItemEntityRenderer {

    public PresentItemRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ItemEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        int[] innerColor = new int[] { 156, 56, 207 };
        int[] outerColor = new int[] { 42, 90, 173 };
        int preRadiantSize = matrixStackIn.poseStack.size();
        matrixStackIn.pushPose();
        BakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getModel(entityIn.getItem(), entityIn.m_9236_(), (LivingEntity) null, 0);
        float f1 = Mth.sin(((float) entityIn.getAge() + partialTicks) / 10.0F + entityIn.bobOffs) * 0.1F + 0.1F;
        float f2 = this.shouldBob() ? ibakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y() : 0.0F;
        matrixStackIn.translate(0.0, (double) (f1 + 0.5F * f2), 0.0);
        WorldRenderUtils.renderRadiant(entityIn, matrixStackIn, bufferIn, innerColor, outerColor, 128, 0.1F);
        while (matrixStackIn.poseStack.size() > preRadiantSize) {
            matrixStackIn.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}