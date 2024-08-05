package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Saddleable;

public class SaddleLayer<T extends Entity & Saddleable, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final ResourceLocation textureLocation;

    private final M model;

    public SaddleLayer(RenderLayerParent<T, M> renderLayerParentTM0, M m1, ResourceLocation resourceLocation2) {
        super(renderLayerParentTM0);
        this.model = m1;
        this.textureLocation = resourceLocation2;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (t3.isSaddled()) {
            this.m_117386_().copyPropertiesTo(this.model);
            this.model.prepareMobModel(t3, float4, float5, float6);
            this.model.setupAnim(t3, float4, float5, float7, float8, float9);
            VertexConsumer $$10 = multiBufferSource1.getBuffer(RenderType.entityCutoutNoCull(this.textureLocation));
            this.model.m_7695_(poseStack0, $$10, int2, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}