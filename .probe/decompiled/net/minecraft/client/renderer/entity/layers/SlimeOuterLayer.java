package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;

public class SlimeOuterLayer<T extends LivingEntity> extends RenderLayer<T, SlimeModel<T>> {

    private final EntityModel<T> model;

    public SlimeOuterLayer(RenderLayerParent<T, SlimeModel<T>> renderLayerParentTSlimeModelT0, EntityModelSet entityModelSet1) {
        super(renderLayerParentTSlimeModelT0);
        this.model = new SlimeModel<>(entityModelSet1.bakeLayer(ModelLayers.SLIME_OUTER));
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        Minecraft $$10 = Minecraft.getInstance();
        boolean $$11 = $$10.shouldEntityAppearGlowing(t3) && t3.m_20145_();
        if (!t3.m_20145_() || $$11) {
            VertexConsumer $$12;
            if ($$11) {
                $$12 = multiBufferSource1.getBuffer(RenderType.outline(this.m_117347_(t3)));
            } else {
                $$12 = multiBufferSource1.getBuffer(RenderType.entityTranslucent(this.m_117347_(t3)));
            }
            ((SlimeModel) this.m_117386_()).m_102624_(this.model);
            this.model.prepareMobModel(t3, float4, float5, float6);
            this.model.setupAnim(t3, float4, float5, float7, float8, float9);
            this.model.m_7695_(poseStack0, $$12, int2, LivingEntityRenderer.getOverlayCoords(t3, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}