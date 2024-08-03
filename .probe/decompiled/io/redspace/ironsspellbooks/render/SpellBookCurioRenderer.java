package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@OnlyIn(Dist.CLIENT)
public class SpellBookCurioRenderer implements ICurioRenderer {

    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack itemStack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (renderLayerParent.getModel() instanceof HumanoidModel) {
            HumanoidModel<LivingEntity> humanoidModel = (HumanoidModel<LivingEntity>) renderLayerParent.getModel();
            poseStack.pushPose();
            humanoidModel.body.translateAndRotate(poseStack);
            poseStack.translate((slotContext.entity() != null && !slotContext.entity().getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? -5.5 : -4.5) * 0.0625, 0.5625, 0.0);
            poseStack.mulPose(Axis.YP.rotation((float) Math.PI));
            poseStack.mulPose(Axis.ZP.rotation(3.0543263F));
            poseStack.scale(0.625F, 0.625F, 0.625F);
            this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, poseStack, renderTypeBuffer, null, 0);
            poseStack.popPose();
        }
    }
}