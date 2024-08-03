package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final ResourceLocation WINGS_LOCATION = new ResourceLocation("textures/entity/elytra.png");

    private final ElytraModel<T> elytraModel;

    public ElytraLayer(RenderLayerParent<T, M> renderLayerParentTM0, EntityModelSet entityModelSet1) {
        super(renderLayerParentTM0);
        this.elytraModel = new ElytraModel<>(entityModelSet1.bakeLayer(ModelLayers.ELYTRA));
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        ItemStack $$10 = t3.getItemBySlot(EquipmentSlot.CHEST);
        if ($$10.is(Items.ELYTRA)) {
            ResourceLocation $$12;
            if (t3 instanceof AbstractClientPlayer $$11) {
                if ($$11.isElytraLoaded() && $$11.getElytraTextureLocation() != null) {
                    $$12 = $$11.getElytraTextureLocation();
                } else if ($$11.isCapeLoaded() && $$11.getCloakTextureLocation() != null && $$11.m_36170_(PlayerModelPart.CAPE)) {
                    $$12 = $$11.getCloakTextureLocation();
                } else {
                    $$12 = WINGS_LOCATION;
                }
            } else {
                $$12 = WINGS_LOCATION;
            }
            poseStack0.pushPose();
            poseStack0.translate(0.0F, 0.0F, 0.125F);
            this.m_117386_().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(t3, float4, float5, float7, float8, float9);
            VertexConsumer $$16 = ItemRenderer.getArmorFoilBuffer(multiBufferSource1, RenderType.armorCutoutNoCull($$12), false, $$10.hasFoil());
            this.elytraModel.m_7695_(poseStack0, $$16, int2, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack0.popPose();
        }
    }
}