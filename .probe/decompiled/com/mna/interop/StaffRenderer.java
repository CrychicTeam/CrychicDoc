package com.mna.interop;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class StaffRenderer implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.pushPose();
        ICurioRenderer.translateIfSneaking(matrixStack, slotContext.entity());
        ICurioRenderer.rotateIfSneaking(matrixStack, slotContext.entity());
        matrixStack.translate(0.0F, 0.3F, 0.2F);
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        matrixStack.translate(0.5, -0.7, 0.0);
        matrixStack.scale(2.0F, 2.0F, 2.0F);
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer, mc.level, light);
        matrixStack.popPose();
    }
}