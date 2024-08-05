package net.mehvahdjukaar.supplementaries.client.renderers.entities.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class SlimedLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public SlimedLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        int i = entity.f_19797_;
        float f = ((float) ((long) i % 2000L) + partialTicks) / 20000.0F;
        float f1 = 0.5F + Mth.sin((float) ((double) f * Math.PI)) * 0.3F;
        matrixStackIn.pushPose();
        matrixStackIn.popPose();
    }
}