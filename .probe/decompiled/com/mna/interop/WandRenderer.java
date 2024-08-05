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

public class WandRenderer implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.pushPose();
        ICurioRenderer.translateIfSneaking(matrixStack, slotContext.entity());
        ICurioRenderer.rotateIfSneaking(matrixStack, slotContext.entity());
        float scale = 1.25F;
        matrixStack.translate(0.275, 0.8F, 0.2F);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-110.0F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        matrixStack.scale(scale, scale, scale);
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer, mc.level, light);
        matrixStack.popPose();
    }
}