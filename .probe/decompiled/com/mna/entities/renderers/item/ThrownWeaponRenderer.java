package com.mna.entities.renderers.item;

import com.mna.entities.boss.attacks.ThrownAllfatherAxe;
import com.mna.entities.boss.attacks.ThrownWeapon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class ThrownWeaponRenderer extends EntityRenderer<ThrownWeapon> {

    final Minecraft mc = Minecraft.getInstance();

    public ThrownWeaponRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(ThrownWeapon entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(((float) entityIn.f_19797_ + partialTicks) * 60.0F));
        float scale = entityIn.getRenderScale();
        matrixStackIn.scale(scale, scale, scale);
        this.mc.getItemRenderer().renderStatic(ThrownAllfatherAxe.renderStack, ItemDisplayContext.NONE, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
        matrixStackIn.popPose();
    }

    public ResourceLocation getTextureLocation(ThrownWeapon entity) {
        return null;
    }
}