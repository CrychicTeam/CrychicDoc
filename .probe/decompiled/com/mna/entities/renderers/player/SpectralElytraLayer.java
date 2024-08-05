package com.mna.entities.renderers.player;

import com.mna.items.ItemInit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SpectralElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("mna", "textures/entity/elytra.png");

    private final ElytraModel<T> modelElytra;

    public SpectralElytraLayer(RenderLayerParent<T, M> rendererIn, EntityModelSet entityModelSet0) {
        super(rendererIn);
        this.modelElytra = new ElytraModel<>(entityModelSet0.bakeLayer(ModelLayers.ELYTRA));
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entitylivingbaseIn.getItemBySlot(EquipmentSlot.CHEST);
        if (itemstack.getItem() == ItemInit.SPECTRAL_ELYTRA.get()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0, 0.0, 0.125);
            this.m_117386_().copyPropertiesTo(this.modelElytra);
            this.modelElytra.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, this.modelElytra.m_103119_(TEXTURE_ELYTRA), false, true);
            this.modelElytra.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
    }
}