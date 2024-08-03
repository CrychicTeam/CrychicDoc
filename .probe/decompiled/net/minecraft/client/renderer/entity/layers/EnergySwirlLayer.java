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
import net.minecraft.world.entity.PowerableMob;

public abstract class EnergySwirlLayer<T extends Entity & PowerableMob, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public EnergySwirlLayer(RenderLayerParent<T, M> renderLayerParentTM0) {
        super(renderLayerParentTM0);
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (t3.isPowered()) {
            float $$10 = (float) t3.tickCount + float6;
            EntityModel<T> $$11 = this.model();
            $$11.prepareMobModel(t3, float4, float5, float6);
            this.m_117386_().copyPropertiesTo($$11);
            VertexConsumer $$12 = multiBufferSource1.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset($$10) % 1.0F, $$10 * 0.01F % 1.0F));
            $$11.setupAnim(t3, float4, float5, float7, float8, float9);
            $$11.m_7695_(poseStack0, $$12, int2, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
        }
    }

    protected abstract float xOffset(float var1);

    protected abstract ResourceLocation getTextureLocation();

    protected abstract EntityModel<T> model();
}