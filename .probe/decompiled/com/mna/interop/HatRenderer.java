package com.mna.interop;

import com.mna.items.ItemInit;
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

public class HatRenderer implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.pushPose();
        matrixStack.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
        if (slotContext.entity().m_6047_()) {
            matrixStack.translate(0.0, 0.25, 0.0);
        }
        matrixStack.mulPose(Axis.XP.rotationDegrees(-90.0F + headPitch));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        matrixStack.translate(0.0, 0.0, -0.525);
        if (stack.getItem() == ItemInit.HAT_STYLED_SKULL.get()) {
            matrixStack.translate(0.0, 0.0, -0.3);
        }
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer, mc.level, light);
        matrixStack.popPose();
    }
}