package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public abstract class RenderLayer<T extends Entity, M extends EntityModel<T>> {

    private final RenderLayerParent<T, M> renderer;

    public RenderLayer(RenderLayerParent<T, M> renderLayerParentTM0) {
        this.renderer = renderLayerParentTM0;
    }

    protected static <T extends LivingEntity> void coloredCutoutModelCopyLayerRender(EntityModel<T> entityModelT0, EntityModel<T> entityModelT1, ResourceLocation resourceLocation2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5, T t6, float float7, float float8, float float9, float float10, float float11, float float12, float float13, float float14, float float15) {
        if (!t6.m_20145_()) {
            entityModelT0.copyPropertiesTo(entityModelT1);
            entityModelT1.prepareMobModel(t6, float7, float8, float12);
            entityModelT1.setupAnim(t6, float7, float8, float9, float10, float11);
            renderColoredCutoutModel(entityModelT1, resourceLocation2, poseStack3, multiBufferSource4, int5, t6, float13, float14, float15);
        }
    }

    protected static <T extends LivingEntity> void renderColoredCutoutModel(EntityModel<T> entityModelT0, ResourceLocation resourceLocation1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, T t5, float float6, float float7, float float8) {
        VertexConsumer $$9 = multiBufferSource3.getBuffer(RenderType.entityCutoutNoCull(resourceLocation1));
        entityModelT0.m_7695_(poseStack2, $$9, int4, LivingEntityRenderer.getOverlayCoords(t5, 0.0F), float6, float7, float8, 1.0F);
    }

    public M getParentModel() {
        return this.renderer.getModel();
    }

    protected ResourceLocation getTextureLocation(T t0) {
        return this.renderer.getTextureLocation(t0);
    }

    public abstract void render(PoseStack var1, MultiBufferSource var2, int var3, T var4, float var5, float var6, float var7, float var8, float var9, float var10);
}