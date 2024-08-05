package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;

public abstract class EyesLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public EyesLayer(RenderLayerParent<T, M> renderLayerParentTM0) {
        super(renderLayerParentTM0);
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        VertexConsumer $$10 = multiBufferSource1.getBuffer(this.renderType());
        this.m_117386_().m_7695_(poseStack0, $$10, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public abstract RenderType renderType();
}