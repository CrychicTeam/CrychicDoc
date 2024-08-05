package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.ShulkerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Shulker;

public class ShulkerHeadLayer extends RenderLayer<Shulker, ShulkerModel<Shulker>> {

    public ShulkerHeadLayer(RenderLayerParent<Shulker, ShulkerModel<Shulker>> renderLayerParentShulkerShulkerModelShulker0) {
        super(renderLayerParentShulkerShulkerModelShulker0);
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Shulker shulker3, float float4, float float5, float float6, float float7, float float8, float float9) {
        ResourceLocation $$10 = ShulkerRenderer.getTextureLocation(shulker3.getColor());
        VertexConsumer $$11 = multiBufferSource1.getBuffer(RenderType.entitySolid($$10));
        ((ShulkerModel) this.m_117386_()).getHead().render(poseStack0, $$11, int2, LivingEntityRenderer.getOverlayCoords(shulker3, 0.0F));
    }
}